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
import kickr.core.api.PlayerResource;
import kickr.core.api.TableResource;
import kickr.core.api.administration.DemoDataResource;
import kickr.db.FoosballTableDAO;
import kickr.db.GameDAO;
import kickr.db.MatchDAO;
import kickr.db.PlayerDAO;
import kickr.db.entity.FoosballTable;
import kickr.db.entity.Game;
import kickr.db.entity.Match;
import kickr.db.entity.Player;
import kickr.service.MatchService;

import org.eclipse.jetty.servlet.FilterHolder;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;


public class KickrApplication extends Application<KickrConfiguration> {

  public static void main(String[] args) throws Exception {
    new KickrApplication().run(args);
  }

  private final HibernateBundle<KickrConfiguration> hibernateBundle = 
    new HibernateBundle<KickrConfiguration>(
            FoosballTable.class, Game.class, Match.class, Player.class) {
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
    
    PlayerDAO playerDao = new PlayerDAO(sessionFactory);
    MatchDAO matchDao = new MatchDAO(sessionFactory);
    GameDAO gameDao = new GameDAO(sessionFactory);
    FoosballTableDAO tableDao = new FoosballTableDAO(sessionFactory);
    
    MatchService matchService = new MatchService(matchDao, gameDao, playerDao, tableDao);
    
    environment.jersey().register(new MatchResource(matchService, matchDao));
    environment.jersey().register(new PlayerResource(playerDao));
    environment.jersey().register(new TableResource(tableDao));
    environment.jersey().register(new DemoDataResource(tableDao, playerDao));

    FilterHolder corsFilter = environment.getApplicationContext()
        .addFilter("org.eclipse.jetty.servlets.CrossOriginFilter", "/", EnumSet.of(DispatcherType.REQUEST));
    
    corsFilter.setInitParameter("allowedOrigins", "*");
    corsFilter.setInitParameter("allowedHeaders", "X-Requested-With,Content-Type,Accept,Origin");
  }
  
}
