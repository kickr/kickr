package kickr.web.api;

import io.dropwizard.hibernate.UnitOfWork;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import kickr.web.model.FoosballTableData;
import kickr.db.dao.FoosballTableDAO;
import kickr.db.entity.FoosballTable;

@Path("table")
@Produces(MediaType.APPLICATION_JSON)
public class TableResource {

  protected FoosballTableDAO tableDao;
  
  public TableResource(FoosballTableDAO tableDao) {
    this.tableDao = tableDao;
  }
  
  @GET
  @UnitOfWork
  public List<FoosballTableData> getTables(@QueryParam("firstResult") int firstResult, 
      @QueryParam("maxResults") int maxResults) {
    List<FoosballTable> tables = tableDao.getTables(firstResult, maxResults);
    
    return FoosballTableData.fromTables(tables);
  }
}
