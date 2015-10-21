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
package kickr.analysis.model;

import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import kickr.db.entity.Player;
import kickr.db.entity.Rating;

/**
 *
 * @author nikku
 */
public class RatingResults {

  private final Map<Player, Rating> newRatings;
  private final double probability;

  public RatingResults(Map<Player, Rating> newRatings, double probability) {
    this.newRatings = newRatings;
    this.probability = probability;
  }

  public Rating getNewRating(Player player) {
    return newRatings.get(player);
  }

  public Set<Player> getPlayers() {
    return newRatings.keySet();
  }

  public void forEach(BiConsumer<? super Player, ? super Rating> bc) {
    newRatings.forEach(bc);
  }

  public Map<Player, Rating> getNewRatings() {
    return newRatings;
  }

  public double getProbability() {
    return probability;
  }

  @Override
  public String toString() {
    return String.format("RatingResults{ probability: %.3f, newRatings: %s }", probability, newRatings);
  }
}
