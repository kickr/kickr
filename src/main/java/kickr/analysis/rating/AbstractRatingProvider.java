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

import kickr.analysis.config.RatingConfiguration;
import kickr.db.entity.Player;
import kickr.db.entity.Rating;

/**
 *
 * @author nikku
 */
public abstract class AbstractRatingProvider implements RatingProvider {

  protected final RatingConfiguration ratingConfiguration;

  public AbstractRatingProvider(RatingConfiguration ratingConfiguration) {
    this.ratingConfiguration = ratingConfiguration;
  }

  protected abstract Rating loadRating(Player player);
  
  @Override
  public final Rating getRating(Player player) {
    Rating rating = this.loadRating(player);

    if (rating == null) {
      rating = this.createDefaultRating(player);
    }
    
    return ratingConfiguration.adjustRating(rating);
  }

  /**
   * Create a default rating for the given player
   *
   * @param player
   * @return
   */
  protected Rating createDefaultRating(Player player) {
    return new Rating(player,
                  ratingConfiguration.getInitialMean(),
                  ratingConfiguration.getInitialStandardDeviation());
  }

}
