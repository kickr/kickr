package kickr.core.model;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotNull;

import kickr.db.entity.Player;

public class PlayerData {

  protected String name;
  
  @NotNull
  protected String alias;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getAlias() {
    return alias;
  }

  public void setAlias(String alias) {
    this.alias = alias;
  }
  
  public static PlayerData fromPlayer(Player player) {
    PlayerData playerData = new PlayerData();
    
    playerData.setAlias(player.getAlias());
    playerData.setName(player.getName());
    
    return playerData;
  }
  
  public static List<PlayerData> fromPlayers(List<Player> players) {
    List<PlayerData> playersData = new ArrayList<>();
    
    for (Player player : players) {
      playersData.add(fromPlayer(player));
    }
    
    return playersData;
  }
  
  public Player toPlayer() {
    return new Player(alias, name);
  }
}
