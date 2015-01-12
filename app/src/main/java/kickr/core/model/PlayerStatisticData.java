package kickr.core.model;

import java.util.List;
import java.util.stream.Collectors;
import kickr.db.entity.PlayerStatistics;

/**
 *
 * @author nikku
 */
public class PlayerStatisticData {

  private PlayerData player;

  private long score;
  private long games;

  public PlayerStatisticData() {}

  public PlayerStatisticData(long score, long games, PlayerData player) {
    this.player = player;
    this.score = score;
    this.games = games;
  }

  public PlayerData getPlayer() {
    return player;
  }

  public long getGames() {
    return games;
  }

  public long getScore() {
    return score;
  }
  
  
  // helpers
  
  public static List<PlayerStatisticData> fromStatistics(List<PlayerStatistics> scores) {
    return scores.stream().map(PlayerStatisticData::fromStatistic).collect(Collectors.toList());
  }
  
  public static PlayerStatisticData fromStatistic(PlayerStatistics change) {
    return new PlayerStatisticData(change.getScore(), change.getGames(), PlayerData.fromPlayer(change.getPlayer()));
  }
}
