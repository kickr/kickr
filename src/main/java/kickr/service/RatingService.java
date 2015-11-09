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
package kickr.service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;
import kickr.analysis.config.RatingConfiguration;
import kickr.db.dao.RatingDAO;
import kickr.db.dao.ScoreChangeDAO;
import kickr.db.dao.ScoreDAO;
import kickr.db.entity.Match;
import kickr.db.entity.Player;
import kickr.db.entity.Rating;
import kickr.db.entity.Score;
import kickr.analysis.rating.AbstractRatingProvider;
import kickr.analysis.rating.RatingCalculator;
import kickr.analysis.model.RatingResults;
import kickr.analysis.model.ScoreDefinition;
import kickr.analysis.model.ScoringResults;
import kickr.analysis.score.ScoringCalculator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author nikku
 */
public class RatingService extends AbstractRatingProvider {

  private static final Logger LOG = LoggerFactory.getLogger(RatingService.class);

  private final ScoreChangeDAO scoreChangeDao;
  private final ScoreDAO scoreDao;
  private final RatingDAO ratingDao;

  private final RatingCalculator ratingCalculator;
  private final ScoringCalculator scoringCalculator;

  public RatingService(RatingConfiguration ratingConfiguration, RatingDAO ratingDao, ScoreDAO scoreDao, ScoreChangeDAO scoreChangeDao) {
    super(ratingConfiguration);
    
    this.ratingDao = ratingDao;
    this.scoreDao = scoreDao;

    this.scoreChangeDao = scoreChangeDao;

    this.ratingCalculator = new RatingCalculator(this);
    this.scoringCalculator = new ScoringCalculator(ratingConfiguration);
  }

  protected void logChanges(Match match, RatingResults ratingResults, ScoringResults scoringResults) {
    LOG.info("rated Match{id: {}} with \n\tratings={}\n\tscoreChanges={}", match, ratingResults, scoringResults);
  }

  @Override
  protected Rating loadRating(Player player) {
    return ratingDao.getLatest(player);
  }

  public void rateMatches(List<Match> unratedMatches) {
    for (Match match: unratedMatches) {
      this.rateMatch(match);
    }
  }
  
  protected void rateMatch(Match match) {

    ScoreDefinition defaultScores = ScoreDefinition.DEFAULT;

    ScoreDefinition lastWeekScores = ScoreDefinition.LAST_WEEK;

    RatingResults ratingResults = ratingCalculator.calculateNewRatings(match);
    ScoringResults scoringResults = scoringCalculator.calculateScoreChanges(match, ratingResults, defaultScores);

    logChanges(match, ratingResults, scoringResults);

    Date scoringDate = Date.from(
            match.getPlayed().toInstant()
              .plus(ratingConfiguration.getDelay()));

    ratingResults.forEach((p, rating) -> {
      rating.setCreated(scoringDate);
      
      ratingDao.save(rating);
    });

    scoringResults.forEach((player, scoreChange) -> {

      Stream.of(defaultScores, lastWeekScores).forEach(scoreDefinition -> {

        int scoreUpdate = scoreChange.getValue();

        Score currentScore = scoreDao.getLatestByPlayer(scoreDefinition, player);
        Score newScore;

        if (currentScore != null) {
          newScore = currentScore.addValue(scoreUpdate);
        } else {
          newScore = new Score(player, scoreDefinition.getKey(), scoreUpdate);
        }

        newScore.setCreated(scoringDate);

        scoreDao.save(newScore);
      });

      scoreChange.setCreated(scoringDate);

      scoreChangeDao.save(scoreChange);
    });
    
    match.setRated(true);
  }
  
  public void resetScores() {
    Instant now = Instant.now();
    Instant before = now.minus(7, ChronoUnit.DAYS);
    Instant lastBefore = now.minus(10, ChronoUnit.DAYS);

    ScoreDefinition defaultScores = ScoreDefinition.DEFAULT;
    ScoreDefinition lastWeekScores = ScoreDefinition.LAST_WEEK;

    List<Player> players = scoreChangeDao.findActivePlayers(defaultScores, Date.from(lastBefore));

    Map<Player, Integer> summarizedScores = scoreChangeDao.getScoresByDefinition(defaultScores, Date.from(before), Date.from(now));

    for (Player player: players) {
      Integer sum = summarizedScores.getOrDefault(player, 0);
      
      LOG.info("[LAST WEEK] {}: {}", player.getAlias(), sum);

      scoreDao.save(new Score(player, lastWeekScores.getKey(), 0, sum));
    }

    System.out.println("Summarized Scores: " + summarizedScores);
  }
}