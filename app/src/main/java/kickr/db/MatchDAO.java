package kickr.db;

import io.dropwizard.hibernate.AbstractDAO;

import java.util.List;

import kickr.db.entity.Match;

import org.hibernate.SessionFactory;

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
    return criteria().setFirstResult(firstResult).setMaxResults(maxResults).list();
  }

}
