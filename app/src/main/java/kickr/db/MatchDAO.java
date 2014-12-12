package kickr.db;

import io.dropwizard.hibernate.AbstractDAO;

import java.util.List;

import kickr.db.entity.Match;
import org.hibernate.Criteria;

import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;

public class MatchDAO extends AbstractDAO<Match> {

  public MatchDAO(SessionFactory sessionFactory) {
    super(sessionFactory);
  }
  
  public Match findMatchById(Long id) {
    return get(id);
  }
  
  public void create(Match match) {
    persist(match);
  }
  
  @SuppressWarnings("unchecked")
  public List<Match> getMatches(int firstResult, int maxResults) {
    return namedQuery("Match.list")
        .setFirstResult(firstResult)
        .setMaxResults(maxResults)
        .list();
  }

  public void removeMatch(Long id) {
    Match match = get(id);
    match.setRemoved(true);
  }
}
