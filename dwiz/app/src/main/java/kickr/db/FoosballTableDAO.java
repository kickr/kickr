package kickr.db;

import org.hibernate.SessionFactory;

import kickr.db.entity.FoosballTable;
import io.dropwizard.hibernate.AbstractDAO;

public class FoosballTableDAO extends AbstractDAO<FoosballTable> {

  public FoosballTableDAO(SessionFactory sessionFactory) {
    super(sessionFactory);
  }
  
  public void createTable(FoosballTable table) {
    persist(table);
  }

}
