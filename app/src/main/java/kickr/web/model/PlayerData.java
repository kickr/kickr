package kickr.web.model;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotNull;

import kickr.db.entity.Player;

public class PlayerData {

  protected String name;
  
  @NotNull
  protected String alias;
  
  protected String email;

  public PlayerData() { }

  public PlayerData(String alias, String email) {
    this.alias = alias;
    this.email = email;
  }

  public PlayerData(String alias) {
    this(alias, null);
  }

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
  
  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public static PlayerData fromPlayer(Player player) {
    
    if (player == null) {
      return null;
    }
    
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
    return new Player(alias, name, email);
  }
}
