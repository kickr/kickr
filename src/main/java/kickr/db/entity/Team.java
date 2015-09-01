package kickr.db.entity;

import java.io.Serializable;
import javax.persistence.Embeddable;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

/**
 *
 * @author nikku
 */
@Embeddable
public class Team implements Serializable {

  @NotNull
  @ManyToOne
  protected Player offense;

  @NotNull
  @ManyToOne
  protected Player defense;

  public Team() { }
  
  public Team(Player offense, Player defense) {
    this.offense = offense;
    this.defense = defense;
  }

  public Player getOffense() {
    return offense;
  }

  public Player getDefense() {
    return defense;
  }
  
  public boolean isSingle() {
    return offense.equals(defense);
  }
}
