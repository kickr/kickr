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

/**
 *
 * @author nikku
 */
public class Scores {

  public static final int WIN_MULTIPLIER = 24;
  public static final int LOSS_MULTIPLIER = 12;
  
  public static final int DEFAULT_WIN = 7;

  public static final double MV = 0.22;

  public static int calculateWinPoints(double w) {
    return (int) ((1 - ((w * (1 - MV)) + MV)) / ((w * (1 - MV)) + MV) * WIN_MULTIPLIER) + DEFAULT_WIN;
  }

  public static int calculateLossPoints(double w) {
    double l = 1.0 - w;
    return (int) ((l * (1 - MV)) / (1 - (l * (1 - MV))) * -LOSS_MULTIPLIER);
  }
}
