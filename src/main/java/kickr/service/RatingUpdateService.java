package kickr.service;

import java.time.Duration;
import java.util.List;
import kickr.analysis.config.RatingConfiguration;
import kickr.db.entity.Match;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author nikku
 */
public class RatingUpdateService {
  
  private static final Logger LOG = LoggerFactory.getLogger(RatingUpdateService.class);

  
  private final MatchService matchService;

  private final RatingService ratingService;

  private final Duration ratingDelay;


  public RatingUpdateService(MatchService matchService, RatingService ratingService, RatingConfiguration configuration) {
    this.matchService = matchService;
    this.ratingService = ratingService;

    this.ratingDelay = configuration.getDelay();
  }

  /**
   * Calculate new ratings/scores for all unrated games
   */
  public void updateRatings() {
    trace("[update ratings]", () -> {
      List<Match> unratedMatches = matchService.getUnratedMatches(ratingDelay, 50);

      LOG.info("[update ratings]: found {} unrated matches", unratedMatches.size());

      ratingService.rateMatches(unratedMatches);
    });
  }


  /**
   * Update scores per player
   */
  public void updateScores() {
    trace("[update scores]", () -> {
      ratingService.resetScores();
    });
  }

  protected void trace(String topic, Runnable runnable) {
    LOG.info(topic + " START");

    long startMs = System.currentTimeMillis();

    try {
      runnable.run();
    } catch (RuntimeException e) {
      LOG.error(topic + " ERROR", e);
    }

    LOG.info("{} FINISHED in {}ms", topic, System.currentTimeMillis() - startMs);
  }
}
