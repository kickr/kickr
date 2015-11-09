
package jskills;

import java.util.HashMap;
import java.util.Map;

/**
 * An update of player ratings including the probability
 * of the actual rating.
 *
 * @author nikku
 */
public class RatingUpdates extends HashMap<Player, Rating> {

  private double probability = 1.0;

  public RatingUpdates(Map<Player, Rating> updates, double probability) {
    super(updates);

    this.probability = probability;
  }

  public RatingUpdates(Map<Player, Rating> updatedRatings) {
    super(updatedRatings);
  }

  public double getProbability() {
    return probability;
  }

}
