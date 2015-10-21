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
package kickr.analysis.test;

import kickr.analysis.model.RatingResults;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import kickr.analysis.config.RatingConfiguration;
import kickr.analysis.model.ScoreDefinition;
import kickr.analysis.model.ScoringResults;
import kickr.db.entity.Match;
import kickr.db.entity.Player;
import kickr.db.entity.Rating;
import kickr.analysis.rating.AbstractRatingProvider;
import kickr.analysis.rating.RatingCalculator;
import kickr.analysis.score.ScoringCalculator;
import kickr.db.entity.ScoreChange;

/**
 *
 * @author nikku
 */
public class OfflineAnalytics extends AbstractRatingProvider {

  private int round;

  private Map<Player, List<RoundResult>> allResults;

  public OfflineAnalytics(RatingConfiguration ratingConfiguration) {
    super(ratingConfiguration);
    
    round = 0;
    allResults = new HashMap<>();
  }

  protected List<RoundResult> getResults(Player player) {
    List<RoundResult> results = allResults.computeIfAbsent(player, p -> new ArrayList<>());

    int loggedResults = results.size();

    RoundResult lastResult = loggedResults == 0 ? createDefaultResult(player) : results.get(loggedResults - 1);

    if (loggedResults < round) {
      results.addAll(Collections.nCopies(round - loggedResults, lastResult));
    }

    return results;
  }

  protected void addResult(Player player, Rating rating, ScoreChange scoreChange) {
    List<RoundResult> results = getResults(player);

    RoundResult lastResult = getLastResult(player);

    results.add(lastResult.update(rating, scoreChange.getValue()));
  }

  protected void fillBlanks() {
    // ensure we have the latest results for _ALL_ players
    for (Player player: allResults.keySet()) {
      getResults(player);
    }
  }

  protected void roundFinished() {
    round++;
  }

  protected RoundResult getLastResult(Player player) {
    List<RoundResult> results = getResults(player);
    return round > 0 ? results.get(round - 1) : createDefaultResult(player);
  }

  @Override
  protected Rating loadRating(Player player) {
    return getLastResult(player).getRating();
  }
  
  public void printResults() {

    // fill empty slots in table
    fillBlanks();

    ////// RATINGS ////////////////////

    System.out.println("");
    System.out.println("");

    // HEADER
    System.out.print("Name;");
    for (int i = 0; i < round; i++) {
      System.out.print(i + ";");
    }
    System.out.println("");

    // COLUMNS
    allResults.forEach((player, roundResults) -> {
      System.out.printf("%3s;", player.getAlias());

      roundResults.forEach(roundResult -> {
        System.out.printf("%.3f;", roundResult.getRating().getConservativeRating());
      });

      System.out.println();
    });

    ////// SCORES ////////////////////

    System.out.println("");
    System.out.println("");

    // HEADER
    System.out.print("Name;");
    for (int i = 0; i < round; i++) {
      System.out.print(i + ";");
    }
    System.out.println("");

    // COLUMNS
    allResults.forEach((player, roundResults) -> {
      System.out.printf("%3s;", player.getAlias());

       roundResults.forEach(roundResult -> {
         System.out.printf("%d;", roundResult.getScore());
       });

      System.out.println();
    });


    // HEADER

    System.out.println("");
    System.out.println("");

    System.out.print("Name;");
    for (int i = 0; i < round; i++) {
      System.out.print(i + ";");
    }
    System.out.println("");

    // COLUMNS
    allResults.forEach((player, roundResults) -> {
      System.out.printf("%3s;", player.getAlias());

      RoundResult lastResult = roundResults.get(roundResults.size() - 1);
      System.out.printf("%d;", lastResult.gamesPlayed);

      System.out.println();
    });
  }

  protected RoundResult createDefaultResult(Player player) {
    return new RoundResult(createDefaultRating(player), 0, 0);
  }

  public void rateMatch(Match match) {
    RatingCalculator ratingCalculator = new RatingCalculator(this);
    ScoringCalculator scoringCalculator = new ScoringCalculator(ratingConfiguration);

    RatingResults ratingResults = ratingCalculator.calculateNewRatings(match);
    ScoringResults scoringResults = scoringCalculator.calculateScoreChanges(match, ratingResults, ScoreDefinition.DEFAULT);

    for (Player player: ratingResults.getPlayers()) {
      this.addResult(player, ratingResults.getNewRating(player), scoringResults.getScoreChange(player));
    }

    roundFinished();
  }

  /**
   * Update container
   */
  protected static class RoundResult {

    private Rating rating;
    private int score;
    int gamesPlayed;

    public RoundResult(Rating rating, int score, int gamesPlayed) {
      this.rating = rating;
      this.score = score;
      this.gamesPlayed = gamesPlayed;
    }

    public Rating getRating() {
      return rating;
    }

    public int getScore() {
      return score;
    }

    public RoundResult update(Rating rating, int scoreUpdate) {
      return new RoundResult(rating, score + scoreUpdate, this.gamesPlayed + 1);
    }
  }
}