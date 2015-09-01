package kickr.web.model;

import javax.validation.constraints.NotNull;
import kickr.db.entity.Team;

public class TeamData {

  @NotNull
  protected PlayerData offense;
  
  protected PlayerData defense;
  
  public TeamData() { }
  
  public TeamData(PlayerData offense, PlayerData defense) {
    this.offense = offense;
    this.defense = defense;
  }
  
  public PlayerData getOffense() {
    return offense;
  }
  
  public void setOffense(PlayerData offense) {
    this.offense = offense;
  }
  
  public PlayerData getDefense() {
    return defense;
  }
  
  public void setDefense(PlayerData defense) {
    this.defense = defense;
  }
  
  public static TeamData fromTeam(Team team) {
    return new TeamData(PlayerData.fromPlayer(team.getOffense()), PlayerData.fromPlayer(team.getDefense()));
  }
}
