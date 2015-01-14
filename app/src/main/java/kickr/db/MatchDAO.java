package kickr.db;

import io.dropwizard.hibernate.AbstractDAO;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;

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
  
  public List<Match> getMatches(int firstResult, int maxResults) {
    return list(namedQuery("Match.getAll")
                  .setFirstResult(firstResult)
                  .setMaxResults(maxResults));
  }

  public void removeMatch(Long id) {
    Match match = get(id);
    removeMatch(match);
  }

  public void removeMatch(Match match) {
    match.setRemoved(true);
  }

  public List<Match> getUnratedMatches(Duration delay) {
    Date played = Date.from(Instant.now().minus(delay));
    
    return list(namedQuery("Match.getUnrated").setParameter("played", played));
  }
}
