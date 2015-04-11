package kickr.db.entity;

import java.util.Date;
import java.util.List;
import javax.persistence.AssociationOverride;
import javax.persistence.AssociationOverrides;
import javax.persistence.Embedded;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import kickr.db.entity.user.User;
import kickr.util.Side;


@Entity
@Table(name = "kickr_match")
@NamedQueries({
  @NamedQuery(
    name = "Match.getAll",
    query = "SELECT m FROM Match m " + 
              "LEFT JOIN FETCH m.team1.offense " +
              "LEFT JOIN FETCH m.team1.defense " +
              "LEFT JOIN FETCH m.team2.offense " +
              "LEFT JOIN FETCH m.team2.defense " +
              "JOIN FETCH m.table " +
              "LEFT JOIN FETCH m.creator " +
            "WHERE m.played IS NOT NULL ORDER BY m.played DESC"),
  @NamedQuery(
    name = "Match.getUnrated",
    query = "SELECT m FROM Match m WHERE m.removed = false AND m.rated = false AND m.played IS NOT NULL AND m.played < :played")
})
public class Match extends BaseEntity {

  @NotNull
  @Embedded
  @AssociationOverrides({
    @AssociationOverride(name="offense", joinColumns = @JoinColumn(name="team1_offense_id")),
    @AssociationOverride(name="defense", joinColumns = @JoinColumn(name="team1_defense_id"))
  })
  protected Team team1;
  
  @NotNull
  @Embedded
  @AssociationOverrides({
    @AssociationOverride(name="offense", joinColumns = @JoinColumn(name="team2_offense_id")),
    @AssociationOverride(name="defense", joinColumns = @JoinColumn(name="team2_defense_id"))
  })
  protected Team team2;

  @NotNull
  @ManyToOne
  protected FoosballTable table;
  
  @Embedded
  protected MatchResult result;

  @Temporal(TemporalType.TIMESTAMP)
  protected Date played;

  protected boolean rated = false;
  
  protected boolean removed = false;

  @OneToMany
  @JoinColumn(name = "match_id")
  protected List<Game> games;

  @ManyToOne
  @JoinColumn(name = "creator_id")
  private User creator;

  public Match() { }

  public Team getTeam1() {
    return team1;
  }

  public void setTeam1(Team team1) {
    this.team1 = team1;
  }

  public Team getTeam2() {
    return team2;
  }

  public void setTeam2(Team team2) {
    this.team2 = team2;
  }

  public boolean isRemoved() {
    return removed;
  }

  public void setRemoved(boolean removed) {
    this.removed = removed;
  }

  public FoosballTable getTable() {
    return table;
  }

  public User getCreator() {
    return creator;
  }

  public void setCreator(User creator) {
    this.creator = creator;
  }

  public void setTable(FoosballTable table) {
    this.table = table;
  }

  public void setPlayed(Date played) {
    this.played = played;
  }

  public Date getPlayed() {
    return played;
  }

  public List<Game> getGames() {
    return games;
  }

  public boolean isRated() {
    return rated;
  }

  public void setRated(boolean rated) {
    this.rated = rated;
  }

  public void setGames(List<Game> games) {
    this.games = games;
  }
  
  public Team getTeam(Side side) {
    if (side == Side.TEAM1) {
      return team1;
    } else {
      return team2;
    }
  }

  public void setResult(MatchResult result) {
    this.result = result;
  }

  public MatchResult getResult() {
    return result;
  }
}
