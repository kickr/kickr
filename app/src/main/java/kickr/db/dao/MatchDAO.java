package kickr.db.dao;

import io.dropwizard.hibernate.AbstractDAO;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;

import java.util.List;

import kickr.db.entity.Match;
import kickr.db.util.Page;
import org.hibernate.Query;

import org.hibernate.SessionFactory;

public class MatchDAO extends AbstractDAO<Match> {

  public MatchDAO(SessionFactory sessionFactory) {
    super(sessionFactory);
  }
  
  public Match findMatchById(Long id) {
    return uniqueResult(namedQuery("Match.get").setParameter("id", id));
  }
  
  public void create(Match match) {
    persist(match);
  }
  
  public List<Match> getMatches(Page page) {
    return list(namedQuery("Match.getAll")
                  .setFirstResult(page.firstResult)
                  .setMaxResults(page.maxResults));
  }

  public List<Match> getMatchesByAliases(List<String> aliases, Page page) {

    StringBuilder filterBuilder = new StringBuilder(
      "SELECT m FROM Match m " +
        "LEFT JOIN FETCH m.team1.offense " +
        "LEFT JOIN FETCH m.team1.defense " +
        "LEFT JOIN FETCH m.team2.offense " +
        "LEFT JOIN FETCH m.team2.defense " +
        "JOIN FETCH m.table " +
        "LEFT JOIN FETCH m.creator " +
      "WHERE m.played IS NOT NULL ");

    addAliasFilterers(filterBuilder, aliases);

    filterBuilder.append("ORDER BY m.played DESC");

    Query query = currentSession().createQuery(filterBuilder.toString());

    addAliasParameters(query, aliases);

    return list(query
              .setFirstResult(page.firstResult)
              .setMaxResults(page.maxResults));
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

  private void addAliasFilterers(StringBuilder filterBuilder, List<String> aliases) {
    for (int i = 0; i < aliases.size(); i++) {
      filterBuilder.append("AND m.participants LIKE :filter").append(i).append(" ");
    }
  }

  private void addAliasParameters(Query query, List<String> aliases) {
    for (int i = 0; i < aliases.size(); i++) {
      query.setParameter("filter" + i, "%:" + aliases.get(i) + ":%");
    }
  }
}
