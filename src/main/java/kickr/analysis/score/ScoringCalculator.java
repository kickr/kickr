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
package kickr.analysis.score;

import java.util.HashMap;
import java.util.Map;
import kickr.analysis.config.RatingConfiguration;
import kickr.analysis.model.RatingResults;
import kickr.analysis.model.ScoringResults;
import kickr.analysis.model.ScoreDefinition;
import kickr.db.entity.Match;
import kickr.db.entity.MatchResult;
import kickr.db.entity.Player;
import kickr.db.entity.Rating;
import kickr.db.entity.ScoreChange;
import kickr.db.entity.Team;

/**
 *
 * @author nikku
 */
public class ScoringCalculator {
  
  private final RatingConfiguration ratingConfiguration;

  public ScoringCalculator(RatingConfiguration ratingConfiguration) {
    this.ratingConfiguration = ratingConfiguration;
  }
  
  public ScoringResults calculateScoreChanges(Match match, RatingResults ratingResults, ScoreDefinition scoreDefinition) {

    MatchResult matchResult = match.getResult();

    Team winnerTeam = null;
    Team loserTeam = null;

    if (matchResult.getTeam1Wins() > matchResult.getTeam2Wins()) {
      winnerTeam = match.getTeam1();
      loserTeam = match.getTeam2();
    } else if (matchResult.getTeam1Wins() < matchResult.getTeam2Wins()) {
      winnerTeam = match.getTeam2();
      loserTeam = match.getTeam1();
    }

    double probability = ratingResults.getProbability();

    int winScore = ratingConfiguration.getWinPoints(probability);
    int lossScore = ratingConfiguration.getLossPoints(probability);
    int tieScore = ratingConfiguration.getTiePoints();

    Map<Player, ScoreChange> scoreChanges = new HashMap<>();

    for (Player player: ratingResults.getPlayers()) {

      Rating newRating = ratingResults.getNewRating(player);
      Team team = match.getTeam(player);

      int scoreUpdate = team.equals(winnerTeam) ? winScore :
                          team.equals(loserTeam) ? lossScore :
                            tieScore;

      ScoreChange scoreChange = new ScoreChange(
          scoreDefinition.getKey(), scoreUpdate, match, player, newRating);

      System.out.println("PLAYER " + player.getAlias() + " ~~> " + scoreChange);

      scoreChanges.put(player, scoreChange);
    }

    return new ScoringResults(scoreChanges);
  }
}
