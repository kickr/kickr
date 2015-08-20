package kickr.web.api;

import kickr.web.view.match.NewMatchView;
import kickr.web.view.match.MatchesView;
import kickr.web.model.match.MatchData;
import io.dropwizard.hibernate.UnitOfWork;

import java.util.List;
import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;

import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.NotAcceptableException;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import kickr.db.MatchDAO;
import kickr.db.entity.Match;
import kickr.db.entity.user.User;
import kickr.service.MatchService;
import kickr.web.BaseResource;
import kickr.web.form.CreateMatchForm;
import kickr.web.view.match.LatestMatchesView;
import support.form.FormData;
import support.security.annotation.Auth;

@Path("match")
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

    return createView(MatchesView.class).withMatches(MatchData.fromMatches(matches));
  }

  @GET
  @Path("latest")
  @UnitOfWork
  public LatestMatchesView listLatest() {
    List<Match> matches = matchDao.getMatches(0, 5);

    return createView(LatestMatchesView.class).withMatches(MatchData.fromMatches(matches));
  }

  @GET
  @RolesAllowed("user")
  @Path("new")
  public NewMatchView newForm() {
    return createView(NewMatchView.class);
  }

  @POST
  @Path("new")
  @RolesAllowed("user")
  @UnitOfWork
  public void createFromForm(@Auth User user, @Valid @FormData CreateMatchForm createMatchData) {
    System.out.println(createMatchData);
    // matchService.insertMatch(createMatchData, user);
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
      throw new NotAcceptableException("already rated");
    }

    matchDao.removeMatch(match);
  }

}
