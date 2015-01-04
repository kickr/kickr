package kickr.cli;

import io.dropwizard.cli.ConfiguredCommand;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.hibernate.HibernateBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import kickr.config.KickrConfiguration;
import kickr.db.UserDAO;
import kickr.db.entity.user.Role;
import kickr.db.entity.user.User;
import kickr.security.service.CredentialsService;
import net.sourceforge.argparse4j.inf.Namespace;
import net.sourceforge.argparse4j.inf.Subparser;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import support.transactional.WithTransaction;

/**
 *
 * @author nikku
 */
public class SetupCommand extends ConfiguredCommand<KickrConfiguration> {
  
  private static final Logger LOGGER = LoggerFactory.getLogger(SetupCommand.class);

  public SetupCommand() {
    super("setup", "Sets up the application");
  }

  @Override
  public void configure(Subparser subparser) {
    super.configure(subparser);

    subparser.addArgument("username").help("Name of the admin user").required(true);
    subparser.addArgument("email").help("Name of the admin user").required(true);
    subparser.addArgument("password").help("Password for the admin user").required(true);
  }

  @Override
  protected void run(Bootstrap<KickrConfiguration> bootstrap, Namespace namespace, KickrConfiguration configuration) throws Exception {

    HibernateBundle<KickrConfiguration> hibernateBundle = new HibernateBundle<KickrConfiguration>(User.class) {
      @Override
      public DataSourceFactory getDataSourceFactory(KickrConfiguration configuration) {
        return configuration.getDataSourceFactory();
      }
    };

    final Environment environment = new Environment(bootstrap.getApplication().getName(),
                                                    bootstrap.getObjectMapper(),
                                                    bootstrap.getValidatorFactory().getValidator(),
                                                    bootstrap.getMetricRegistry(),
                                                    bootstrap.getClassLoader());
    configuration.getMetricsFactory().configure(environment.lifecycle(), bootstrap.getMetricRegistry());
    bootstrap.run(configuration, environment);

    hibernateBundle.run(configuration, environment);

    SessionFactory sessionFactory = hibernateBundle.getSessionFactory();

    WithTransaction transactional = new WithTransaction(sessionFactory);

    UserDAO userDao = new UserDAO(sessionFactory);
    CredentialsService credentialsService = new CredentialsService();

    User user = new User(namespace.getString("username"), namespace.getString("email"));

    user.setPassword(credentialsService.encryptPassword(namespace.getString("password")));
    user.setPermissions(Role.toPermissions(Role.ADMIN, Role.USER));

    transactional.run(() -> {
      userDao.createUser(user);
    });

    LOGGER.info("Created user {}", user.getName());
  }
}
