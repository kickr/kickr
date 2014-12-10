package kickr.db;

import io.dropwizard.hibernate.AbstractDAO;

import java.util.List;

import kickr.db.entity.Player;

import org.hibernate.SessionFactory;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;

public class PlayerDAO extends AbstractDAO<Player> {

  public PlayerDAO(SessionFactory sessionFactory) {
    super(sessionFactory);
  }
  
  public void create(Player player) {
    persist(player);
  }
  
  @SuppressWarnings("unchecked")
  public List<Player> findPlayersMatchingCriteria(String name, String alias) {
    return criteria().add(
        Restrictions.or(
            Restrictions.like("name", name, MatchMode.ANYWHERE),
            Restrictions.like("alias", alias, MatchMode.ANYWHERE)))
        .list();
  }
  
  public Player findPlayerByAlias(String alias) {
    return (Player) criteria().add(Restrictions.like("alias", alias)).uniqueResult();
  }

}
