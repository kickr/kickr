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
import java.util.function.BiConsumer;
import kickr.db.entity.Player;
import kickr.db.entity.ScoreChange;

/**
 *
 * @author nikku
 */
public class ScoringResults {

  private final Map<Player, ScoreChange> scoreChanges;

  public ScoringResults(Map<Player, ScoreChange> scoreChanges) {
    this.scoreChanges = scoreChanges;
  }

  public Map<Player, ScoreChange> getScoreChanges() {
    return scoreChanges;
  }
  
  public ScoreChange getScoreChange(Player player) {
    return scoreChanges.get(player);
  }

  public void forEach(BiConsumer<? super Player, ? super ScoreChange> bc) {
    scoreChanges.forEach(bc);
  }

  @Override
  public String toString() {
    return String.format("ScoringResults{ scoreChanges: %s }", scoreChanges);
  }

}
