package jskills.trueskill;

import java.util.Collection;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import jskills.GameInfo;
import jskills.util.Guard;
import jskills.PairwiseComparison;
import static jskills.PairwiseComparison.DRAW;
import static jskills.PairwiseComparison.LOSE;
import static jskills.PairwiseComparison.WIN;
import jskills.Player;
import jskills.util.RankSorter;
import jskills.Rating;
import jskills.SkillCalculator;
import jskills.SkillCalculator.SupportedOptions;
import jskills.Team;
import jskills.util.Range;
import static jskills.util.MathUtils.square;

/**
 * Calculates new ratings for only two teams where each team has 1 or more players.
 * <remarks>
 * When you only have two teams, the math is still simple: no factor graphs are used yet.
 * </remarks>
 */
public class TwoTeamTrueSkillCalculator extends SkillCalculator {

    public TwoTeamTrueSkillCalculator() {
        super(EnumSet.noneOf(SupportedOptions.class), Range.<Team>exactly(2), Range.<Player>atLeast(1));
    }

    @Override
    public Map<Player, Rating> calculateNewRatings(GameInfo gameInfo, Collection<Team> teams, int... teamRanks) {

        Guard.argumentNotNull(gameInfo, "gameInfo");
        validateTeamCountAndPlayersCountPerTeam(teams);

        List<Team> teamsl = RankSorter.sort(teams, teamRanks);

        Team team1 = teamsl.get(0);
        Team team2 = teamsl.get(1);

        boolean wasDraw = (teamRanks[0] == teamRanks[1]);

        HashMap<Player, Rating> results = new HashMap<Player, Rating>();

        updatePlayerRatings(gameInfo,
                results,
                team1,
                team2,
                wasDraw ? PairwiseComparison.DRAW : PairwiseComparison.WIN);

        updatePlayerRatings(gameInfo,
                results,
                team2,
                team1,
                wasDraw ? PairwiseComparison.DRAW : PairwiseComparison.LOSE);

        return results;
    }

    private static void updatePlayerRatings(GameInfo gameInfo,
                                            Map<Player, Rating> newPlayerRatings,
                                            Team selfTeam,
                                            Team otherTeam,
                                            PairwiseComparison selfToOtherTeamComparison) {
        double drawMargin = DrawMargin.GetDrawMarginFromDrawProbability(gameInfo.getDrawProbability(), gameInfo.getBeta());
        double betaSquared = square(gameInfo.getBeta());
        double tauSquared = square(gameInfo.getDynamicsFactor());

        int totalPlayers = selfTeam.size() + otherTeam.size();

        double selfMeanSum = 0;
        for (Rating r : selfTeam.getRatings()) selfMeanSum += r.getMean();
        double otherTeamMeanSum = 0;
        for (Rating r : otherTeam.getRatings()) otherTeamMeanSum += r.getMean();

        double sum = 0;
        for (Rating r : selfTeam.getRatings()) sum += square(r.getStandardDeviation());
        for (Rating r : otherTeam.getRatings()) sum += square(r.getStandardDeviation());
        
        double c = Math.sqrt(sum + totalPlayers*betaSquared);

        double winningMean = selfMeanSum;
        double losingMean = otherTeamMeanSum;

        switch (selfToOtherTeamComparison) {
            case WIN: case DRAW: /* NOP */ break;
            case LOSE:
                winningMean = otherTeamMeanSum;
                losingMean = selfMeanSum;
                break;
        }

        double meanDelta = winningMean - losingMean;

        double v;
        double w;
        double rankMultiplier;

        if (selfToOtherTeamComparison != PairwiseComparison.DRAW) {
            // non-draw case
            v = TruncatedGaussianCorrectionFunctions.vExceedsMargin(meanDelta, drawMargin, c);
            w = TruncatedGaussianCorrectionFunctions.wExceedsMargin(meanDelta, drawMargin, c);
            rankMultiplier = selfToOtherTeamComparison.multiplier;
        }
        else {
            // assume draw
            v = TruncatedGaussianCorrectionFunctions.vWithinMargin(meanDelta, drawMargin, c);
            w = TruncatedGaussianCorrectionFunctions.wWithinMargin(meanDelta, drawMargin, c);
            rankMultiplier = 1;
        }

        for (Player teamPlayer : selfTeam.getPlayers()) {
            Rating previousPlayerRating = selfTeam.getRating(teamPlayer);

            double meanMultiplier = (square(previousPlayerRating.getStandardDeviation()) + tauSquared)/c;
            double stdDevMultiplier = (square(previousPlayerRating.getStandardDeviation()) + tauSquared)/square(c);

            double playerMeanDelta = (rankMultiplier*meanMultiplier*v);
            double newMean = previousPlayerRating.getMean() + playerMeanDelta;

            double newStdDev =
                Math.sqrt((square(previousPlayerRating.getStandardDeviation()) + tauSquared)*(1 - w*stdDevMultiplier));

            newPlayerRatings.put(teamPlayer, new Rating(newMean, newStdDev));
        }
    }

    @Override
    public double calculateMatchQuality(GameInfo gameInfo, Collection<Team> teams) {

        Guard.argumentNotNull(gameInfo, "gameInfo");
        validateTeamCountAndPlayersCountPerTeam(teams);

        Iterator<Team> teamsIt = teams.iterator();
        
        // We've verified that there's just two teams
        Collection<Rating> team1 = teamsIt.next().getRatings();
        int team1Count = team1.size();

        Collection<Rating> team2 = teamsIt.next().getRatings();
        int team2Count = team2.size();

        int totalPlayers = team1Count + team2Count;

        double betaSquared = square(gameInfo.getBeta());

        double team1MeanSum = 0;
        for (Rating r : team1) team1MeanSum += r.getMean();
        double team1StdDevSquared = 0;
        for (Rating r : team1) team1StdDevSquared += square(r.getStandardDeviation());

        double team2MeanSum = 0;
        for (Rating r : team2) team2MeanSum += r.getMean();
        double team2SigmaSquared = 0;
        for (Rating r : team2) team2SigmaSquared += square(r.getStandardDeviation());

        // This comes from equation 4.1 in the TrueSkill paper on page 8            
        // The equation was broken up into the part under the square root sign and 
        // the exponential part to make the code easier to read.

        double sqrtPart
            = Math.sqrt(
                (totalPlayers*betaSquared)
                /
                (totalPlayers*betaSquared + team1StdDevSquared + team2SigmaSquared)
                );

        double expPart
            = Math.exp(
                (-1*square(team1MeanSum - team2MeanSum))
                /
                (2*(totalPlayers*betaSquared + team1StdDevSquared + team2SigmaSquared))
                );

        return expPart*sqrtPart;
    }
}