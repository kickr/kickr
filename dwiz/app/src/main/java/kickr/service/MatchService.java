package kickr.service;

import java.util.ArrayList;
import java.util.List;

import kickr.core.model.CoreMatchData;
import kickr.core.model.GameData;
import kickr.core.model.PlayerData;
import kickr.db.MatchDAO;
import kickr.db.PlayerDAO;
import kickr.db.entity.Game;
import kickr.db.entity.Match;
import kickr.db.entity.Player;

import org.joda.time.DateTime;

public class MatchService {

  
  protected MatchDAO matchDao;
  protected PlayerDAO playerDao;
  
  public MatchService(MatchDAO matchDao, PlayerDAO playerDao) {
    this.matchDao = matchDao;
    this.playerDao = playerDao;
  }
  
  protected Player selectOrInsertPlayer(PlayerData playerData) {
    Player player = playerDao.findPlayerByAlias(playerData.getAlias());
    if (player == null) {
      player = playerData.toPlayer();
      playerDao.create(player);
    }
    return player;
  }

  public void insertMatch(CoreMatchData matchData) {
    Match match = new Match();
    
    Player offenseTeam1 = selectOrInsertPlayer(matchData.getTeams().getTeam1().getOffense());
    Player defenseTeam1 = selectOrInsertPlayer(matchData.getTeams().getTeam1().getDefense());
    Player offenseTeam2 = selectOrInsertPlayer(matchData.getTeams().getTeam2().getOffense());
    Player defenseTeam2 = selectOrInsertPlayer(matchData.getTeams().getTeam2().getDefense());
    
    match.setDefenseTeam1(defenseTeam1);
    match.setOffenseTeam1(offenseTeam1);
    match.setDefenseTeam2(defenseTeam2);
    match.setOffenseTeam2(offenseTeam2);
    match.setDate(DateTime.now().toDate());
    
    List<Game> games = new ArrayList<Game>();
    
    for (int i = 0; i < matchData.getGames().size(); i++) {
      Game game = matchData.getGames().get(i).toGame();
      game.setGameNumber(i);
      games.add(game);
    }
    
    match.setGames(games);
    
    matchDao.create(match);
  }
}
