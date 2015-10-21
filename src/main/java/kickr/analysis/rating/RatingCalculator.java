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
package kickr.analysis.rating;

import kickr.analysis.model.RatingResults;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import jskills.GameInfo;
import jskills.PlayerInfo;
import jskills.TeamInfo;
import jskills.TrueSkillCalculator;
import kickr.db.entity.Match;
import kickr.db.entity.MatchResult;
import kickr.db.entity.Player;
import kickr.db.entity.Rating;
import kickr.db.entity.Team;

/**
 * A calculator for
 * @author nikku
 */
public class RatingCalculator {

  private final RatingProvider ratingProvider;

  public RatingCalculator(RatingProvider ratingProvider) {
    this.ratingProvider = ratingProvider;
  }

  public RatingResults calculateNewRatings(Match match) {

    GameInfo gameInfo = GameInfo.getDefaultGameInfo();

    jskills.Team jTeam1 = createJTeam(match.getTeam1());
    jskills.Team jTeam2 = createJTeam(match.getTeam2());

    List<jskills.Team> jTeams = Arrays.asList(jTeam1, jTeam2);

    MatchResult result = match.getResult();

    jskills.RatingUpdates ratingUpdates = TrueSkillCalculator.calculateNewRatings(
      gameInfo,
      jTeams,
      result.getTotalGames() - result.getTeam1Wins(),
      result.getTotalGames() - result.getTeam2Wins()
    );

    return createRatingChanges(ratingUpdates);
  }


  ////////// static helpers /////////////////////////////////////////////

  private jskills.Team createJTeam(Team team) {

    Player offense = team.getOffense();
    Player defense = team.getDefense();

    PlayerInfo<Player> jOffense = new PlayerInfo<>(offense);
    PlayerInfo<Player> jDefense = new PlayerInfo<>(defense);

    TeamInfo jTeam = new TeamInfo();

    jTeam.addPlayer(jOffense, createJRating(offense));

    if (!offense.equals(defense)) {
      jTeam.addPlayer(jDefense, createJRating(defense));
    }

    return jTeam;
  }

  private jskills.Rating createJRating(Player player) {
    Rating rating = ratingProvider.getRating(player);
    return new jskills.Rating(rating.getMean(), rating.getStandardDeviation());
  }

  private RatingResults createRatingChanges(jskills.RatingUpdates ratingUpdates) {

    Map<Player, Rating> newRatings = new HashMap<>();

    ratingUpdates.forEach((jPlayer, jRating) -> {
      Player player = ((jskills.Player<Player>) jPlayer).getId();

      newRatings.put(player, createRating(player, jRating));
    });

    return new RatingResults(newRatings, ratingUpdates.getProbability());
  }

  private Rating createRating(Player player, jskills.Rating jRating) {
    return new Rating(player, jRating.getMean(), jRating.getStandardDeviation());
  }
}