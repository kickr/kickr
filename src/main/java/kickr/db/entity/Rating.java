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
package kickr.db.entity;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 *
 * @author nikku
 */
@Entity
@Table(name = "kickr_rating")
@NamedQueries({
  @NamedQuery(
    name = "Rating.latestByPlayer",
    query = "SELECT r FROM Rating r WHERE " +
                "r.player = :player AND " +
                "r.created <= :latestDate " +
              "ORDER BY r.created DESC")
})
public class Rating extends BaseEntity {

  @ManyToOne(cascade = CascadeType.REMOVE)
  private Player player;

  private double mean;

  @Column(name = "standard_deviation")
  private double standardDeviation;

  public Rating() {}

  public Rating(Player player, double mean, double standardDeviation) {
    this.player = player;
    this.mean = mean;
    this.standardDeviation = standardDeviation;
  }

  public Player getPlayer() {
    return player;
  }

  public double getMean() {
    return mean;
  }

  public double getStandardDeviation() {
    return standardDeviation;
  }

  public double getConservativeRating() {
    return mean - standardDeviation * 3;
  }

  @Override
  public String toString() {
    return String.format(
        "Rating{ mean: %.3f, standardDeviation: %.3f }",
            mean, standardDeviation);
  }
}