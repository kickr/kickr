package kickr.db.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
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
  @Column(name = "score_type")
  private String scoreType;

  @NotNull
  @Column(name = "change_type")
  private String changeType;
  
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
  @OneToOne
  @JoinColumn(name = "rating_id")
  private Rating rating;

  public ScoreChange() { }
  
  public ScoreChange(String scoreType, int value, Match match, Player player, Rating rating) {

    this.scoreType = scoreType;
    this.changeType = "matchResults";
    this.value = value;

    this.match = match;
    this.player = player;
    this.rating = rating;

    this.created = match.getPlayed();
  }

  public int getValue() {
    return value;
  }

  public String getScoreType() {
    return scoreType;
  }

  public String getChangeType() {
    return changeType;
  }

  public Player getPlayer() {
    return player;
  }

  public Match getMatch() {
    return match;
  }

  public Rating getRating() {
    return rating;
  }

  @Override
  public String toString() {
    return String.format(
        "ScoreChange{ scoreType: %s, value: %s }",
            scoreType, value);
  }
}
