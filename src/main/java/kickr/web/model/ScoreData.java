package kickr.web.model;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import kickr.db.entity.Score;
import kickr.db.entity.ScoreChange;
import kickr.db.dto.ScoreWithChanges;

/**
 *
 * @author nikku
 */
public class ScoreData {
  
  private final PlayerData player;
  
  private final int value;
  private final long changes;
  
  private final Date lastUpdated;

  public ScoreData(PlayerData player, int value, Date lastUpdated, int changes) {
    
    this.player = player;
    
    this.value = value;
    this.changes = changes;
    
    this.lastUpdated = lastUpdated;
  }
  
  public static ScoreData fromScore(ScoreWithChanges scoreWithChanges) {
    Score score = scoreWithChanges.getScore();
    
    int changes = scoreWithChanges.getChanges();
    
    return new ScoreData(
      PlayerData.fromPlayer(score.getPlayer()), 
      score.getValue(),
      score.getCreated(),
      changes);
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

  public long getChanges() {
    return changes;
  }
}
