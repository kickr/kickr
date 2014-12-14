package kickr.db;

import io.dropwizard.hibernate.AbstractDAO;

import java.util.List;
import javax.inject.Inject;

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
  
  public void createPlayerIfNotExists(Player player) {
    if (findPlayerByAlias(player.getAlias()) == null) {
      create(player);
    }
  }
  
  @SuppressWarnings("unchecked")
  public List<Player> findPlayersMatchingNameOrAlias(String part) {
    return criteria().add(
        Restrictions.or(
            Restrictions.ilike("name", part, MatchMode.ANYWHERE),
            Restrictions.ilike("alias", part, MatchMode.ANYWHERE)))
        .list();
  }
  
  public Player findPlayerByAlias(String alias) {
    return (Player) criteria().add(Restrictions.eq("alias", alias)).uniqueResult();
  }

}
