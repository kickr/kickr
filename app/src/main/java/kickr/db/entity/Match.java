package kickr.db.entity;

import java.util.Collection;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;


@Entity
@Table(name = "kickr_match")
@NamedQueries({
  @NamedQuery(
    name = "Match.list",
    query = "SELECT m FROM Match m WHERE m.removed = false ORDER BY m.date DESC")
})
public class Match extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  protected Long id;

  @NotNull
  @ManyToOne
  protected Player defenseTeam1;
  
  @NotNull
  @ManyToOne
  protected Player offenseTeam1;

  @NotNull
  @ManyToOne
  protected Player defenseTeam2;

  @NotNull
  @ManyToOne
  protected Player offenseTeam2;

  @NotNull
  @ManyToOne
  protected FoosballTable table;

  @NotNull
  @Temporal(TemporalType.TIMESTAMP)
  protected Date date;
  
  @OneToMany
  @OrderBy("gameNumber ASC")
  protected Collection<Game> games;
  
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Player getDefenseTeam1() {
    return defenseTeam1;
  }

  public void setDefenseTeam1(Player defenseTeam1) {
    this.defenseTeam1 = defenseTeam1;
  }

  public Player getOffenseTeam1() {
    return offenseTeam1;
  }

  public void setOffenseTeam1(Player offenseTeam1) {
    this.offenseTeam1 = offenseTeam1;
  }

  public Player getDefenseTeam2() {
    return defenseTeam2;
  }

  public void setDefenseTeam2(Player defenseTeam2) {
    this.defenseTeam2 = defenseTeam2;
  }

  public Player getOffenseTeam2() {
    return offenseTeam2;
  }

  public void setOffenseTeam2(Player offenseTeam2) {
    this.offenseTeam2 = offenseTeam2;
  }

  public FoosballTable getTable() {
    return table;
  }

  public void setTable(FoosballTable table) {
    this.table = table;
  }

  public Date getDate() {
    return date;
  }

  public void setDate(Date date) {
    this.date = date;
  }

  public Collection<Game> getGames() {
    return games;
  }

  public void setGames(Collection<Game> games) {
    this.games = games;
  }
}
