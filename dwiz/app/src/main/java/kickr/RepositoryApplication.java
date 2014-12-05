package kickr;

import kickr.core.api.ModelResource;
import kickr.db.ModelDAO;
import kickr.db.entity.Model;
import kickr.service.ModelService;
import io.dropwizard.Application;
import io.dropwizard.assets.AssetsBundle;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.hibernate.HibernateBundle;
import io.dropwizard.migrations.MigrationsBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import java.util.EnumSet;
import javax.servlet.DispatcherType;
import org.eclipse.jetty.servlet.FilterHolder;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;


public class RepositoryApplication extends Application<RepositoryConfiguration> {

  public static void main(String[] args) throws Exception {
    new RepositoryApplication().run(args);
  }

  private final HibernateBundle<RepositoryConfiguration> hibernateBundle = 
    new HibernateBundle<RepositoryConfiguration>(Model.class) {
      @Override
      public DataSourceFactory getDataSourceFactory(RepositoryConfiguration configuration) {
        return configuration.getDataSourceFactory();
      }

      @Override
      protected void configure(Configuration configuration) {
        configuration.setProperty("hibernate.hbm2ddl.auto", "update");
      }
    };

  private final MigrationsBundle<RepositoryConfiguration> migrationsBundle =
    new MigrationsBundle<RepositoryConfiguration>() {
      @Override
      public DataSourceFactory getDataSourceFactory(RepositoryConfiguration configuration) {
        return configuration.getDataSourceFactory();
      }
    };
          
  @Override
  public String getName() {
    return "repository";
  }

  @Override
  public void initialize(Bootstrap<RepositoryConfiguration> bootstrap) {
    bootstrap.addBundle(new AssetsBundle("/web", "/", "index.html"));
    
    bootstrap.addBundle(migrationsBundle);
    bootstrap.addBundle(hibernateBundle);
  }

  @Override
  public void run(RepositoryConfiguration configuration, Environment environment) throws Exception {
    
    SessionFactory sessionFactory = hibernateBundle.getSessionFactory();
    
    final ModelDAO modelDao = new ModelDAO(sessionFactory);
    final ModelService modelService = new ModelService();
    
    environment.jersey().register(new ModelResource(modelService, modelDao));

    FilterHolder corsFilter = environment.getApplicationContext()
        .addFilter("org.eclipse.jetty.servlets.CrossOriginFilter", "/", EnumSet.of(DispatcherType.REQUEST));
    
    corsFilter.setInitParameter("allowedOrigins", "*");
    corsFilter.setInitParameter("allowedHeaders", "X-Requested-With,Content-Type,Accept,Origin");
  }
}
