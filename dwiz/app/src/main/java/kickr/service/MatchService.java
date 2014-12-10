package kickr.service;

import java.util.ArrayList;
import java.util.List;

import kickr.core.model.CoreMatchData;
import kickr.core.model.PlayerData;
import kickr.core.model.TableData;
import kickr.db.FoosballTableDAO;
import kickr.db.GameDAO;
import kickr.db.MatchDAO;
import kickr.db.PlayerDAO;
import kickr.db.entity.FoosballTable;
import kickr.db.entity.Game;
import kickr.db.entity.Match;
import kickr.db.entity.Player;

import org.joda.time.DateTime;

public class MatchService {

  
  protected MatchDAO matchDao;
  protected PlayerDAO playerDao;
  protected FoosballTableDAO tableDao;
  protected GameDAO gameDao;
  
  public MatchService(MatchDAO matchDao, GameDAO gameDao, PlayerDAO playerDao, FoosballTableDAO tableDao) {
    this.matchDao = matchDao;
    this.gameDao = gameDao;
    this.playerDao = playerDao;
    this.tableDao = tableDao;
  }
  
  protected FoosballTable selectTable(TableData tableData) {
    return tableDao.findTableById(tableData.getId());
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
    
    match.setTable(selectTable(matchData.getTable()));

    List<Game> games = new ArrayList<>();
    
    for (int i = 0; i < matchData.getGames().size(); i++) {
      Game game = matchData.getGames().get(i).toGame();
      game.setGameNumber(i);
      games.add(game);
      
      gameDao.create(game);
    }
    
    match.setGames(games);
    
    matchDao.create(match);
  }
}
