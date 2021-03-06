package kickr.db.dao;

import kickr.db.entity.PlayerStatistics;
import kickr.db.entity.Score;
import io.dropwizard.hibernate.AbstractDAO;
import java.time.Instant;
import java.time.Period;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import kickr.db.entity.Player;
import kickr.db.entity.ScoreChange;
import kickr.db.entity.ScoreWithChanges;
import org.hibernate.SessionFactory;

/**
 *
 * @author nikku
 */
public class ScoreDAO extends AbstractDAO<Score> {

  public ScoreDAO(SessionFactory sessionFactory) {
    super(sessionFactory);
  }

  public Score getOrCreateScore(Player player) {
    Score score = uniqueResult(namedQuery("Score.byPlayer").setParameter("player", player));

    if (score == null) {
      score = new Score(player, 0);
      persist(score);
    }

    return score;
  }

  public List<PlayerStatistics> getStatistics(int firstResult, int maxResults) {

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
    }).skip(firstResult)
      .limit(maxResults)
      .collect(Collectors.toList());
  }

  public List<ScoreWithChanges> getScoresWithChanges(int firstResult, int maxResults) {
    
    List<Score> scores = list(currentSession()
                            .createQuery("SELECT s FROM Score s ORDER BY s.value DESC")
                            .setFirstResult(firstResult)
                            .setMaxResults(maxResults));
   
    if (scores.isEmpty()) {
      return Collections.emptyList();
    }

    
    Date latestDate = Date.from(Instant.now().minus(Period.ofDays(7)));
    
    List<ScoreChange> changes = currentSession()
                                   .createQuery(
                                      "SELECT c FROM ScoreChange c " +
                                        "JOIN FETCH c.match " +
                                        "JOIN FETCH c.score " +
                                        "JOIN FETCH c.score.player " +
                                      "WHERE c.created > :latestDate AND c.score IN :scores")
                                   .setDate("latestDate", latestDate)
                                   .setParameterList("scores", scores)
                                   .list();
    
    Map<Score, List<ScoreChange>> changesByScore = changes.stream()
                                                      .collect(Collectors.groupingBy(ScoreChange::getScore));
    
    return scores.stream()
              .map(s -> new ScoreWithChanges(s, changesByScore.getOrDefault(s, Collections.emptyList())))
              .collect(Collectors.toList());
  }

  public void createChanges(ScoreChange update) {
    currentSession().persist(update);
  }
}