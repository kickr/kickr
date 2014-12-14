package kickr.core.api;

import io.dropwizard.hibernate.UnitOfWork;

import java.util.List;
import javax.inject.Inject;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import kickr.core.model.PlayerData;
import kickr.db.PlayerDAO;
import kickr.db.entity.Player;

@Path("/player")
@Produces(MediaType.APPLICATION_JSON)
public class PlayerResource {

  protected PlayerDAO playerDao;

  public PlayerResource(PlayerDAO playerDao) {
    this.playerDao = playerDao;
  }
  
  /**
   * Query conditions are disjunctive
   */
  @GET
  @UnitOfWork
  public List<PlayerData> getPlayers(@QueryParam("namePart") String namePart) {
    List<Player> players = playerDao.findPlayersMatchingNameOrAlias(namePart);
    
    return PlayerData.fromPlayers(players);
  }
  
  @GET
  @Path("{alias}")
  @UnitOfWork
  public PlayerData getPlayer(@PathParam("alias") String alias) {
    Player player = playerDao.findPlayerByAlias(alias);
    return PlayerData.fromPlayer(player);
  }
  
  @POST
  @UnitOfWork
  @Consumes(MediaType.APPLICATION_JSON)
  public void createPlayer(PlayerData player) {
    playerDao.create(player.toPlayer());
  }
}
