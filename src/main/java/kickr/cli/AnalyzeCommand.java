/*
 * The MIT License
 *
 * Copyright 2015 nikku.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package kickr.cli;

import kickr.analytics.Analytics;
import io.dropwizard.cli.ConfiguredCommand;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.hibernate.HibernateBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import kickr.KickrApplication;
import kickr.config.KickrConfiguration;
import kickr.db.dao.MatchDAO;
import kickr.db.entity.user.User;
import net.sourceforge.argparse4j.inf.Namespace;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import support.transactional.WithTransaction;

/**
 *
 * @author nikku
 */
public class AnalyzeCommand extends ConfiguredCommand<KickrConfiguration> {
  
  private static final Logger LOGGER = LoggerFactory.getLogger(AnalyzeCommand.class);

  public AnalyzeCommand() {
    super("analyze", "Analyze existing kickr players");
  }

  @Override
  protected void run(Bootstrap<KickrConfiguration> bootstrap, Namespace namespace, KickrConfiguration configuration) throws Exception {

    HibernateBundle<KickrConfiguration> hibernateBundle = KickrApplication.createHibernateBundle();

    Environment environment = new Environment(bootstrap.getApplication().getName(),
                                              bootstrap.getObjectMapper(),
                                              bootstrap.getValidatorFactory().getValidator(),
                                              bootstrap.getMetricRegistry(),
                                              bootstrap.getClassLoader());

    configuration.getMetricsFactory().configure(environment.lifecycle(), bootstrap.getMetricRegistry());
    bootstrap.run(configuration, environment);

    hibernateBundle.run(configuration, environment);

    SessionFactory sessionFactory = hibernateBundle.getSessionFactory();

    WithTransaction transactional = new WithTransaction(sessionFactory);

    MatchDAO matchDao = new MatchDAO(sessionFactory);

    transactional.run(() -> {
      new Analytics().run(matchDao);
    });

    LOGGER.info("Analytics done");
  }
}