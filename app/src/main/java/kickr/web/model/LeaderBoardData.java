package kickr.web.model;

import java.util.List;

/**
 *
 * @author nikku
 */
public class LeaderBoardData {

  private final List<ScoreData> scores;
  private final List<PlayerStatisticData> performance;

  public LeaderBoardData(List<ScoreData> scores, List<PlayerStatisticData> performance) {

    this.scores = scores;
    this.performance = performance;
  }

  public List<PlayerStatisticData> getPerformance() {
    return performance;
  }

  public List<ScoreData> getScores() {
    return scores;
  }
}
