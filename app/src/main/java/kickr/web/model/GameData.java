package kickr.web.model;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import javax.validation.constraints.NotNull;

import kickr.db.entity.Game;

public class GameData {

  @NotNull
  protected Integer team1;
  
  @NotNull
  protected Integer team2;

  public GameData() { }

  public GameData(Integer team1, Integer team2) {
    this.team1 = team1;
    this.team2 = team2;
  }

  public Integer getTeam1() {
    return team1;
  }

  public void setTeam1(Integer team1) {
    this.team1 = team1;
  }

  public Integer getTeam2() {
    return team2;
  }

  public void setTeam2(Integer team2) {
    this.team2 = team2;
  }
  
  public Game toGame() {
    Game game = new Game();
    game.setScoreTeam1(team1);
    game.setScoreTeam2(team2);
    return game;
  }
  
  
  public static GameData fromGame(Game game) {
    GameData gameData = new GameData();
    
    gameData.setTeam1(game.getScoreTeam1());
    gameData.setTeam2(game.getScoreTeam2());
    
    return gameData;
  }

  public static List<GameData> fromGames(Collection<Game> games) {
    return games.stream().map(GameData::fromGame).collect(Collectors.toList());
  }
}
