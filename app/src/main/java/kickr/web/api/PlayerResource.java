package kickr.web.api;

import io.dropwizard.hibernate.UnitOfWork;

import java.util.List;
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
   * @param namePart
   * @return
   */
  @GET
  @RolesAllowed("user")
  @UnitOfWork
  public List<PlayerData> getPlayers(@QueryParam("namePart") String namePart) {
    List<Player> players = playerDao.findPlayersMatchingNameOrAlias(namePart);
    
    return PlayerData.fromPlayers(players);
  }
  
  @POST
  @RolesAllowed("user")
  @UnitOfWork
  public void createPlayer(PlayerData player) {
    playerDao.create(player.toPlayer());
  }
}
