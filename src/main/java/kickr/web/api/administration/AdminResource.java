package kickr.web.api.administration;

import io.dropwizard.hibernate.UnitOfWork;
import javax.annotation.security.RolesAllowed;

import javax.ws.rs.POST;
import javax.ws.rs.Path;

import kickr.db.dao.FoosballTableDAO;
import kickr.db.dao.PlayerDAO;
import kickr.db.entity.FoosballTable;
import kickr.db.entity.Player;
import kickr.db.entity.user.User;
import kickr.service.RatingService;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import support.security.annotation.Auth;

@Path("admin")
@RolesAllowed("admin")
public class AdminResource {

  protected FoosballTableDAO tableDao;
  protected PlayerDAO playerDao;
  
  private final RatingService ratingService;
  
  private final SessionFactory sessionFactory;
  
  public AdminResource(RatingService ratingService, FoosballTableDAO tableDao, PlayerDAO playerDao, SessionFactory sessionFactory) {
    this.ratingService = ratingService;
    
    this.tableDao = tableDao;
    this.playerDao = playerDao;
    
    this.sessionFactory = sessionFactory;
  }
  
  @POST
  @Path("demo")
  @UnitOfWork
  public void createDemoData(@Auth User user) {

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
  
  @POST
  @Path("scoreboard/reset")
  @UnitOfWork
  public void resetScoreBoard() {
    Session session = sessionFactory.getCurrentSession();
    
    session.createQuery("UPDATE Score s SET s.value = 0").executeUpdate();
    session.createQuery("DELETE FROM ScoreChange c").executeUpdate();
    session.createQuery("Update Match m SET m.rated = false").executeUpdate();
  }
}
