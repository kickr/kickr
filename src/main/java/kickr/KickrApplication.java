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
import kickr.db.dao.ScoreDAO;
import kickr.db.dao.UserDAO;
import kickr.db.entity.FoosballTable;
import kickr.db.entity.Game;
import kickr.db.entity.Match;
import kickr.db.entity.Player;
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
import kickr.web.CharsetResponseFilter;
import kickr.web.ConstraintViolationExceptionHandler;
import kickr.web.NotFoundErrorHandler;
import org.eclipse.jetty.servlet.FilterHolder;
import org.glassfish.jersey.server.filter.RolesAllowedDynamicFeature;
import org.hibernate.SessionFactory;
import support.form.FormDataReaderWriter;
import support.transactional.WithTransaction;
import support.security.SecurityContextFactory;
import support.security.SecurityContextInitializer;
import support.security.auth.AuthFactory;


public class KickrApplication extends Application<KickrConfiguration> {

  private static final int ONE_SECOND = 1000;
  private static final int FIVETEEN_MINUTES = ONE_SECOND * 60 * 15;

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
  private RatingService ratingService;
  private ScheduledExecutorService executorService;


  @Override
  public String getName() {
    return "kickr";
  }

  public static HibernateBundle<KickrConfiguration> createHibernateBundle() {
    return new HibernateBundle<KickrConfiguration>(
            FoosballTable.class,
            Game.class,
            Match.class,
            Player.class,
            Score.class,
            ScoreChange.class,
            User.class,
            AccessToken.class) {

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
    
    this.executorService = environment.lifecycle().scheduledExecutorService("scheduled-pool-%d").build();

    this.elasticSearch = new ElasticSearch(configuration.getElasticConfiguration());

    this.sessionFactory = hibernateBundle.getSessionFactory();

    this.playerDao = new PlayerDAO(sessionFactory);
    this.matchDao = new MatchDAO(sessionFactory);
    this.gameDao = new GameDAO(sessionFactory);
    this.scoreDao = new ScoreDAO(sessionFactory);
    this.tableDao = new FoosballTableDAO(sessionFactory);
    this.accessTokenDao = new AccessTokenDAO(sessionFactory);
    this.userDao = new UserDAO(sessionFactory);

    WithTransaction transactional = new WithTransaction(sessionFactory);

    this.matchService = new MatchService(matchDao, gameDao, playerDao, tableDao);
    this.credentialsService = new CredentialsService();
    this.authenticationService = new AuthenticationService(credentialsService, userDao, accessTokenDao);

    this.ratingService = new RatingService(matchDao, scoreDao, configuration.getRatingConfiguration());


    // schedule update of ratings

    executorService.scheduleWithFixedDelay(() -> {
      transactional.run(() -> {
        ratingService.calculateNewRatings();
      });
    }, ONE_SECOND, FIVETEEN_MINUTES, TimeUnit.MILLISECONDS);


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

    environment.jersey().register(new MatchResource(matchService, matchDao, playerDao, environment.getValidator()));
    environment.jersey().register(new ScoreResource(scoreDao));
    environment.jersey().register(new PlayerResource(playerDao));
    environment.jersey().register(new TableResource(tableDao));
    environment.jersey().register(new AdminResource(ratingService, tableDao, playerDao, sessionFactory));
    
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
