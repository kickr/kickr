package kickr.db.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "kickr_table")
public class FoosballTable extends BaseEntity {
  
  protected String name;
  
  protected String team1Alias;
  protected String team2Alias;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getTeam1Alias() {
    return team1Alias;
  }

  public void setTeam1Alias(String team1Alias) {
    this.team1Alias = team1Alias;
  }

  public String getTeam2Alias() {
    return team2Alias;
  }

  public void setTeam2Alias(String team2Alias) {
    this.team2Alias = team2Alias;
  }
}
