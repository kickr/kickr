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
package kickr.analytics;

import org.junit.Test;

import static kickr.analytics.Scores.*;

/**
 *
 * @author nikku
 */
public class ScoresTest {

  @Test
  public void shouldHaveAwesomeProbability() {

    double[] scenarios = {
      0.01, 0.1, 0.25,
      0.43, 0.47, 0.5, 0.53, 0.57,
      0.75, 0.9, 0.99
    };

    System.out.printf("mv=%.2f, winMultiplier=%s, lossMultiplier=%s\n", MV, WIN_MULTIPLIER, LOSS_MULTIPLIER);

    System.out.println("");
    System.out.println("prop | win | loss | sum | sum / prop");
    
    for (double probability : scenarios) {
      System.out.printf("%.2f | %2d | %2d | %2d | %.3f\n",
              probability,
              calculateWinPoints(probability),
              calculateLossPoints(probability),
              calculateWinPoints(probability) + -1 * calculateLossPoints(probability),
              (calculateWinPoints(probability) + -1 * calculateLossPoints(probability)) / (1 - probability));
    }
  }
}
