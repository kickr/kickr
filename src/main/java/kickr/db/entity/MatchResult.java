package kickr.db.entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 *
 * @author nikku
 */
@Embeddable
public class MatchResult implements Serializable {

  @Column(name = "team1_wins")
  protected int team1Wins;

  @Column(name = "team2_wins")
  protected int team2Wins;

  @Column(name = "total_games")
  protected int totalGames;

  public MatchResult() { }

  public MatchResult(int team1Wins, int team2Wins, int totalGames) {
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
}
