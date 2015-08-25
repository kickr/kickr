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
package kickr.core.web.api;

import io.dropwizard.client.JerseyClientBuilder;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.testing.ResourceHelpers;
import io.dropwizard.testing.junit.DropwizardAppRule;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.Response;
import kickr.KickrApplication;
import kickr.config.KickrConfiguration;
import static org.assertj.core.api.Assertions.assertThat;
import org.hibernate.cfg.AvailableSettings;
import org.hibernate.cfg.Configuration;
import org.hibernate.dialect.H2Dialect;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import support.DatabaseFixtures;

/**
 *
 * @author nikku
 */
public class MatchResourceTest {

  @ClassRule
  public static final DropwizardAppRule<KickrConfiguration> RULE =
          new DropwizardAppRule<>(KickrApplication.class, ResourceHelpers.resourceFilePath("fixtures/integration/test.yml"));

  @Before
  public void before() {
    final DataSourceFactory dataSourceFactory = RULE.getConfiguration().getDataSourceFactory();

    Configuration configuration = new Configuration();
    configuration.setProperty(AvailableSettings.URL, dataSourceFactory.getUrl());
    configuration.setProperty(AvailableSettings.USER, dataSourceFactory.getUser());
    configuration.setProperty(AvailableSettings.PASS, dataSourceFactory.getPassword());
    configuration.setProperty(AvailableSettings.DRIVER, dataSourceFactory.getDriverClass());
    configuration.setProperty(AvailableSettings.DIALECT, "org.hibernate.dialect.H2Dialect");
    
    configuration.setProperty("hibernate.cache.use_second_level_cache", "false");

    DatabaseFixtures.setup("fixtures/sql/test.sql", configuration);
  }

  @Test
  public void loginHandlerRedirectsAfterPost() {
    Client client = new JerseyClientBuilder(RULE.getEnvironment()).build("test client");

    Response response = client.target(
             String.format("http://localhost:%d/login", RULE.getLocalPort()))
            .request()
            .post(Entity.form(new Form("FOO", "BAR")));

    assertThat(response.getStatus()).isEqualTo(302);
  }
}
