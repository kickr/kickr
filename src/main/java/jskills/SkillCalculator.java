package jskills;

import jskills.util.Guard;
import jskills.util.Range;

import java.util.Collection;
import java.util.EnumSet;
import java.util.Map;

/**
 * Base class for all skill calculator implementations.
 */
public abstract class SkillCalculator {
    
    public enum SupportedOptions { PartialPlay, PartialUpdate }

    private final EnumSet<SupportedOptions> supportedOptions;
    private final Range<Player> playersPerTeamAllowed;
    private final Range<Team> totalTeamsAllowed;
    
    protected SkillCalculator(EnumSet<SupportedOptions> supportedOptions,
                              Range<Team> totalTeamsAllowed, 
                              Range<Player> playerPerTeamAllowed) {
        this.supportedOptions = supportedOptions;
        this.totalTeamsAllowed = totalTeamsAllowed;
        this.playersPerTeamAllowed = playerPerTeamAllowed;
    }

    public boolean isSupported(SupportedOptions option) {
        return supportedOptions.contains(option);
    }
    
    /**
     * Calculates new ratings based on the prior ratings and team ranks.
     * 
     * @param gameInfo Parameters for the game.
     * @param teams A mapping of team players and their ratings.
     * @param teamRanks The ranks of the teams where 1 is first place. For a tie, repeat the number (e.g. 1, 2, 2)
     * @return All the players and their new ratings.
     */
    public abstract Map<Player, Rating> calculateNewRatings(GameInfo gameInfo,
            Collection<Team> teams, int... teamRanks);

    /**
     * Calculates the match quality as the likelihood of all teams drawing.
     * 
     * @param gameInfo Parameters for the game.
     * @param teams A mapping of team players and their ratings.
     * @return The quality of the match between the teams as a percentage (0% = bad, 100% = well matched).
     */
    public abstract double calculateMatchQuality(GameInfo gameInfo, Collection<Team> teams);

    protected void validateTeamCountAndPlayersCountPerTeam(Collection<Team> teams) {
        validateTeamCountAndPlayersCountPerTeam(teams, totalTeamsAllowed, playersPerTeamAllowed);
    }

    private static void validateTeamCountAndPlayersCountPerTeam(Collection<Team> teams,
                                                                Range<Team> totalTeams,
                                                                Range<Player> playersPerTeam) {
        Guard.argumentNotNull(teams, "teams");
        int countOfTeams = 0;
        for (Team currentTeam : teams) {
            if (!playersPerTeam.isInRange(currentTeam.size())) {
                throw new IllegalArgumentException();
            }
            countOfTeams++;
        }

        if (!totalTeams.isInRange(countOfTeams)) {
            throw new IllegalArgumentException();
        }
    }
}