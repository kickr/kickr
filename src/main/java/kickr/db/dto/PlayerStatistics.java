package kickr.db.dto;

import kickr.db.entity.Player;

/**
 *
 * @author nikku
 */
public class PlayerStatistics {

  private final Player player;

  private final long totalScore;
  private final long games;

  private final double average;
  private final double confidence;
  private final double rating;

  public PlayerStatistics(Player player, Long totalScore, Long games) {
    this.player = player;

    this.totalScore = totalScore;
    this.games = games;

    this.average = trim((0.0 + totalScore) / games);
    this.confidence = trim(1 - 1 / Math.sqrt(games * 2));
    this.rating = trim(average * confidence * 10);
  }

  public long getGames() {
    return games;
  }

  public Player getPlayer() {
    return player;
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

  private static double trim(double value) {
    return Math.floor(value * 100) / 100;
  }
}
