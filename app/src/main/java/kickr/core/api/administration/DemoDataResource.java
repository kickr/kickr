package kickr.core.api.administration;

import io.dropwizard.hibernate.UnitOfWork;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import kickr.db.FoosballTableDAO;
import kickr.db.PlayerDAO;
import kickr.db.entity.FoosballTable;
import kickr.db.entity.Player;

@Path("admin/demo")
@Produces(MediaType.APPLICATION_JSON)
public class DemoDataResource {

  protected FoosballTableDAO tableDao;
  protected PlayerDAO playerDao;
  
  public DemoDataResource(FoosballTableDAO tableDao, PlayerDAO playerDao) {
    this.tableDao = tableDao;
    this.playerDao = playerDao;
  }
  
  @POST
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
}
