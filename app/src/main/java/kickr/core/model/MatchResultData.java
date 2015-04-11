package kickr.core.model;

import kickr.db.entity.MatchResult;

public class MatchResultData {

  protected Integer team1;
  protected Integer team2;
  protected Integer totalGames;
  
  public Integer getTeam1() {
    return team1;
  }

  public void setTeam1(Integer team1) {
    this.team1 = team1;
  }
  
  public Integer getTeam2() {
    return team2;
  }

  public void setTeam2(Integer team2) {
    this.team2 = team2;
  }

  public static MatchResultData fromMatchResult(MatchResult result) {
    MatchResultData resultData = new MatchResultData();
    
    resultData.team1 = result.getTeam1Wins();
    resultData.team2 = result.getTeam2Wins();
    resultData.totalGames = result.getTotalGames();

    return resultData;
  }
}
