package kickr.service;

import java.util.Date;
import java.util.List;
import kickr.db.MatchDAO;
import kickr.db.ScoreDAO;
import kickr.db.entity.Match;
import kickr.db.entity.Player;
import kickr.db.entity.Score;
import kickr.db.entity.Team;
import kickr.util.MatchResults;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author nikku
 */
public class RatingService {
  
  private static final Logger LOG = LoggerFactory.getLogger(RatingService.class);
  
  private static final int TIE_POINTS = 5;
  private static final int LOOSE_POINTS = -5;
  private static final int WIN_POINTS = 10;

  private static final double SINGLE_FACTOR = 0.3;
  
  
  private final MatchDAO matchDao;
  private final ScoreDAO scoreDao;

  public RatingService(MatchDAO matchDao, ScoreDAO scoreDao) {
    this.matchDao = matchDao;
    this.scoreDao = scoreDao;
  }

  public void calculateNewRatings() {
    List<Match> unratedMatches = matchDao.getUnratedMatches();

    LOG.info("rating {} matches", unratedMatches.size());
    
    try {
      
      unratedMatches.stream().forEach((match) -> {

        MatchResults results = MatchResults.compute(match);

        int team1Points;
        int team2Points;

        switch (results.getResultType()) {
          case TEAM1_WON:
            team1Points = WIN_POINTS;
            team2Points = LOOSE_POINTS;
            break;
          case TEAM2_WON: 
            team1Points = LOOSE_POINTS;
            team2Points = WIN_POINTS;
            break;
          default: 
            team1Points = team2Points = TIE_POINTS;
            break;
        }

        
        addTeamPoints(match, match.getTeam1(), team1Points);
        addTeamPoints(match, match.getTeam2(), team2Points);

        LOG.info("rated Match#{}", match.getId());

        match.setRated(true);
      });
      
    } catch (Throwable t) {
      LOG.error("rating failed", t);
      throw t;
    }
  }
  
 
  public void addTeamPoints(Match match, Team team, int points) {
    
    Player offensePlayer = team.getOffense();
    Player defensePlayer = team.getDefense();
    
    boolean isSingle = offensePlayer.equals(defensePlayer);
    
    addPlayerPoints(match, offensePlayer, points, isSingle);
    
    if (!isSingle) {
      addPlayerPoints(match, defensePlayer, points, isSingle);
    }
  }

  public void addPlayerPoints(Match match, Player player, int points, boolean single) {
    Score score = scoreDao.getOrCreateScore(player);
    
    if (single) {
      if (points < 0) {
        points *= (1 - SINGLE_FACTOR);
      } else {
        points *= (1 + SINGLE_FACTOR);
      }
    }
    
    score.addValue(points);
    score.setLastUpdated(new Date());
  }
}
