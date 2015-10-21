package kickr.web.model;

import java.util.List;
import java.util.stream.Collectors;
import kickr.db.entity.ScoreChange;
import kickr.db.entity.ScoreType;

/**
 *
 * @author nikku
 */
public class ScoreChangeData {
  
  private final int value;
  private final ScoreType type;
  
  private final Long matchId;

  public ScoreChangeData(int value, ScoreType type, Long matchId) {
    this.value = value;
    this.type = type;
    this.matchId = matchId;
  }

  public ScoreType getType() {
    return type;
  }

  public int getValue() {
    return value;
  }

  public Long getMatchId() {
    return matchId;
  }
  
  
  // helpers
  
  public static List<ScoreChangeData> fromChanges(List<ScoreChange> changes) {
    return changes.stream().map(ScoreChangeData::fromChange).collect(Collectors.toList());
  }
  
  public static ScoreChangeData fromChange(ScoreChange change) {
    return new ScoreChangeData(change.getValue(), ScoreType.LOST, change.getMatch().getId());
  }
}
