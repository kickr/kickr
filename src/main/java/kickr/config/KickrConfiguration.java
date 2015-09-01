package kickr.config;

import kickr.search.config.ElasticConfiguration;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.Configuration;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.flyway.FlywayFactory;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;


public class KickrConfiguration extends Configuration {

  @Valid
  @NotNull
  @JsonProperty("database")
  private DataSourceFactory database = new DataSourceFactory();

  @Valid
  @NotNull
  @JsonProperty("flyway")
  private FlywayFactory flywayFactory = new FlywayFactory();

  @Valid
  @NotNull
  @JsonProperty("rating")
  private RatingConfiguration ratingConfiguraton = new RatingConfiguration();

  @Valid
  @NotNull
  @JsonProperty("elastic")
  private ElasticConfiguration elasticConfiguration = new ElasticConfiguration();

  public DataSourceFactory getDataSourceFactory() {
    return database;
  }

  public FlywayFactory getFlywayFactory() {
    return flywayFactory;
  }

  public RatingConfiguration getRatingConfiguration() {
    return ratingConfiguraton;
  }

  public ElasticConfiguration getElasticConfiguration() {
    return elasticConfiguration;
  }
}