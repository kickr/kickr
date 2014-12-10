package kickr.core.model;

import javax.validation.constraints.NotNull;

public class TeamData {

  @NotNull
  protected String offense;
  
  @NotNull
  protected String defense;
  
  public TeamData() {
    
  }
  
  public TeamData(String offense, String defense) {
    this.offense = offense;
    this.defense = defense;
  }
  
  public String getOffense() {
    return offense;
  }
  public void setOffense(String offense) {
    this.offense = offense;
  }
  public String getDefense() {
    return defense;
  }
  public void setDefense(String defense) {
    this.defense = defense;
  }
}
