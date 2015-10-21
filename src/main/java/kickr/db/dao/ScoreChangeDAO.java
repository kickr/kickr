package kickr.db.dao;

import io.dropwizard.hibernate.AbstractDAO;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import kickr.analysis.model.ScoreDefinition;
import kickr.db.entity.Player;
import kickr.db.entity.ScoreChange;
import org.hibernate.Query;
import org.hibernate.SessionFactory;

/**
 *
 * @author nikku
 */
public class ScoreChangeDAO extends AbstractDAO<ScoreChange> {

  public ScoreChangeDAO(SessionFactory sessionFactory) {
    super(sessionFactory);
  }

  public ScoreChange save(ScoreChange scoreChange) {
    return persist(scoreChange);
  }

  public List<Player> findActivePlayers(ScoreDefinition scoreDefinition, Date from) {

    List<Player> players = currentSession()
                              .createQuery("SELECT s.player FROM Score s " + 
                                            "WHERE s.type = :type AND s.created > :from " +
                                            "GROUP BY s.player")
                                .setParameter("type", scoreDefinition.getKey())
                                .setParameter("from", from)
                                .list();
    
    return players;
  }

  public Map<Player, Integer> getScoresByDefinition(ScoreDefinition scoreDefinition, Date from, Date to) {

    Query query = currentSession().createQuery(
              "SELECT c.player, SUM(c.value) FROM ScoreChange c " +
                "WHERE c.created > :from AND " +
                      "c.created < :to AND " +
                      "c.scoreType = :scoreType " +
                  "GROUP BY c.player");

    List values = query
                    .setParameter("from", from)
                    .setParameter("to", to)
                    .setParameter("scoreType", scoreDefinition.getKey())
                    .list();

    Map<Player, Integer> sums = new HashMap<>();

    for (Object value: values) {
      Object[] actualValues = (Object[]) value;

      sums.put((Player) actualValues[0], (int) ((long) actualValues[1]));
    }
    
    return sums;
  }
}