package kickr.web.api;

import io.dropwizard.hibernate.UnitOfWork;

import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.security.RolesAllowed;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import kickr.web.model.PlayerData;
import kickr.db.PlayerDAO;
import kickr.db.entity.Player;
import kickr.web.model.SearchResultsData;

@Path("player")
@Produces(MediaType.APPLICATION_JSON)
public class PlayerResource {

  protected PlayerDAO playerDao;

  public PlayerResource(PlayerDAO playerDao) {
    this.playerDao = playerDao;
  }

  /**
   * Query conditions are disjunctive
   * 
   * @param query
   * @return
   */
  @GET
  @Path("search")
  @RolesAllowed("user")
  @UnitOfWork
  public SearchResultsData getPlayers(@QueryParam("query") String query) {

    query = query.trim();

    List<Player> players = playerDao.findPlayersMatchingNameOrAlias(query);

    List<SearchResultsData.Result> results =
            players.stream()
                .map(p -> new SearchResultsData.Result(p.getAlias(), p.getName()))
                .collect(Collectors.toList());

    PlayerData playerDefinition = PlayerUtil.parsePlayer(query);

    if (playerDefinition.getEmail() != null) {
      results.add(0, new SearchResultsData.Result(
        playerDefinition.getAlias() + " &lt;" + playerDefinition.getEmail() + "&gt;",
        "(new player)"
      ));
    }

    return new SearchResultsData().withResults(results);
  }
  
  @POST
  @RolesAllowed("user")
  @UnitOfWork
  public void createPlayer(PlayerData player) {
    playerDao.create(player.toPlayer());
  }
}
