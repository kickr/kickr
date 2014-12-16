package kickr.core.model;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import kickr.db.entity.Score;
import kickr.db.entity.ScoreChange;
import kickr.db.entity.ScoreWithChanges;

/**
 *
 * @author nikku
 */
public class ScoreData {
  
  private final PlayerData player;
  
  private final int value;
  private final long added;
  
  private final Date lastUpdated;
  private final List<ScoreChangeData> changes;

  public ScoreData(PlayerData player, int value, Date lastUpdated, int added, List<ScoreChangeData> changes) {
    
    this.player = player;
    
    this.value = value;
    this.added = added;
    
    this.changes = changes;
    
    this.lastUpdated = lastUpdated;
  }
  
  public static ScoreData fromScore(ScoreWithChanges scoreWithChanges) {
    Score score = scoreWithChanges.getScore();
    List<ScoreChange> changes = scoreWithChanges.getChanges();
    
    int added = (int) changes.stream().collect(Collectors.summarizingInt(c -> c.getValue())).getSum();
    
    return new ScoreData(
      PlayerData.fromPlayer(score.getPlayer()), 
      score.getValue(),
      score.getLastUpdated(),
      added,
      ScoreChangeData.fromChanges(changes));
  }
  
  public static List<ScoreData> fromScores(List<ScoreWithChanges> scoresWithChanges) {
    return scoresWithChanges.stream()
              .map(ScoreData::fromScore)
              .collect(Collectors.toList());
  }

  public PlayerData getPlayer() {
    return player;
  }

  public int getValue() {
    return value;
  }
  
  public Date getLastUpdated() {
    return lastUpdated;
  }

  public long getAdded() {
    return added;
  }

  public List<ScoreChangeData> getChanges() {
    return changes;
  }
}
