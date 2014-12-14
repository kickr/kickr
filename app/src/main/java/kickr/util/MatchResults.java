package kickr.util;

import kickr.db.entity.Game;
import kickr.db.entity.Match;

/**
 *
 * @author nikku
 */
public class MatchResults {

  private int team1Goals = 0;
  private int team2Goals = 0;
  
  private int team1Score = 0;
  private int team2Score = 0;
  
  private int totalGames = 0;
  
  
  public enum ResultType {
    TEAM1_WON,
    TEAM2_WON,
    TIE
  };
  
  public static MatchResults compute(Match match) {
    
    MatchResults matchResults = new MatchResults();
    
    for (Game game: match.getGames()) {
      matchResults.team1Goals += game.getScoreTeam1();
      matchResults.team2Goals += game.getScoreTeam2();
      
      matchResults.totalGames++;
      
      if (game.getScoreTeam1() > game.getScoreTeam2()) {
        matchResults.team1Score++;
      }
      
      if (game.getScoreTeam2() > game.getScoreTeam1()) {
        matchResults.team2Score++;
      }
    }
    
    return matchResults;
  }

  public Integer getTeam1Score() {
    return team1Score;
  }

  public Integer getTeam2Score() {
    return team2Score;
  }

  public int getTotalGames() {
    return totalGames;
  }

  public int getTeam1Goals() {
    return team1Goals;
  }

  public int getTeam2Goals() {
    return team2Goals;
  }
  
  public ResultType getResultType() {
    
    if (team1Score > team2Score) {
      return ResultType.TEAM1_WON;
    }
    
    if (team2Score > team1Score) {
      return ResultType.TEAM2_WON;
    }
    
    return ResultType.TIE;
  }

}
