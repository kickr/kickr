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
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import kickr.KickrApplication;
import kickr.config.KickrConfiguration;
import org.hibernate.cfg.AvailableSettings;
import org.hibernate.cfg.Configuration;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import support.DatabaseFixtures;
import support.form.FormDataReaderWriter;

/**
 *
 * @author nikku
 */
public class AbstractIntegrationTest {

  @ClassRule
  public static final DropwizardAppRule<KickrConfiguration> RULE =
      new DropwizardAppRule<>(KickrApplication.class, ResourceHelpers.resourceFilePath("fixtures/integration/test.yml"));
  
  private static Configuration databaseConfiguration;

  private Client client = null;

  @BeforeClass
  public static void createDatabaseConfiguration() {
    final DataSourceFactory dataSourceFactory = RULE.getConfiguration().getDataSourceFactory();

    databaseConfiguration = new Configuration();
    databaseConfiguration.setProperty(AvailableSettings.URL, dataSourceFactory.getUrl());
    databaseConfiguration.setProperty(AvailableSettings.USER, dataSourceFactory.getUser());
    databaseConfiguration.setProperty(AvailableSettings.PASS, dataSourceFactory.getPassword());
    databaseConfiguration.setProperty(AvailableSettings.DRIVER, dataSourceFactory.getDriverClass());
    databaseConfiguration.setProperty(AvailableSettings.DIALECT, "org.hibernate.dialect.H2Dialect");

    databaseConfiguration.setProperty("hibernate.cache.use_second_level_cache", "false");
  }

  @Before
  public void before() {
    DatabaseFixtures.setup("fixtures/sql/test.sql", databaseConfiguration);
  }
  
  @After
  public void after() {
    DatabaseFixtures.teardown(databaseConfiguration);

    if (client != null) {
      client.close();
      client = null;
    }
  }

  public KickrApplication getApplication() {
    return RULE.getApplication();
  }

  protected Client getClient() {
    FormDataReaderWriter formDataSupport = new FormDataReaderWriter(RULE.getEnvironment().getValidator());

    if (client == null) {
      client = new JerseyClientBuilder(RULE.getEnvironment())
                      .withProvider(formDataSupport)
                      .build("test client");
    }

    return client;
  }

  protected Entity<?> formEntity(Object form) {
    return Entity.entity(form, MediaType.APPLICATION_FORM_URLENCODED);
  }

  protected Response submit(String uri, Object form) {
    return target(uri).request().post(formEntity(form));
  }
  
  protected WebTarget target(String uri) {

    if (uri.startsWith("/")) {
      uri = String.format("http://localhost:%d%s", RULE.getLocalPort(), uri);
    }

    return getClient().target(uri);
  }
}
