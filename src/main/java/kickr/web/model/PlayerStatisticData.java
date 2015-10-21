package kickr.web.model;

import java.util.List;
import java.util.stream.Collectors;
import kickr.db.dto.PlayerStatistics;

/**
 *
 * @author nikku
 */
public class PlayerStatisticData {

  private PlayerData player;

  private long totalScore;
  private long games;
  private double average;
  private double confidence;
  private double rating;

  public PlayerStatisticData() {}

  public PlayerStatisticData(long totalScore, long games, double average, double confidence, double rating, PlayerData player) {
    this.totalScore = totalScore;
    this.games = games;

    this.average = average;
    this.confidence = confidence;
    this.rating = rating;

    this.player = player;
  }

  public PlayerData getPlayer() {
    return player;
  }

  public long getGames() {
    return games;
  }

  public long getTotalScore() {
    return totalScore;
  }

  public double getAverage() {
    return average;
  }

  public double getConfidence() {
    return confidence;
  }

  public double getRating() {
    return rating;
  }

  
  // helpers
  
  public static List<PlayerStatisticData> fromStatistics(List<PlayerStatistics> scores) {
    return scores.stream().map(PlayerStatisticData::fromStatistic).collect(Collectors.toList());
  }
  
  public static PlayerStatisticData fromStatistic(PlayerStatistics change) {
    return new PlayerStatisticData(
        change.getTotalScore(),
        change.getGames(),
        change.getAverage(),
        change.getConfidence(),
        change.getRating(),
        PlayerData.fromPlayer(change.getPlayer()));
  }
}
