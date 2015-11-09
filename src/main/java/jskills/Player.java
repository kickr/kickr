package jskills;

/**
 * Represents a player who has a {@link Rating}.
 *
 * @param <T> id parameter type
 */
public interface Player<T> {

    /**
     * Return the players identifier.
     *
     * @return
     */
    public T getId();
}