package kickr.core.model;

import javax.validation.constraints.NotNull;

public class TeamData {

  @NotNull
  protected PlayerData offense;
  
  @NotNull
  protected PlayerData defense;
  
  public TeamData() {
    
  }
  
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
}
