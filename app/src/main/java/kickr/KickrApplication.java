package kickr;

import io.dropwizard.Application;
import io.dropwizard.assets.AssetsBundle;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.hibernate.HibernateBundle;
import io.dropwizard.migrations.MigrationsBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

import java.util.EnumSet;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.servlet.DispatcherType;
import kickr.core.api.MatchResource;
import kickr.core.api.PlayerResource;
import kickr.core.api.ScoreResource;
import kickr.core.api.TableResource;
import kickr.core.api.administration.AdminResource;
import kickr.db.FoosballTableDAO;
import kickr.db.GameDAO;
import kickr.db.MatchDAO;
import kickr.db.PlayerDAO;
import kickr.db.ScoreDAO;
import kickr.db.entity.FoosballTable;
import kickr.db.entity.Game;
import kickr.db.entity.Match;
import kickr.db.entity.Player;
import kickr.db.entity.Score;
import kickr.service.MatchService;
import kickr.service.RatingService;
import org.eclipse.jetty.servlet.FilterHolder;
import org.hibernate.SessionFactory;
import support.WithTransaction;



public class KickrApplication extends Application<KickrConfiguration> {

  private static final int ONE_MINUTE = 1000 * 60 * 15;
  private static final int ONE_SECOND = 1000;

  // static
  
  public static void main(String[] args) throws Exception {
    new KickrApplication().run(args);
  }
  
  
  // instance
  
  private HibernateBundle<KickrConfiguration> hibernateBundle;
  private MigrationsBundle<KickrConfiguration> migrationsBundle;
  
  
  @Override
  public String getName() {
    return "kickr";
  }

  @Override
  public void initialize(Bootstrap<KickrConfiguration> bootstrap) {
    hibernateBundle = new HibernateBundle<KickrConfiguration>(
        FoosballTable.class, Game.class, Match.class, Player.class, Score.class) {

      @Override
      public DataSourceFactory getDataSourceFactory(KickrConfiguration configuration) {
        return configuration.getDataSourceFactory();
      }
    };
    
    migrationsBundle = new MigrationsBundle<KickrConfiguration>() {
      @Override
      public DataSourceFactory getDataSourceFactory(KickrConfiguration configuration) {
        return configuration.getDataSourceFactory();
      }
    };

    
    bootstrap.addBundle(new AssetsBundle("/web", "/", "index.html"));

    bootstrap.addBundle(migrationsBundle);
    bootstrap.addBundle(hibernateBundle);
  }

  @Override
  public void run(KickrConfiguration configuration, Environment environment) throws Exception {
    ScheduledExecutorService scheduledExecutorService = environment.lifecycle().scheduledExecutorService("scheduled-pool-%d").build();
    
    SessionFactory sessionFactory = hibernateBundle.getSessionFactory();
    
    PlayerDAO playerDao = new PlayerDAO(sessionFactory);
    MatchDAO matchDao = new MatchDAO(sessionFactory);
    GameDAO gameDao = new GameDAO(sessionFactory);
    ScoreDAO scoreDao = new ScoreDAO(sessionFactory);
    FoosballTableDAO tableDao = new FoosballTableDAO(sessionFactory);
    
    WithTransaction transactional = new WithTransaction(sessionFactory);
    
    MatchService matchService = new MatchService(matchDao, gameDao, playerDao, tableDao);
    RatingService ratingService = new RatingService(matchDao, scoreDao);

    
    // schedule update of ratings
    
    scheduledExecutorService.scheduleWithFixedDelay(() -> {
      transactional.run(() -> {
        ratingService.calculateNewRatings();
      });
    }, ONE_SECOND, ONE_MINUTE, TimeUnit.MILLISECONDS);
    
    
    // resources
    
    environment.jersey().register(new MatchResource(matchService, matchDao));
    environment.jersey().register(new ScoreResource(scoreDao));
    environment.jersey().register(new PlayerResource(playerDao));
    environment.jersey().register(new TableResource(tableDao));
    environment.jersey().register(new AdminResource(ratingService, tableDao, playerDao));
    
    // servlet filters
    
    FilterHolder corsFilter = environment.getApplicationContext()
        .addFilter("org.eclipse.jetty.servlets.CrossOriginFilter", "/", EnumSet.of(DispatcherType.REQUEST));
    
    corsFilter.setInitParameter("allowedOrigins", "*");
    corsFilter.setInitParameter("allowedHeaders", "X-Requested-With,Content-Type,Accept,Origin");
  }
}
