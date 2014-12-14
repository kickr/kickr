package kickr.db.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "kickr_game")
public class Game extends BaseEntity {

  @NotNull
  protected Integer scoreTeam1;

  @NotNull
  protected Integer scoreTeam2;
  
  /**
   * Determines the order of games in a match
   */
  @NotNull
  protected Integer gameNumber;
  
  public Integer getScoreTeam1() {
    return scoreTeam1;
  }

  public void setScoreTeam1(Integer scoreTeam1) {
    this.scoreTeam1 = scoreTeam1;
  }
  
  public Integer getScoreTeam2() {
    return scoreTeam2;
  }
  
  public void setScoreTeam2(Integer scoreTeam2) {
    this.scoreTeam2 = scoreTeam2;
  }
  
  public Integer getGameNumber() {
    return gameNumber;
  }
  
  public void setGameNumber(Integer gameNumber) {
    this.gameNumber = gameNumber;
  }
}
