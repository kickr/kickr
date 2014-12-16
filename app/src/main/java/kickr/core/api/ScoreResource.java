package kickr.core.api;

import io.dropwizard.hibernate.UnitOfWork;
import java.util.List;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import kickr.core.model.ScoreData;
import kickr.db.ScoreDAO;
import kickr.db.entity.Score;
import kickr.db.entity.ScoreWithChanges;

/**
 *
 * @author nikku
 */
@Path("/score")
public class ScoreResource extends BaseResource {
  
  private final ScoreDAO scoreDao;

  public ScoreResource(ScoreDAO scoreDao) {
    this.scoreDao = scoreDao;
  }
  
  @GET
  @UnitOfWork
  public List<ScoreData> getScores(
      @QueryParam("firstResult") @DefaultValue("0") int firstResult, 
      @QueryParam("maxResults") @DefaultValue("10") int maxResults) {
    
    assertValidPagination(firstResult, maxResults);
    
    List<ScoreWithChanges> scores = scoreDao.getAll(firstResult, maxResults);
    
    return ScoreData.fromScores(scores);
  }
}
