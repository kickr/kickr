package kickr.config;

import java.time.Duration;
import javax.validation.constraints.NotNull;

/**
 *
 * @author nikku
 */
public class RatingConfiguration {
  
  @NotNull
  private int delay = 1000 * 60 * 60 * 2; // 2 hours
  
  public Duration getDelay() {
    return Duration.ofMillis(delay);
  }
}
