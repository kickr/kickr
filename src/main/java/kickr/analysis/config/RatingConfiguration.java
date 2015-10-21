package kickr.analysis.config;

import java.time.Duration;
import javax.validation.constraints.NotNull;
import jskills.GameInfo;
import kickr.db.entity.Rating;
import kickr.db.entity.Score;

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

  private final GameInfo DEFAULT_GAME_INFO = GameInfo.getDefaultGameInfo();

  private final double initialMean = DEFAULT_GAME_INFO.getInitialMean();
  private final double initialStandardDeviation = DEFAULT_GAME_INFO.getInitialStandardDeviation();

  public static final int WIN_MULTIPLIER = 16;
  public static final int LOSS_MULTIPLIER = 8;

  public static final int DEFAULT_WIN = 14;

  public static final double MIN_STANDARD_DEVIATION = 2.5d;

  public static final double MV = 0.22;

  public double getInitialMean() {
    return initialMean;
  }

  public double getInitialStandardDeviation() {
    return initialStandardDeviation;
  }

  public int getWinPoints(double w) {
    return (int) ((1 - ((w * (1 - MV)) + MV)) / ((w * (1 - MV)) + MV) * WIN_MULTIPLIER) + DEFAULT_WIN;
  }

  public int getLossPoints(double w) {
    double l = 1.0 - w;
    return (int) ((l * (1 - MV)) / (1 - (l * (1 - MV))) * -LOSS_MULTIPLIER);
  }

  public int getTiePoints() {
    return 4;
  }

  private double getAdjustedDeviation(double standardDeviation) {
    return Math.max(standardDeviation, MIN_STANDARD_DEVIATION);
  }

  public Rating adjustRating(Rating rating) {
    return new Rating(rating.getPlayer(), rating.getMean(), getAdjustedDeviation(rating.getStandardDeviation()));
  }

  public Score adjustScore(Score score) {
    return score;
  }
}
