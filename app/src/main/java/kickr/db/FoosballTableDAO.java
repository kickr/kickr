package kickr.db;

import java.util.List;

import org.hibernate.SessionFactory;

import kickr.db.entity.FoosballTable;
import io.dropwizard.hibernate.AbstractDAO;
import javax.inject.Inject;
import javax.persistence.NoResultException;

public class FoosballTableDAO extends AbstractDAO<FoosballTable> {

  public FoosballTableDAO(SessionFactory sessionFactory) {
    super(sessionFactory);
  }
  
  public void createTable(FoosballTable table) {
    persist(table);
  }
  
  @SuppressWarnings("unchecked")
  public List<FoosballTable> getTables(int firstResult, int maxResults) {
    return criteria().setFirstResult(firstResult).setMaxResults(maxResults).list();
  }

  public FoosballTable findTableById(Long id) {
    FoosballTable table = get(id);
    
    if (table == null) {
      throw new NoResultException("Table not found");
    }
    
    return table;
  }
}
