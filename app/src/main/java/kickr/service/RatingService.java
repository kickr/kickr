package kickr.service;

import java.time.Duration;
import java.util.List;
import kickr.config.RatingConfiguration;
import kickr.db.MatchDAO;
import kickr.db.ScoreDAO;
import kickr.db.entity.Match;
import kickr.db.entity.Player;
import kickr.db.entity.Score;
import kickr.db.entity.ScoreChange;
import kickr.db.entity.ScoreType;
import kickr.db.entity.Team;
import kickr.util.MatchResultDetails;
import kickr.util.ScoreUpdates;
import kickr.util.Side;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author nikku
 */
public class RatingService {
  
  private static final Logger LOG = LoggerFactory.getLogger(RatingService.class);
  
  private final MatchDAO matchDao;
  private final ScoreDAO scoreDao;
  
  private final Duration ratingDelay;

  public RatingService(MatchDAO matchDao, ScoreDAO scoreDao, RatingConfiguration configuration) {
    this.matchDao = matchDao;
    this.scoreDao = scoreDao;
    
    this.ratingDelay = configuration.getDelay();
  }

  public void calculateNewRatings() {
    List<Match> unratedMatches = matchDao.getUnratedMatches(ratingDelay);
    
    LOG.info("rating {} matches", unratedMatches.size());
    
    try {
      unratedMatches.stream()
          .map(MatchResultDetails::compute)
          .map(this::createChanges)
          .flatMap(changes -> changes.stream())
          .forEach(change -> {
            scoreDao.createChanges(change);
            change.apply();
          });
      
    } catch (Throwable t) {
      LOG.error("rating failed", t);
      throw t;
    }
  }
 
  public List<ScoreChange> createChanges(MatchResultDetails result) {
    
    Match match = result.getMatch();

    ScoreUpdates updates = new ScoreUpdates(match, player -> getScore(match, player));

    if (result.isTied()) {
      
      // add tie updates
      updates.add(Side.TEAM1, ScoreType.TIE, 2);
      updates.add(Side.TEAM2, ScoreType.TIE, 2);
      
      for (Side side : Side.values()) {
        if (result.getCloseLosses(side) > 0) {
          updates.add(side, ScoreType.CLOSE_LOSS, 1);
        }
        
        if (result.getStomps(side) > 0) {
          updates.add(side, ScoreType.STOMP, 2);
        }
      }
    }
    
    for (Side side : Side.values()) {
      
      Team team = match.getTeam(side);
      boolean shortHanded = team.isSingle() && !match.getTeam(side.opposite()).isSingle();
      
      // add win updates
      if (result.isWinner(side)) {
        updates.add(side, ScoreType.WON, 5);
        
        if (result.getStomps(side) > 0) {
          updates.add(side, ScoreType.STOMP, 1);
        }
      }
      // add loss updates
      else {
        updates.add(side, ScoreType.LOST, -1);

        if (result.getCloseLosses(side) > 0) {
          updates.add(side, ScoreType.CLOSE_LOSS, 1);
        }
        
        if (result.getStomps(side) > 0) {
          updates.add(side, ScoreType.STOMP, 2);
        }
      }
      
      if (shortHanded) {
        updates.add(side, ScoreType.SHORT_HANDED, 2);
      }
    }
    
    match.setRated(true);

    LOG.info("rated Match#{}", match.getId());
      
    return updates.asList();
  }

  public Score getScore(Match match, Player player) {
    return scoreDao.getOrCreateScore(player);
  }
}
