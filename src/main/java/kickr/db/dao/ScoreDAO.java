package kickr.db.dao;

import kickr.db.dto.PlayerStatistics;
import kickr.db.entity.Score;
import java.time.Instant;
import java.time.Period;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import static java.util.stream.Collectors.toList;
import kickr.analysis.model.ScoreDefinition;
import kickr.db.entity.Player;
import kickr.db.entity.ScoreChange;
import kickr.db.dto.ScoreWithChanges;
import kickr.db.util.Page;
import org.hibernate.SQLQuery;
import org.hibernate.SessionFactory;

/**
 *
 * @author nikku
 */
public class ScoreDAO extends BaseDAO<Score> {

  public ScoreDAO(SessionFactory sessionFactory) {
    super(sessionFactory);
  }

  public Score getLatestScore(ScoreDefinition scoreDefinition, Player player) {

    String scoreType = scoreDefinition.getKey();

    return uniqueResult(namedQuery("Score.latestByPlayer")
                                  .setParameter("player", player)
                                  .setParameter("scoreType", scoreType)
                                  .setParameter("latestDate", new Date())
                                  .setMaxResults(1));
  }

  public List<PlayerStatistics> getStatistics(Page page) {

    Date latestDate = Date.from(Instant.now().minus(Period.ofDays(7)));

    List<Object[]> results = (List<Object[]>) currentSession()
        .createQuery("SELECT p, SUM(c.value) FROM ScoreChange c JOIN c.player p WHERE c.created > :latestDate GROUP BY p.id, c.match.id")
        .setDate("latestDate", latestDate)
        .list();

    Map<Player, List<Object[]>> gameValues = results.stream().collect(Collectors.groupingBy(r -> (Player) r[0]));

    List<PlayerStatistics> playerStatistics = new ArrayList<>();

    gameValues.forEach((player, rows) -> {

      long games = rows.size();
      long score =  0;

      for (Object[] r: rows) {
        score += (Long) r[1];
      }

      playerStatistics.add(new PlayerStatistics(player, score, games));
    });

    return playerStatistics.stream().sorted((s1, s2) -> {
      return -1 * Double.compare(s1.getRating(), s2.getRating());
    }).skip(page.firstResult)
      .limit(page.maxResults)
      .collect(Collectors.toList());
  }

  public <T> Function<Object, T> extractValue(Class<T> cls, int index) {
    return (row) -> {
      return cls.cast(((Object[]) row)[index]);
    };
  }

  public Map<Player, Score> getLatestScores(ScoreDefinition scoreDefinition, Collection<Player> players, Date latestDate) {

    SQLQuery query = currentSession()
            .createSQLQuery("SELECT s.*, p.* FROM kickr_score s " +
                              "JOIN (" +
                                "SELECT MAX(sm.created) created, MAX(sm.run_index) run_index, sm.player_id FROM kickr_score sm " +
                                  "WHERE sm.score_type = :scoreType AND sm.created <= :latestDate " +
                                  "GROUP BY sm.player_id " +
                              ") so ON (" +
                                "s.created = so.created AND " +
                                "s.run_index = so.run_index AND " +
                                "s.player_id = so.player_id " +
                              ") " +
                              "JOIN kickr_player p ON (s.player_id = p.id) " +
                            "WHERE s.score_type = :scoreType AND s.player_id IN :players");

    query.setParameterList("players", players);
    query.setParameter("latestDate", latestDate);
    query.setParameter("scoreType", scoreDefinition.getKey());
    
    query.addEntity("s", Score.class);
    query.addEntity("p", Player.class);
    query.addFetch("p", "s", "player");

    List<Object> results = query.list();

    return results.stream()
              .map(extractValue(Score.class, 0))
              .reduce(new HashMap<Player, Score>(),
                  (map, s) -> {
                    map.put(s.getPlayer(), s);
                    return map;
                  },
                  (a, b) -> a);
  }

  public List<ScoreWithChanges> getScoresWithChanges(Date latestDate, Page page) {

    List<Score> scores = getScores(latestDate, page);

    List<Player> players = scores.stream().map(s ->  s.getPlayer()).collect(toList());

    Score nullScore = new Score();

    Map<Player, Score> latestScores = getLatestScores(ScoreDefinition.LAST_WEEK, players, latestDate);

    return scores.stream().map((s) -> {
      Player player = s.getPlayer();

      return new ScoreWithChanges(s, latestScores.getOrDefault(player, nullScore).getValue());
    }).collect(toList());
  }

  public List<ScoreWithChanges> getScoresWithChanges(Page page) {
    return this.getScoresWithChanges(new Date(), page);
  }

  public void createChanges(ScoreChange update) {
    currentSession().persist(update);
  }

  public Score save(Score score) {
    return persist(score);
  }

  public List<Score> getScores(Date latestDate) {
    return getScores(latestDate, null);
  }
  
  public List<Score> getScores(Date latestDate, Page page) {

    SQLQuery query = currentSession()
            .createSQLQuery("SELECT s.*, p.* FROM kickr_score s " +
                              "JOIN (" +
                                "SELECT MAX(sm.created) created, MAX(sm.run_index) run_index, sm.player_id FROM kickr_score sm " +
                                  "WHERE sm.score_type = :scoreType AND sm.created <= :latestDate " +
                                  "GROUP BY sm.player_id " +
                              ") so ON (" +
                                "s.created = so.created AND " +
                                "s.run_index = so.run_index AND " +
                                "s.player_id = so.player_id " +
                              ") " +
                              "JOIN kickr_player p ON (s.player_id = p.id) " +
                            "WHERE s.score_type = :scoreType " +
                            "ORDER BY s.value DESC");

    if (page != null) {
      paginate(query, page);
    }

    query.setParameter("scoreType", ScoreDefinition.DEFAULT.getKey());
    query.setParameter("latestDate", latestDate);

    query.addEntity("s", Score.class);
    query.addEntity("p", Player.class);
    query.addFetch("p", "s", "player");

    List<Object> results = query.list();

    return results.stream().map(extractValue(Score.class, 0)).collect(toList());
  }

}