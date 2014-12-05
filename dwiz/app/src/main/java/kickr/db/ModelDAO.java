package kickr.db;

import kickr.db.entity.Model;
import io.dropwizard.hibernate.AbstractDAO;
import java.util.List;
import org.hibernate.SessionFactory;

/**
 *
 * @author nikku
 */
public class ModelDAO extends AbstractDAO<Model> {

  public ModelDAO(SessionFactory sessionFactory) {
    super(sessionFactory);
  }

  public Model create(Model model) {
    return persist(model);
  }

  public List<Model> query() {
    return list(namedQuery("Model.findLatest").setMaxResults(5));
  }
  
  public Model findByKey(String id) {
    return get(id);
  }
  
  public Model findLatestById(String id) {
    return uniqueResult(namedQuery("Model.findById").setParameter("id", id).setMaxResults(1));
  }

  public Model findByRevision(String id, long revision) {
    return uniqueResult(namedQuery("Model.findByRevision").setParameter("id", id).setParameter("revision", revision));
  }
}