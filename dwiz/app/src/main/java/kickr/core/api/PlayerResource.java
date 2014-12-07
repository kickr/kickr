package kickr.core.api;

import io.dropwizard.hibernate.UnitOfWork;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
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
  
  @GET
  @UnitOfWork
  public List<PlayerData> getPlayers(@QueryParam("namePart") String namePart) {
    List<Player> players = playerDao.findPlayersMatchingName(namePart);
    
    return PlayerData.fromPlayers(players);
  }
}