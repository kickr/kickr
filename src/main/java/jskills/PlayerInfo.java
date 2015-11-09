package jskills;

import jskills.util.Guard;

/**
 * A {@link Player} implementation.
 *
 * @param <T> id parameter type
 */
public class PlayerInfo<T> implements Player<T>, SupportPartialPlay, SupportPartialUpdate {

    // = 100% play time
    private static final double DefaultPartialPlayPercentage = 1.0;

    // = receive 100% update
    private static final double DefaultPartialUpdatePercentage = 1.0;

    // The identifier for the player, such as a name.
    private final T id;

    /**
     * Indicates the percent of the time the player should be weighted where 0.0
     * indicates the player didn't play and 1.0 indicates the player played 100%
     * of the time.
     */
    private final double partialPlayPercentage;

    /**
     * Indicated how much of a skill update a player should receive where 0.0
     * represents no update and 1.0 represents 100% of the update.
     */
    private final double partialUpdatePercentage;

    /**
     * Constructs a player.
     * 
     * @param id The identifier for the player, such as a name.
     */
    public PlayerInfo(T id) {
        this(id, DefaultPartialPlayPercentage, DefaultPartialUpdatePercentage);
    }

    /**
     * Constructs a player.
     * 
     * @param id The identifier for the player, such as a name.
     * @param partialPlayPercentage The weight percentage to give this player when calculating a new rank.
     */
    public PlayerInfo(T id, double partialPlayPercentage) {
        this(id, partialPlayPercentage, DefaultPartialUpdatePercentage);
    }

    /**
     * Constructs a player.
     * 
     * @param id The identifier for the player, such as a name.
     * @param partialPlayPercentage The weight percentage to give this player when calculating a new rank.
     * @param partialUpdatePercentage Indicates how much of a skill update a player should receive
     *                                where 0 represents no update and 1.0 represents 100% of the update.
     */
    public PlayerInfo(T id, double partialPlayPercentage, double partialUpdatePercentage) {
        // If they don't want to give a player an id, that's ok...
        Guard.argumentInRangeInclusive(partialPlayPercentage, 0, 1.0, "partialPlayPercentage");
        Guard.argumentInRangeInclusive(partialUpdatePercentage, 0, 1.0, "partialUpdatePercentage");
        this.id = id;
        this.partialPlayPercentage = partialPlayPercentage;
        this.partialUpdatePercentage = partialUpdatePercentage;
    }

    @Override
    public T getId() {
        return id;
    }

    @Override
    public double getPartialPlayPercentage() {
        return partialPlayPercentage;
    }

    @Override
    public double getPartialUpdatePercentage() {
        return partialUpdatePercentage;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PlayerInfo<?> player = (PlayerInfo<?>) o;

        if (Double.compare(player.getPartialPlayPercentage(), getPartialPlayPercentage()) != 0) return false;
        if (Double.compare(player.getPartialUpdatePercentage(), getPartialUpdatePercentage()) != 0) return false;
        return !(getId() != null ? !getId().equals(player.getId()) : player.getId() != null);

    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = getId() != null ? getId().hashCode() : 0;
        temp = Double.doubleToLongBits(getPartialPlayPercentage());
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(getPartialUpdatePercentage());
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }

    @Override public String toString() {
        return id != null ? id.toString() : super.toString();
    }
}