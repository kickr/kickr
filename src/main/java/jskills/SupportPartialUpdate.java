package jskills;

/**
 * Indicates support for allowing partial update (where a player only gets part
 * of the calculated skill update).
 */
public interface SupportPartialUpdate {
    /**
     * Indicated how much of a skill update a player should receive where 0.0
     * represents no update and 1.0 represents 100% of the update.
     */
    public double getPartialUpdatePercentage();
}