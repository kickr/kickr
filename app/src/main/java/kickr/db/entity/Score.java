package kickr.db.entity;

import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

/**
 *
 * @author nikku
 */
@Entity
@Table(name = "kickr_score")
@NamedQueries({
  @NamedQuery(name="Score.byPlayer", query="SELECT s FROM Score s JOIN FETCH s.player p WHERE p = :player")
})
public class Score extends BaseEntity {
  
  @ManyToOne
  private Player player;

  @Temporal(TemporalType.TIMESTAMP)
  private Date lastUpdated;
  
  @NotNull
  private int value = 0;
  
  public Score() { }
  
  public Score(Player player, int value) {
    this.player = player;
    this.lastUpdated = new Date();
    this.value = value;
  }

  public Long getId() {
    return id;
  }

  public Player getPlayer() {
    return player;
  }

  public Date getLastUpdated() {
    return lastUpdated;
  }
  
  public void setLastUpdated(Date lastUpdated) {
    this.lastUpdated = lastUpdated;
  }

  public void addValue(int points) {
    this.value += points;
  }

  public int getValue() {
    return value;
  }
}
