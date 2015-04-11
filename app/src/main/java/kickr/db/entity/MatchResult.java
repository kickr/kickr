package kickr.db.entity;

import java.io.Serializable;
import javax.persistence.Embeddable;
import kickr.util.MatchResultDetails;
import kickr.util.Side;

/**
 *
 * @author nikku
 */
@Embeddable
public class MatchResult implements Serializable {

  protected int team1Wins;

  protected int team2Wins;
  
  protected int totalGames;

  public MatchResult() { }

  private MatchResult(int team1Wins, int team2Wins, int totalGames) {
    this.team1Wins = team1Wins;
    this.team2Wins = team2Wins;
    this.totalGames = totalGames;
  }

  public int getTeam1Wins() {
    return team1Wins;
  }

  public void setTeam1Wins(int team1Wins) {
    this.team1Wins = team1Wins;
  }

  public int getTeam2Wins() {
    return team2Wins;
  }

  public void setTeam2Wins(int team2Wins) {
    this.team2Wins = team2Wins;
  }

  public int getTotalGames() {
    return totalGames;
  }

  public void setTotalGames(int totalGames) {
    this.totalGames = totalGames;
  }

  /**
   * Create a match result from the given match result details.
   *
   * @param result
   * @return
   */
  public static MatchResult create(MatchResultDetails result) {
    return new MatchResult(result.getWins(Side.TEAM1), result.getWins(Side.TEAM2), result.getTotalGames());
  }
}
