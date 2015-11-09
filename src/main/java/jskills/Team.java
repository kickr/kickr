package jskills;

import java.util.Collection;
import java.util.List;

/**
 * A team consisting of players with their current ratings.
 */
public interface Team {

    /**
     * Return all players in this list.
     * 
     * @return
     */
    public List<Player> getPlayers();

    /**
     * Return the numbered player in the team.
     *
     * @param playerNum
     * @return
     */
    public Player getPlayer(int playerNum);

    /**
     * Returns the size of the team.
     * 
     * @return
     */
    public int size();

    /**
     * Return the ratings of all team members.
     * 
     * @return 
     */
    public Collection<Rating> getRatings();

    /**
     * Return a players rating.
     * 
     * @param player
     * @return 
     */
    public Rating getRating(Player player);

    /**
     * Return the rating for the given player (position wise).
     * 
     * @param playerNum
     * @return 
     */
    public default Rating getRating(int playerNum) {
      Player player = getPlayer(playerNum);
      return getRating(player);
    }

}