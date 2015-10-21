package kickr;

import kickr.db.dao.AccessTokenDAO;
import kickr.config.KickrConfiguration;
import io.dropwizard.Application;
import io.dropwizard.assets.AssetsBundle;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.flyway.FlywayBundle;
import io.dropwizard.flyway.FlywayFactory;
import io.dropwizard.forms.MultiPartBundle;
import io.dropwizard.hibernate.HibernateBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.dropwizard.views.ViewBundle;
import java.util.EnumSet;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.servlet.DispatcherType;
import kickr.analysis.config.RatingConfiguration;
import kickr.cli.AnalyzeCommand;
import kickr.cli.SetupCommand;
import kickr.web.NotAuthorizedErrorHandler;
import kickr.web.api.UserResource;
import kickr.web.api.MatchResource;
import kickr.web.api.PlayerResource;
import kickr.web.api.RootResource;
import kickr.web.api.ScoreResource;
import kickr.web.api.TableResource;
import kickr.web.api.administration.AdminResource;
import kickr.db.dao.FoosballTableDAO;
import kickr.db.dao.GameDAO;
import kickr.db.dao.MatchDAO;
import kickr.db.dao.PlayerDAO;
import kickr.db.dao.RatingDAO;
import kickr.db.dao.ScoreChangeDAO;
import kickr.db.dao.ScoreDAO;
import kickr.db.dao.UserDAO;
import kickr.db.entity.FoosballTable;
import kickr.db.entity.Game;
import kickr.db.entity.Match;
import kickr.db.entity.Player;
import kickr.db.entity.Rating;
import kickr.db.entity.Score;
import kickr.db.entity.ScoreChange;
import kickr.db.entity.user.AccessToken;
import kickr.db.entity.user.User;
import kickr.search.ElasticSearch;
import kickr.search.SearchIndexer;
import kickr.security.UserSecurityContextFactory;
import kickr.security.service.AuthenticationService;
import kickr.security.service.CredentialsService;
import kickr.service.MatchService;
import kickr.service.RatingService;
import kickr.service.RatingUpdateService;
import kickr.web.CharsetResponseFilter;
import kickr.web.ConstraintViolationExceptionHandler;
import kickr.web.NotFoundErrorHandler;
import kickr.web.api.GraphsResource;
import org.eclipse.jetty.servlet.FilterHolder;
import org.glassfish.jersey.server.filter.RolesAllowedDynamicFeature;
import org.hibernate.SessionFactory;
import support.form.FormDataReaderWriter;
import support.transactional.WithTransaction;
import support.security.SecurityContextFactory;
import support.security.SecurityContextInitializer;
import support.security.auth.AuthFactory;


public class KickrApplication extends Application<KickrConfiguration> {

  private static final int FIVE_SECOND = 1000 * 5;
  private static final int TEN_SECONDS = FIVE_SECOND * 2;

  private static final int FIVETEEN_MINUTES = TEN_SECONDS * 6 * 15;
  private static final int ONE_HOUR = FIVETEEN_MINUTES * 4;

  // static

  public static void main(String[] args) throws Exception {
    new KickrApplication().run(args);
  }


  // instance

  private HibernateBundle<KickrConfiguration> hibernateBundle;
  private ElasticSearch elasticSearch;
  private SessionFactory sessionFactory;
  private PlayerDAO playerDao;
  private MatchDAO matchDao;
  private GameDAO gameDao;
  private ScoreDAO scoreDao;
  private FoosballTableDAO tableDao;
  private AccessTokenDAO accessTokenDao;
  private UserDAO userDao;
  private MatchService matchService;
  private CredentialsService credentialsService;
  private AuthenticationService authenticationService;
  private RatingUpdateService ratingUpdateService;
  private ScheduledExecutorService executorService;
  private RatingDAO ratingDao;
  private RatingService ratingService;
  private ScoreChangeDAO scoreChangeDao;


  @Override
  public String getName() {
    return "kickr";
  }

  public static HibernateBundle<KickrConfiguration> createHibernateBundle() {
    return new HibernateBundle<KickrConfiguration>(
            AccessToken.class,
            FoosballTable.class,
            Game.class,
            Match.class,
            Player.class,
            Rating.class,
            Score.class,
            ScoreChange.class,
            User.class) {

      @Override
      public DataSourceFactory getDataSourceFactory(KickrConfiguration configuration) {
        return configuration.getDataSourceFactory();
      }
    };
  }

  @Override
  public void initialize(Bootstrap<KickrConfiguration> bootstrap) {
    hibernateBundle = createHibernateBundle();

    bootstrap.addBundle(new MultiPartBundle());

    bootstrap.addBundle(new ViewBundle<KickrConfiguration>() { });

    bootstrap.addBundle(new AssetsBundle("/assets", "/assets"));

    bootstrap.addBundle(hibernateBundle);

    bootstrap.addBundle(new FlywayBundle<KickrConfiguration>() {

      @Override
      public DataSourceFactory getDataSourceFactory(KickrConfiguration configuration) {
        return configuration.getDataSourceFactory();
      }

      @Override
      public FlywayFactory getFlywayFactory(KickrConfiguration configuration) {
        return configuration.getFlywayFactory();
      }
    });

    bootstrap.addCommand(new SetupCommand());
    bootstrap.addCommand(new AnalyzeCommand());
  }

  @Override
  public void run(KickrConfiguration configuration, Environment environment) throws Exception {

    RatingConfiguration ratingConfiguration = configuration.getRatingConfiguration();

    this.executorService = environment.lifecycle().scheduledExecutorService("scheduled-pool-%d").build();

    this.elasticSearch = new ElasticSearch(configuration.getElasticConfiguration());

    this.sessionFactory = hibernateBundle.getSessionFactory();

    this.accessTokenDao = new AccessTokenDAO(sessionFactory);
    this.matchDao = new MatchDAO(sessionFactory);
    this.gameDao = new GameDAO(sessionFactory);
    this.playerDao = new PlayerDAO(sessionFactory);
    this.ratingDao = new RatingDAO(sessionFactory);
    this.scoreDao = new ScoreDAO(sessionFactory);
    this.scoreChangeDao = new ScoreChangeDAO(sessionFactory);
    this.tableDao = new FoosballTableDAO(sessionFactory);
    this.userDao = new UserDAO(sessionFactory);

    WithTransaction transactional = new WithTransaction(sessionFactory);

    this.ratingService = new RatingService(ratingConfiguration, ratingDao, scoreDao, scoreChangeDao);
    this.matchService = new MatchService(matchDao, gameDao, playerDao, tableDao);
    this.credentialsService = new CredentialsService();
    this.authenticationService = new AuthenticationService(credentialsService, userDao, accessTokenDao);
    this.ratingUpdateService = new RatingUpdateService(matchService, ratingService, ratingConfiguration);

    // schedule update of ratings

    executorService.scheduleWithFixedDelay(() -> {
      transactional.run(() -> {
        ratingUpdateService.updateRatings();
      });
    }, FIVE_SECOND, TEN_SECONDS, TimeUnit.MILLISECONDS);

    executorService.scheduleWithFixedDelay(() -> {
      transactional.run(() -> {
        ratingUpdateService.updateScores();
      });
    }, TEN_SECONDS, ONE_HOUR, TimeUnit.MILLISECONDS);

    // user management

    SecurityContextFactory<User> securityContextFactory = new UserSecurityContextFactory("kickr", transactional, authenticationService);

    environment.jersey().register(new SecurityContextInitializer(securityContextFactory));
    environment.jersey().register(new RolesAllowedDynamicFeature());

    environment.jersey().register(AuthFactory.binder(new AuthFactory<>(User.class)));


    // resources

    environment.jersey().register(new NotAuthorizedErrorHandler());
    environment.jersey().register(new NotFoundErrorHandler());
    environment.jersey().register(new ConstraintViolationExceptionHandler());
    
    environment.jersey().register(new CharsetResponseFilter());

    environment.jersey().register(new RootResource(authenticationService));

    environment.jersey().register(new GraphsResource(scoreDao));

    environment.jersey().register(new MatchResource(matchService, matchDao, playerDao, environment.getValidator()));
    environment.jersey().register(new ScoreResource(scoreDao));
    environment.jersey().register(new PlayerResource(playerDao));
    environment.jersey().register(new TableResource(tableDao));
    environment.jersey().register(new AdminResource(ratingUpdateService, tableDao, playerDao, sessionFactory));
    
    environment.jersey().register(new FormDataReaderWriter(environment.getValidator()));

    environment.jersey().register(new UserResource());

    SearchIndexer searchIndexer = new SearchIndexer(matchDao, elasticSearch, environment.getObjectMapper());

    executorService.schedule(() -> {
      transactional.run(() -> {

        try {
          searchIndexer.index();
        } catch (Exception e) {
          System.err.println("Failed to index");
          e.printStackTrace(System.err);
        }
      });
    }, 10, TimeUnit.SECONDS);
    

    environment.lifecycle().manage(elasticSearch);

    // cross origin requests

    FilterHolder corsFilter = environment.getApplicationContext()
        .addFilter("org.eclipse.jetty.servlets.CrossOriginFilter", "/", EnumSet.of(DispatcherType.REQUEST));

    corsFilter.setInitParameter("allowedOrigins", "*");
    corsFilter.setInitParameter("allowedHeaders", "X-Requested-With,Content-Type,Accept,Origin");
  }
}
