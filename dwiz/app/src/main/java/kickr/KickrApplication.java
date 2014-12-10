package kickr;

import io.dropwizard.Application;
import io.dropwizard.assets.AssetsBundle;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.hibernate.HibernateBundle;
import io.dropwizard.migrations.MigrationsBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

import java.util.EnumSet;

import javax.servlet.DispatcherType;

import kickr.core.api.MatchResource;
import kickr.core.api.ModelResource;
import kickr.core.api.PlayerResource;
import kickr.core.api.TableResource;
import kickr.db.FoosballTableDAO;
import kickr.db.MatchDAO;
import kickr.db.ModelDAO;
import kickr.db.PlayerDAO;
import kickr.db.entity.FoosballTable;
import kickr.db.entity.Game;
import kickr.db.entity.Match;
import kickr.db.entity.Model;
import kickr.db.entity.Player;
import kickr.service.MatchService;
import kickr.service.ModelService;

import org.eclipse.jetty.servlet.FilterHolder;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.context.internal.ManagedSessionContext;


public class KickrApplication extends Application<KickrConfiguration> {

  public static void main(String[] args) throws Exception {
    new KickrApplication().run(args);
  }

  private final HibernateBundle<KickrConfiguration> hibernateBundle = 
    new HibernateBundle<KickrConfiguration>(Model.class, FoosballTable.class, 
        Game.class, Match.class, Player.class) {
      @Override
      public DataSourceFactory getDataSourceFactory(KickrConfiguration configuration) {
        return configuration.getDataSourceFactory();
      }

      @Override
      protected void configure(Configuration configuration) {
        configuration.setProperty("hibernate.hbm2ddl.auto", "update");
      }
    };

  private final MigrationsBundle<KickrConfiguration> migrationsBundle =
    new MigrationsBundle<KickrConfiguration>() {
      @Override
      public DataSourceFactory getDataSourceFactory(KickrConfiguration configuration) {
        return configuration.getDataSourceFactory();
      }
    };
          
  @Override
  public String getName() {
    return "repository";
  }

  @Override
  public void initialize(Bootstrap<KickrConfiguration> bootstrap) {
    bootstrap.addBundle(new AssetsBundle("/web", "/", "index.html"));
    
    bootstrap.addBundle(migrationsBundle);
    bootstrap.addBundle(hibernateBundle);
  }

  @Override
  public void run(KickrConfiguration configuration, Environment environment) throws Exception {
    
    SessionFactory sessionFactory = hibernateBundle.getSessionFactory();
    
    final ModelDAO modelDao = new ModelDAO(sessionFactory);
    final ModelService modelService = new ModelService();
    
    PlayerDAO playerDao = new PlayerDAO(sessionFactory);
    MatchDAO matchDao = new MatchDAO(sessionFactory);
    FoosballTableDAO tableDao = new FoosballTableDAO(sessionFactory);
    
    MatchService matchService = new MatchService(matchDao, playerDao);
    
    initDb(sessionFactory);
    
    environment.jersey().register(new ModelResource(modelService, modelDao));
    environment.jersey().register(new MatchResource(matchService, matchDao));
    environment.jersey().register(new PlayerResource(playerDao));
    environment.jersey().register(new TableResource(tableDao));

    FilterHolder corsFilter = environment.getApplicationContext()
        .addFilter("org.eclipse.jetty.servlets.CrossOriginFilter", "/", EnumSet.of(DispatcherType.REQUEST));
    
    corsFilter.setInitParameter("allowedOrigins", "*");
    corsFilter.setInitParameter("allowedHeaders", "X-Requested-With,Content-Type,Accept,Origin");
  }
  
  protected void initDb(SessionFactory sessionFactory) {
    Session session = sessionFactory.openSession();
    
    ManagedSessionContext.bind(session);

    try {
      FoosballTableDAO tableDao = new FoosballTableDAO(sessionFactory);
      PlayerDAO playerDao = new PlayerDAO(sessionFactory);
      
      FoosballTable table = new FoosballTable();
      table.setName("camunda HQ");
      table.setTeam1Alias("Klo");
      table.setTeam2Alias("Kaffee");
      tableDao.createTable(table);
      
      playerDao.create(new Player("SMI", "Roman Smirnov", "roman@roman"));
      playerDao.create(new Player("NRE", "Nico Rehwaldt", "nico@nico"));
      playerDao.create(new Player("CLI", "Christian Lipphardt", "cli@cli"));
      playerDao.create(new Player("SÖX", "Michael Schöttes", "micha@micha"));
      playerDao.create(new Player("THL", "Thorben Lindhauer", "thorben@thorben"));
      
      session.flush();
      session.close();
    } finally {
      ManagedSessionContext.unbind(sessionFactory);
    }
    
  }
  
}
