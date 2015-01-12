package kickr.db.entity;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

/**
 *
 * @author nikku
 */
@Entity
@Table(name = "kickr_score_change")
public class ScoreChange extends BaseEntity {

  @NotNull
  @Enumerated(EnumType.ORDINAL)
  @Column(name = "change_type")
  private ScoreType type;
  
  @NotNull
  @Column(name = "change_value")
  private int value;
  
  @NotNull
  @ManyToOne
  private Player player;
  
  @NotNull
  @ManyToOne
  private Match match;
  
  @NotNull
  @ManyToOne
  private Score score;
  
  public ScoreChange() { }
  
  public ScoreChange(ScoreType type, int value, Player player, Match match, Score score) {
    this.type = type;
    this.value = value;
    
    this.player = player;
    this.match = match;
    this.score = score;
    
    this.created = match.getPlayed();
  }

  public int getValue() {
    return value;
  }

  public void setValue(int value) {
    this.value = value;
  }

  public ScoreType getType() {
    return type;
  }

  public Player getPlayer() {
    return player;
  }

  public void setPlayer(Player player) {
    this.player = player;
  }

  public Match getMatch() {
    return match;
  }

  public void setMatch(Match match) {
    this.match = match;
  }

  public Score getScore() {
    return score;
  }

  public void setScore(Score score) {
    this.score = score;
  }
  
  public void apply() {
    score.addValue(value);
    score.setLastUpdated(new Date());
  }
  
  public void unapply() {
    score.addValue(-1 * value);
    score.setLastUpdated(new Date());
  }
}
