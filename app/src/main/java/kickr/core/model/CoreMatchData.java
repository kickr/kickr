package kickr.core.model;

import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotNull;

public class CoreMatchData {
  
  @NotNull
  protected TeamsData teams;

  @NotNull
  protected List<GameData> games;
  
  protected ResultData score;
  
  protected Date played;
  
  protected TableData table;
  
  
  public TeamsData getTeams() {
    return teams;
  }
  
  public void setTeams(TeamsData teams) {
    this.teams = teams;
  }
  
  public ResultData getScore() {
    return score;
  }
  
  public void setScore(ResultData score) {
    this.score = score;
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
  
  public TableData getTable() {
    return table;
  }
  
  public void setTable(TableData table) {
    this.table = table;
  }
  
}
