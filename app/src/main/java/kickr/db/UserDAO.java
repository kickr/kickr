package kickr.db;

import io.dropwizard.hibernate.AbstractDAO;
import kickr.db.entity.user.User;
import org.hibernate.SessionFactory;

/**
 *
 * @author nikku
 */
public class UserDAO extends AbstractDAO<User> {

  public UserDAO(SessionFactory sessionFactory) {
    super(sessionFactory);
  }

  public User getByName(String name) {
    return uniqueResult(currentSession().getNamedQuery("User.byName").setParameter("name", name));
  }

  public void createUser(User user) {
    currentSession().persist(user);
  }
}
