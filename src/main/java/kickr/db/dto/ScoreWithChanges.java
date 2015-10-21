package kickr.db.dto;

import kickr.db.entity.Score;

/**
 *
 * @author nikku
 */
public class ScoreWithChanges {
  
  private final Score score;
  
  private final int changes;

  public ScoreWithChanges(Score score, int changes) {
    this.score = score;
    this.changes = changes;
  }

  public Score getScore() {
    return score;
  }

  public int getChanges() {
    return changes;
  }
}
