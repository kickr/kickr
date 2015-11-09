package jskills;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * A {@link Team} implementation.
 */
public class TeamInfo implements Team {

    private final List<Player> players;

    private final Map<Player, Rating> ratingMap;

    public TeamInfo() {
        this.players = new ArrayList<>();
        this.ratingMap = new LinkedHashMap<>();
    }

    /**
     * Adds the player to the team.
     *
     * @param player The player to add.
     * @param rating The rating of the player
     *
     * @return The instance of the team (for chaining convenience).
     */
    public TeamInfo addPlayer(Player player, Rating rating) {
        if (this.ratingMap.containsKey(player)) {
            throw new IllegalArgumentException("Player already added");
        }

        this.players.add(player);
        this.ratingMap.put(player, rating);
        
        return this;
    }

    @Override
    public Player getPlayer(int playerNum) {
        return this.players.get(playerNum);
    }

    @Override
    public List<Player> getPlayers() {
        return Collections.unmodifiableList(this.players);
    }

    @Override
    public int size() {
        return this.players.size();
    }

    @Override
    public Collection<Rating> getRatings() {
        return this.ratingMap.values();
    }

    @Override
    public Rating getRating(Player player) {
        return this.ratingMap.get(player);
    }

    @Override
    public String toString() {

      StringBuilder builder = new StringBuilder();

      builder.append("Team{ players: [ ");

      String playersAsString = getPlayers().stream().map(p -> {
        Rating rating = getRating(p);

        return String.format(
            "Player{ name: %s, stats: (%.3f, %.3f, %.3f) }",
            p.getId(),
            rating.getMean(),
            rating.getStandardDeviation(),
            rating.getConservativeRating());
      }).collect(Collectors.joining(", "));

      builder.append(playersAsString);

      builder.append(" ] }");
      
      return builder.toString();
    }

    ////// static helpers /////////////////////////////////////

    /**
     * Concatenates multiple teams into a list of teams.
     *
     * @param teams The teams to concatenate together.
     * @return A sequence of teams.
     */
    public static List<Team> concat(Team... teams) {
        return new ArrayList<>(Arrays.asList(teams));
    }
}