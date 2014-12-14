package kickr.core.model;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import kickr.db.entity.Score;

/**
 *
 * @author nikku
 */
public class ScoreData {
  
  private PlayerData player;
  private int value;
  private Date lastUpdated;

  public ScoreData(PlayerData player, int value, Date lastUpdated) {
    
    this.player = player;
    this.value = value;
    this.lastUpdated = lastUpdated;
  }
  
  public static ScoreData fromScore(Score score) {
    return new ScoreData(PlayerData.fromPlayer(score.getPlayer()), score.getValue(), score.getLastUpdated());
  }
  
  public static List<ScoreData> fromScores(List<Score> scores) {
    return scores.stream()
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
}
