package kickr.db;

import kickr.db.entity.Score;
import io.dropwizard.hibernate.AbstractDAO;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
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

  public List<ScoreWithChanges> getAll(int firstResult, int maxResults) {
    
    List<Score> scores = list(currentSession()
                            .createQuery("SELECT s FROM Score s JOIN FETCH s.player ORDER BY s.value DESC")
                            .setFirstResult(firstResult)
                            .setMaxResults(maxResults));
   
    if (scores.isEmpty()) {
      return Collections.emptyList();
    }
    
    Date latestDate = Date.from(Instant.now().minus(5, ChronoUnit.DAYS));
    
    List<ScoreChange> changes = currentSession()
                                   .createQuery("SELECT c FROM ScoreChange c JOIN FETCH c.match JOIN FETCH c.score WHERE c.created > :latestDate AND c.score IN :scores")
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

