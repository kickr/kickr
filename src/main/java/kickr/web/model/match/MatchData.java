package kickr.web.model.match;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.constraints.NotNull;
import kickr.db.entity.Game;

import kickr.db.entity.Match;
import kickr.web.model.GameData;
import kickr.web.model.FoosballTableData;
import kickr.web.model.TeamData;

public class MatchData extends CoreMatchData {

  @NotNull
  protected Long id;
  
  private boolean rated;
  
  private boolean removed;
  
  private Date created;
  
  public Long getId() {
    return id;
  }
  
  public void setId(Long id) {
    this.id = id;
  }

  public boolean isRated() {
    return rated;
  }

  public void setRated(boolean rated) {
    this.rated = rated;
  }

  public boolean isRemoved() {
    return removed;
  }

  public void setRemoved(boolean removed) {
    this.removed = removed;
  }

  public Date getCreated() {
    return created;
  }

  public void setCreated(Date created) {
    this.created = created;
  }

  public void addGames(List<Game> games) {
    this.games = GameData.fromGames(games);
  }

  public static MatchData fromMatch(Match match) {
    
    MatchData matchData = new MatchData();
    matchData.id = match.getId();
    
    matchData.team1 = TeamData.fromTeam(match.getTeam1());
    matchData.team2 = TeamData.fromTeam(match.getTeam2());
    
    matchData.setCreated(match.getCreated());
    matchData.setPlayed(match.getPlayed());
    matchData.setRated(match.isRated());
    matchData.setRemoved(match.isRemoved());
    
    matchData.result = MatchResultData.fromMatchResult(match.getResult());
    
    matchData.table = FoosballTableData.fromTable(match.getTable());
        
    return matchData;
  }
  
  public static List<MatchData> fromMatches(List<Match> matches) {
    return matches.stream().map(MatchData::fromMatch).collect(Collectors.toList());
  }
}
