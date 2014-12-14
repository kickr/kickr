package kickr.db;

import kickr.db.entity.Score;
import io.dropwizard.hibernate.AbstractDAO;
import java.util.List;
import javax.persistence.NoResultException;
import kickr.db.entity.Player;
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
      score = new Score(player, 100);
      persist(score);
    }

    return score;
  }

  public List<Score> getAll(int firstResult, int maxResults) {
    return list(currentSession()
                  .createQuery("SELECT s FROM Score s JOIN FETCH s.player ORDER BY s.value DESC")
                  .setFirstResult(firstResult)
                  .setMaxResults(maxResults));
  }
}

