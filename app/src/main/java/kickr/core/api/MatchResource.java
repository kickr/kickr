package kickr.core.api;

import io.dropwizard.hibernate.UnitOfWork;

import java.util.List;
import javax.annotation.security.RolesAllowed;

import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import kickr.core.model.CoreMatchData;
import kickr.core.model.MatchData;
import kickr.db.MatchDAO;
import kickr.db.entity.Match;
import kickr.db.entity.user.User;
import kickr.service.MatchService;
import support.security.annotation.Auth;

@Path("/match")
@Produces(MediaType.APPLICATION_JSON)
public class MatchResource extends BaseResource {

  protected MatchDAO matchDao;

  protected MatchService matchService;
  
  public MatchResource(MatchService matchService, MatchDAO matchDao) {
    this.matchDao = matchDao;
    this.matchService = matchService;
  }
  
  @GET
  @UnitOfWork
  public List<MatchData> getMatches(
      @QueryParam("firstResult") @DefaultValue("0") int firstResult, 
      @QueryParam("maxResults") @DefaultValue("10") int maxResults) {
    
    assertValidPagination(firstResult, maxResults);
    
    List<Match> matches = matchDao.getMatches(firstResult, maxResults);
    
    return MatchData.fromMatches(matches);
  }
  
  @POST
  @RolesAllowed("user")
  @UnitOfWork
  public void create(@Auth User user, CoreMatchData createMatchData) {
    matchService.insertMatch(createMatchData, user);
  }
  
  @GET
  @Path("{id}")
  @UnitOfWork
  public MatchData getMatch(@PathParam("id") Long id) {
    Match match = matchDao.findMatchById(id);
    
    return MatchData.fromMatch(match);
  }
  
  @DELETE
  @Path("{id}")
  @RolesAllowed("user")
  @UnitOfWork
  public void removeMatch(@PathParam("id") Long id) {
    matchDao.removeMatch(id);
  }
}
