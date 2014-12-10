package kickr.service;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;

import kickr.core.model.CoreMatchData;
import kickr.core.model.GameData;
import kickr.db.MatchDAO;
import kickr.db.PlayerDAO;
import kickr.db.entity.Game;
import kickr.db.entity.Match;
import kickr.db.entity.Player;

public class MatchService {

  
  protected MatchDAO matchDao;
  protected PlayerDAO playerDao;
  
  public MatchService(MatchDAO matchDao, PlayerDAO playerDao) {
    this.matchDao = matchDao;
    this.playerDao = playerDao;
  }

  public void insertMatch(CoreMatchData matchData) {
    Match match = new Match();
    
    Player defenseTeam1 = playerDao.findPlayerByAlias(matchData.getTeams().getTeam1().getDefense());
    Player offenseTeam1 = playerDao.findPlayerByAlias(matchData.getTeams().getTeam1().getOffense());
    Player defenseTeam2 = playerDao.findPlayerByAlias(matchData.getTeams().getTeam2().getDefense());
    Player offenseTeam2 = playerDao.findPlayerByAlias(matchData.getTeams().getTeam2().getOffense());
    
    match.setDefenseTeam1(defenseTeam1);
    match.setOffenseTeam1(offenseTeam1);
    match.setDefenseTeam2(defenseTeam2);
    match.setOffenseTeam2(offenseTeam2);
    match.setDate(DateTime.now().toDate());
    
    List<Game> games = new ArrayList<Game>();
    for (GameData game : matchData.getGames()) {
      games.add(game.toGame());
    }
    match.setGames(games);
    
    matchDao.create(match);
  }
}
