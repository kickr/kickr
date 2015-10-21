package kickr.db.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

/**
 *
 * @author nikku
 */
@Entity
@Table(name = "kickr_score")
@NamedQueries({
  @NamedQuery(
    name="Score.latestByPlayer",
    query="SELECT s FROM Score s JOIN FETCH s.player p WHERE " +
                "s.player = :player AND " +
                "s.type = :scoreType AND " +
                "s.created <= :latestDate " +
              "ORDER BY s.created DESC, s.runIndex DESC")
})
public class Score extends BaseEntity {
  
  @ManyToOne
  private Player player;
  
  @NotNull
  private int value;

  @NotNull
  @Column(name="score_type")
  private String type;

  @Column(name="run_index")
  private int runIndex;

  public Score() {
    super();
  }

  public Score(Player player, String type) {
    this(player, type, 0, 0);
  }

  public Score(Player player, String type, int value) {
    this(player, type, 0, value);
  }

  public Score(Player player, String type, int runIndex, int value) {
    super();

    this.player = player;
    this.type = type;

    this.runIndex = runIndex;
    this.value = value;
  }

  public Player getPlayer() {
    return player;
  }

  public int getValue() {
    return value;
  }

  public String getType() {
    return type;
  }

  public Score addValue(int scoreUpdate) {
    return new Score(player, type, runIndex + 1, value + scoreUpdate);
  }
}
