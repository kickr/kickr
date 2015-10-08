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
package kickr.analytics;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import jskills.GameInfo;
import jskills.Player;
import jskills.PlayerInfo;
import jskills.Rating;
import jskills.RatingUpdates;
import jskills.Team;
import jskills.TeamInfo;
import jskills.TrueSkillCalculator;
import kickr.db.dao.MatchDAO;
import kickr.db.entity.Match;
import kickr.db.entity.MatchResult;

/**
 *
 * @author nikku
 */
public class Analytics {

  private static GameInfo DEFAULT_GAME_INFO = GameInfo.getDefaultGameInfo();

  private static Rating NULL_RATING = DEFAULT_GAME_INFO.getDefaultRating();

  private static RoundResult NULL_RESULT = new RoundResult(NULL_RATING, 0, 0);

  
  private static class RoundResult {

    private Rating rating;
    private int score;
    private int gamesPlayed;

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


  private static class Ratings {

    private int round;

    private Map<Player, List<RoundResult>> allResults;

    public Ratings() {
      round = 0;
      allResults = new HashMap<>();
    }

    protected List<RoundResult> getResults(Player player) {
      List<RoundResult> results = allResults.computeIfAbsent(player, p -> new ArrayList<>());
      
      int loggedResults = results.size();

      RoundResult lastResult = loggedResults == 0 ? NULL_RESULT : results.get(loggedResults - 1);
      
      if (loggedResults < round) {
        results.addAll(Collections.nCopies(round - loggedResults, lastResult));
      }
      
      return results;
    }

    public void addResults(Map<Player, Rating> newRatings, Team team, int scoreUpdate) {
      for (Player player: team.getPlayers()) {
        addResult(player, newRatings.get(player), scoreUpdate);
      }
    }

    private void addResult(Player player, Rating rating, int scoreUpdate) {
      List<RoundResult> results = getResults(player);

      RoundResult lastResult = getLastResult(player);

      results.add(lastResult.update(rating, scoreUpdate));

    }


    public void print() {
      
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
        System.out.printf("%3s;", player.getId());

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
        System.out.printf("%3s;", player.getId());

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
        System.out.printf("%3s;", player.getId());

        RoundResult lastResult = roundResults.get(roundResults.size() - 1);
        System.out.printf("%d;", lastResult.gamesPlayed);

        System.out.println();
      });
    }

    public Rating getRating(Player player) {
      return getLastResult(player).getRating();
    }

    public void fillBlanks() {
      // ensure we have the latest results for _ALL_ players
      for (Player player: allResults.keySet()) {
        getResults(player);
      }
    }

    public void roundFinished() {
      round++;
    }

    private RoundResult getLastResult(Player player) {
      List<RoundResult> results = getResults(player);
      return round > 0 ? results.get(round - 1) : NULL_RESULT;
    }

    private int getRound() {
      return round;
    }
  }


  private final Ratings ratings;

  public Analytics() {
    this.ratings = new Ratings();
  }

  public Rating getRating(Player player) {
    return ratings.getRating(player);
  }

  public void run(MatchDAO matchDao) {

    List<Match> matches = matchDao.getMatches(null);

    for (Match match: matches) {

      GameInfo gameInfo = GameInfo.getDefaultGameInfo();

      Team team1 = createTeam(match.getTeam1());
      Team team2 = createTeam(match.getTeam2());

      List<Team> teams = Arrays.asList(team1, team2);

      MatchResult result = match.getResult();

      System.out.println();
      System.out.println();
      System.out.println("GAME " + ratings.getRound());
      System.out.println(team1);
      System.out.println("----- VS -----");
      System.out.println(team2);

      System.out.printf("----- %s:%s -----\n", match.getResult().getTeam1Wins(), match.getResult().getTeam2Wins());
      System.out.printf("----- %s:%s -----\n",
        result.getTotalGames() - result.getTeam1Wins(),
        result.getTotalGames() - result.getTeam2Wins());
      
      RatingUpdates newRatings = TrueSkillCalculator.calculateNewRatings(
        gameInfo,
        teams,
        result.getTotalGames() - result.getTeam1Wins(),
        result.getTotalGames() - result.getTeam2Wins()
      );

      /**
        // 48
        int winPoints = 42;
        // 24
        int lossPoints = 18;

        // 0.80
        double probability = newRatings.getProbability();

        // 48 * (1 - 0.80) + 6
        int winBounty = (int) (Math.ceil(winPoints * (1 - probability)) + 6);
        int lossBounty = (int) (-1 * (Math.ceil(lossPoints * (1 - probability))) + 6);
       */

      // 0.80
      double probability = newRatings.getProbability();

      int winBounty = Scores.calculateWinPoints(probability);
      int lossBounty = Scores.calculateLossPoints(probability);

      System.out.printf("----- probability: %.3f, win/loss: %s / %s\n", probability, winBounty, lossBounty);

      ratings.addResults(newRatings, team1, result.getTeam1Wins() > result.getTeam2Wins() ? winBounty : lossBounty);
      ratings.addResults(newRatings, team2, result.getTeam2Wins() > result.getTeam1Wins() ? winBounty : lossBounty);
      ratings.roundFinished();
    }

    ratings.fillBlanks();
    ratings.print();
  }

  private Team createTeam(kickr.db.entity.Team kickrTeam) {

    kickr.db.entity.Player kickrOffense = kickrTeam.getOffense();
    kickr.db.entity.Player kickrDefense = kickrTeam.getDefense();

    Player<String> offense = new PlayerInfo<>(kickrOffense.getAlias());
    Player<String> defense = new PlayerInfo<>(kickrDefense.getAlias());

    TeamInfo team = new TeamInfo();

    team.addPlayer(offense, getRating(offense));

    if (!kickrOffense.equals(kickrDefense)) {
      team.addPlayer(defense, getRating(defense));
    }

    return team;
  }

}