package kickr.core.api.administration;

import io.dropwizard.hibernate.UnitOfWork;

import javax.ws.rs.POST;
import javax.ws.rs.Path;

import kickr.db.FoosballTableDAO;
import kickr.db.PlayerDAO;
import kickr.db.entity.FoosballTable;
import kickr.db.entity.Player;
import kickr.service.RatingService;

@Path("admin")
public class AdminResource {

  protected FoosballTableDAO tableDao;
  protected PlayerDAO playerDao;
  
  private final RatingService ratingService;
  
  public AdminResource(RatingService ratingService, FoosballTableDAO tableDao, PlayerDAO playerDao) {
    this.ratingService = ratingService;
    
    this.tableDao = tableDao;
    this.playerDao = playerDao;
  }
  
  @POST
  @Path("demo")
  @UnitOfWork
  public void createDemoData() {
    FoosballTable table = new FoosballTable();
    table.setName("camunda HQ");
    table.setTeam1Alias("Klo");
    table.setTeam2Alias("Kaffee");
    tableDao.createTable(table);
    
    playerDao.createPlayerIfNotExists(new Player("SMI", "Roman Smirnov", "roman@roman"));
    playerDao.createPlayerIfNotExists(new Player("NRE", "Nico Rehwaldt", "nico@nico"));
    playerDao.createPlayerIfNotExists(new Player("CLI", "Christian Lipphardt", "cli@cli"));
    playerDao.createPlayerIfNotExists(new Player("SÖX", "Michael Schöttes", "micha@micha"));
    playerDao.createPlayerIfNotExists(new Player("THL", "Thorben Lindhauer", "thorben@thorben"));
  }
  
  @POST
  @Path("scoreboard/update")
  @UnitOfWork
  public void updateScoreBoard() {
    ratingService.calculateNewRatings();
  }
}
