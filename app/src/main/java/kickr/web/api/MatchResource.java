package kickr.web.api;

import kickr.web.view.match.NewMatchView;
import kickr.web.view.match.MatchesView;
import kickr.web.model.match.MatchData;
import io.dropwizard.hibernate.UnitOfWork;
import java.util.ArrayList;
import java.util.Date;

import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import javax.annotation.security.RolesAllowed;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import javax.ws.rs.BadRequestException;

import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.NotAcceptableException;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;

import kickr.db.MatchDAO;
import kickr.db.entity.FoosballTable;
import kickr.db.entity.Match;
import kickr.db.entity.user.User;
import kickr.service.MatchService;
import kickr.web.BaseResource;
import static kickr.web.api.PlayerUtil.parsePlayer;
import kickr.web.form.GamesUtil;
import kickr.web.form.NewMatchForm;
import kickr.web.model.GameData;
import kickr.web.model.FoosballTableData;
import kickr.web.model.TeamData;
import kickr.web.model.match.CoreMatchData;
import kickr.web.view.match.LatestMatchesView;
import kickr.web.view.match.MatchView;
import support.form.FormData;
import support.security.annotation.Auth;

@Path("match")
public class MatchResource extends BaseResource {

  protected MatchDAO matchDao;

  protected MatchService matchService;

  protected Validator validator;

  public MatchResource(MatchService matchService, MatchDAO matchDao, Validator validator) {
    this.matchDao = matchDao;
    this.matchService = matchService;

    this.validator = validator;
  }

  @GET
  @RolesAllowed("user")
  @UnitOfWork
  public MatchesView list(
      @QueryParam("page") @DefaultValue("1") int page,
      @QueryParam("filter") String filter,
      @QueryParam("search") String search) {

    int pageSize = 10;
    
    List<Match> matches = matchDao.getMatches((page - 1 * pageSize), pageSize);

    return createView(MatchesView.class)
              .withMatches(MatchData.fromMatches(matches))
              .withPage(page)
              .withFilter(filter)
              .withSearch(search);
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
    NewMatchForm.Table table = new NewMatchForm.Table(1l, "camunda HQ", "Kaffee", "Klo");

    return createView(NewMatchView.class).withForm(new NewMatchForm().withTable(table));
  }

  @POST
  @Path("new")
  @RolesAllowed("user")
  @UnitOfWork
  public Object createFromForm(@Auth User user, @FormData NewMatchForm newMatchForm) {

    Set<ConstraintViolation<NewMatchForm>> validationErrors = validator.validate(newMatchForm);

    if (!validationErrors.isEmpty()) {
      return createView(NewMatchView.class).withForm(newMatchForm.withErrors(validationErrors));
    }

    CoreMatchData matchData = new CoreMatchData();
    
    matchData.setTeam1(createTeamData(newMatchForm.getTeam1()));
    matchData.setTeam2(createTeamData(newMatchForm.getTeam2()));
    matchData.setTable(createTableData(newMatchForm.getTable()));

    Date played = newMatchForm.getPlayed();
    if (played == null) {
      played = new Date();
    }

    matchData.setPlayed(played);

    matchData.setGames(createGameData(newMatchForm.getGames()));
    
    Match createdMatch = matchService.insertMatch(matchData, user);

    return redirect("/match/" + createdMatch.getId()).build();
  }

  @GET
  @Path("{id}")
  @RolesAllowed("user")
  @UnitOfWork
  public MatchView getMatch(@PathParam("id") Long id) {
    Match match = matchDao.findMatchById(id);

    FoosballTable table = match.getTable();


    System.out.println(table);
    System.out.println(table.getName());

    return createView(MatchView.class)
                .withMatch(MatchData.fromMatch(match))
                .withTable(FoosballTableData.fromTable(table));
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

  private TeamData createTeamData(NewMatchForm.Team team) {
    return new TeamData(
      parsePlayer(team.getOffense()),
      parsePlayer(team.getDefense())
    );
  }

  private FoosballTableData createTableData(NewMatchForm.Table table) {
    return new FoosballTableData(table.getId());
  }

  private List<GameData> createGameData(String gamesStr) {

    Matcher matcher = GamesUtil.GAMES_PATTERN.matcher(gamesStr);

    if (!matcher.matches()) {
      throw new BadRequestException("Invalid input");
    }

    List<GameData> games = new ArrayList<>();

    int idx = 0;

    while ((3 + (3 * idx)) < matcher.groupCount()) {

      games.add(new GameData(
        Integer.parseInt(matcher.group(2 + (3 * idx))),
        Integer.parseInt(matcher.group(3 + (3 * idx)))
      ));

      idx++;
    }
    
    return games;
  }

}
