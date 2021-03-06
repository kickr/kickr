package kickr.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import kickr.web.model.match.CoreMatchData;
import kickr.web.model.PlayerData;
import kickr.web.model.FoosballTableData;
import kickr.web.model.TeamData;
import kickr.db.dao.FoosballTableDAO;
import kickr.db.dao.GameDAO;
import kickr.db.dao.MatchDAO;
import kickr.db.dao.PlayerDAO;
import kickr.db.entity.FoosballTable;
import kickr.db.entity.Game;
import kickr.db.entity.Match;
import kickr.db.entity.MatchResult;
import kickr.db.entity.Player;
import kickr.db.entity.Team;
import kickr.db.entity.user.User;
import kickr.util.MatchResultDetails;


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
  
  protected FoosballTable selectTable(FoosballTableData tableData) {
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

  public Match insertMatch(CoreMatchData matchData, User creator) {
    Match match = new Match();
    
    // assign table
    match.setTable(selectTable(matchData.getTable()));

    // creator
    match.setCreator(creator);

    // assign teams
    TeamData team1Data = matchData.getTeam1();
    
    Player offenseTeam1 = selectOrInsertPlayer(team1Data.getOffense());
    Team team1 = new Team(offenseTeam1, team1Data.getDefense() != null ? selectOrInsertPlayer(team1Data.getDefense()) : offenseTeam1);

    TeamData team2Data = matchData.getTeam2();
    
    Player offenseTeam2 = selectOrInsertPlayer(team2Data.getOffense());
    Team team2 = new Team(offenseTeam2, team2Data.getDefense() != null ? selectOrInsertPlayer(team2Data.getDefense()) : offenseTeam2);

    match.setTeam1(team1);
    match.setTeam2(team2);
    
    // assign played date
    match.setPlayed(new Date());
    
    // create games
    List<Game> games = new ArrayList<>();

    for (int i = 0; i < matchData.getGames().size(); i++) {
      Game game = matchData.getGames().get(i).toGame();
      game.setGameNumber(i);
      games.add(game);

      gameDao.create(game);
    }

    match.setGames(games);

    MatchResultDetails resultDetails = MatchResultDetails.compute(match);

    match.setResult(MatchResult.create(resultDetails));

    matchDao.create(match);
    
    return match;
  }

}
