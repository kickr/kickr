package kickr.web.model.match;

import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotNull;
import kickr.web.model.GameData;
import kickr.web.model.FoosballTableData;
import kickr.web.model.TeamData;

public class CoreMatchData {
  
  @NotNull
  protected TeamData team1;
  
  @NotNull
  protected TeamData team2;

  @NotNull
  protected List<GameData> games;
  
  protected MatchResultData result;
  
  protected Date played;
  
  protected FoosballTableData table;

  public void setTeam1(TeamData team1) {
    this.team1 = team1;
  }

  public TeamData getTeam1() {
    return team1;
  }

  public TeamData getTeam2() {
    return team2;
  }

  public void setTeam2(TeamData team2) {
    this.team2 = team2;
  }
  
  public MatchResultData getResult() {
    return result;
  }
  
  public void setResult(MatchResultData score) {
    this.result = score;
  }
  
  public List<GameData> getGames() {
    return games;
  }
  
  public void setGames(List<GameData> games) {
    this.games = games;
  }
  
  public Date getPlayed() {
    return played;
  }
  
  public void setPlayed(Date played) {
    this.played = played;
  }
  
  public FoosballTableData getTable() {
    return table;
  }
  
  public void setTable(FoosballTableData table) {
    this.table = table;
  }
  
}
