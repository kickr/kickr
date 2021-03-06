package kickr.db.dao;

import io.dropwizard.hibernate.AbstractDAO;
import kickr.db.entity.user.AccessToken;
import kickr.db.entity.user.User;
import org.hibernate.CacheMode;
import org.hibernate.SessionFactory;

/**
 *
 * @author nikku
 */
public class AccessTokenDAO extends AbstractDAO<AccessToken> {

  public AccessTokenDAO(SessionFactory sessionFactory) {
    super(sessionFactory);
  }

  public void remove(AccessToken accessToken) {
    currentSession().delete(accessToken);
  }

  public void create(AccessToken accessToken) {
    currentSession().persist(accessToken);
  }
  
  public AccessToken get(String tokenValue) {

    return (AccessToken)
      currentSession()
        .getNamedQuery("AccessToken.byValue")
        .setParameter("value", tokenValue)
        .setCacheable(true)
        .uniqueResult();
  }

  public int removeByUser(User user) {
    return currentSession()
             .createQuery("DELETE FROM AccessToken a WHERE a.user = :user")
               .setParameter("user", user)
               .executeUpdate();
  }

  public int removeByToken(User user, String token) {
    return currentSession()
             .createQuery("DELETE FROM AccessToken a WHERE a.user = :user AND a.value = :token")
               .setParameter("user", user)
               .setParameter("token", token)
               .executeUpdate();
  }
}
