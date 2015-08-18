package kickr.web.api;

import kickr.web.view.NewMatchView;
import kickr.web.view.MatchesView;
import kickr.web.model.match.CoreMatchData;
import kickr.web.model.match.MatchData;
import io.dropwizard.hibernate.UnitOfWork;

import java.util.List;
import javax.annotation.security.RolesAllowed;

import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

import kickr.db.MatchDAO;
import kickr.db.entity.Match;
import kickr.db.entity.user.User;
import kickr.service.MatchService;
import kickr.web.BaseResource;
import kickr.web.view.LatestMatchesView;
import support.security.annotation.Auth;

@Path("/matches")
public class MatchResource extends BaseResource {

  protected MatchDAO matchDao;

  protected MatchService matchService;

  public MatchResource(MatchService matchService, MatchDAO matchDao) {
    this.matchDao = matchDao;
    this.matchService = matchService;
  }


  @GET
  @RolesAllowed("user")
  @UnitOfWork
  public MatchesView list(
      @QueryParam("firstResult") @DefaultValue("0") int firstResult,
      @QueryParam("maxResults") @DefaultValue("10") int maxResults) {

    assertValidPagination(firstResult, maxResults);

    List<Match> matches = matchDao.getMatches(firstResult, maxResults);

    return new MatchesView(MatchData.fromMatches(matches));
  }


  @GET
  @Path("latest")
  @UnitOfWork
  public LatestMatchesView listLatest() {

    List<Match> matches = matchDao.getMatches(0, 5);

    return new LatestMatchesView(MatchData.fromMatches(matches));
  }


  @GET
  @RolesAllowed("user")
  @Path("new")
  public NewMatchView newMatch() {
    return new NewMatchView();
  }

  @POST
  @RolesAllowed("user")
  @UnitOfWork
  public void create(@Auth User user, CoreMatchData createMatchData) {
    matchService.insertMatch(createMatchData, user);
  }

  @GET
  @Path("{id}")
  @RolesAllowed("user")
  @UnitOfWork
  public MatchData getMatch(@PathParam("id") Long id) {
    Match match = matchDao.findMatchById(id);

    return MatchData.fromMatch(match);
  }

  @DELETE
  @Path("{id}")
  @RolesAllowed("user")
  @UnitOfWork
  public void removeMatch(@Auth User user, @PathParam("id") Long id) {

    Match match = matchDao.findMatchById(id);

    if (match.isRated() && !user.hasRole("admin")) {
      throw new WebApplicationException(Response.status(Response.Status.NOT_ACCEPTABLE).entity("Already rated").build());
    }

    matchDao.removeMatch(match);
  }
}
