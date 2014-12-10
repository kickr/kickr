package kickr.core.model;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotNull;

import kickr.db.entity.Game;
import kickr.db.entity.Match;

public class MatchData extends CoreMatchData {

  @NotNull
  protected Long id;
  
  public Long getId() {
    return id;
  }
  public void setId(Long id) {
    this.id = id;
  }
  
  public static MatchData fromMatch(Match match) {
    MatchData matchData = new MatchData();
    matchData.id = match.getId();
    
    TeamsData teamsData = new TeamsData();
    teamsData.setTeam1(new TeamData(PlayerData.fromPlayer(match.getOffenseTeam1()), 
        PlayerData.fromPlayer(match.getDefenseTeam1())));
    teamsData.setTeam2(new TeamData(PlayerData.fromPlayer(match.getOffenseTeam2()), 
        PlayerData.fromPlayer(match.getDefenseTeam2())));
    matchData.teams = teamsData;
    
    
    Integer winsTeam1 = 0;
    Integer winsTeam2 = 0;
    List<GameData> gamesData = new ArrayList<GameData>();
    for (Game game : match.getGames()) {
      gamesData.add(GameData.fromGame(game));
      
      if (game.getScoreTeam1() > game.getScoreTeam2()) {
        winsTeam1++;
      } else {
        winsTeam2++;
      }
    }
    matchData.games = gamesData;
    
    ScoreData scoreData = new ScoreData();
    scoreData.setTeam1(winsTeam1);
    scoreData.setTeam2(winsTeam2);
    matchData.score = scoreData;
    
    matchData.table = TableData.fromTable(match.getTable());
        
    return matchData;
  }
  
  public static List<MatchData> fromMatches(List<Match> matches) {
    List<MatchData> matchesData = new ArrayList<>();
    
    for (Match match : matches) {
      matchesData.add(fromMatch(match));
    }
    
    return matchesData;
  }
}
