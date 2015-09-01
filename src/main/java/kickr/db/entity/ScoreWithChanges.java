package kickr.db.entity;

import java.util.List;

/**
 *
 * @author nikku
 */
public class ScoreWithChanges {
  
  private final Score score;
  
  private final List<ScoreChange> changes;

  public ScoreWithChanges(Score score, List<ScoreChange> changes) {
    this.score = score;
    this.changes = changes;
  }

  public Score getScore() {
    return score;
  }

  public List<ScoreChange> getChanges() {
    return changes;
  }
}
