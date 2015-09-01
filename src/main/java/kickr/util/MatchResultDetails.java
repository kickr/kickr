package kickr.util;

import java.util.EnumMap;
import java.util.Map;
import kickr.db.entity.Game;
import kickr.db.entity.Match;

import static kickr.util.Side.*;

/**
 *
 * @author nikku
 */
public class MatchResultDetails {
  
  private static final int CLOSE_DIFFERENCE = 2;
  
  private static final int STOMP_MIN_SUM = 7;
  private static final int STOMP_MAX_LOSS_GOALS = 1;

  private final Match match;
  
  private final Map<Side, Integer> wins;
  
  private final Map<Side, Integer> goals;
  
  private final Map<Side, Integer> stomps;
  
  private final Map<Side, Integer> closeLosses;

  private int totalGames = 0;
  
  private int ties = 0;
  
  public MatchResultDetails(Match match) {
    this.match = match;
    
    this.wins = new EnumMap<>(Side.class);
    this.goals = new EnumMap<>(Side.class);
    this.stomps = new EnumMap<>(Side.class);
    this.closeLosses = new EnumMap<>(Side.class);
  }
  
  public int getTies() {
    return ties;
  }
  
  public int getTotalGames() {
    return totalGames;
  }

  public int getWins(Side side) {
    return wins.getOrDefault(side, 0);
  }
  
  public boolean isTied() {
    return !isWinner(TEAM1) && !isWinner(TEAM2);
  }
  
  public boolean isWinner(Side side) {
    return getWins(side) > getWins(side.opposite());
  }
  
  public int getCloseLosses(Side side) {
    return closeLosses.getOrDefault(side, 0);
  }
  
  public int getStomps(Side side) {
    return stomps.getOrDefault(side, 0);
  }

  public Match getMatch() {
    return match;
  }
  
  protected void add(Game game) {

    Integer scoreTeam1 = game.getScoreTeam1();
    Integer scoreTeam2 = game.getScoreTeam2();
    
    Integer difference = Math.abs(scoreTeam1 - scoreTeam2);
    Integer sum = scoreTeam1 + scoreTeam2;
    
    addGoals(TEAM1, scoreTeam1);
    addGoals(TEAM2, scoreTeam2);
    
    totalGames++;

    Side winner = null;
    
    if (scoreTeam1 > scoreTeam2) {
      winner = TEAM1;
    }
    
    if (scoreTeam1 < scoreTeam2) {
      winner = TEAM2;
    }
    
    if (winner != null) {
      addWin(winner);
      
      if (difference <= CLOSE_DIFFERENCE) {
        addCloseLoss(winner.opposite());
      }
      
      if (sum > STOMP_MIN_SUM && sum - difference <= STOMP_MAX_LOSS_GOALS) {
        addStomp(winner);
      }
    } else {
      ties++;
    }
  }
  
  private void addGoals(Side side, Integer sideGoals) {
    goals.merge(side, sideGoals, Integer::sum);
  }

  private void addWin(Side side) {
    wins.merge(side, 1, Integer::sum);
  }
  
  private void addCloseLoss(Side side) {
    closeLosses.merge(side, 1, Integer::sum);
  }

  private void addStomp(Side side) {
    stomps.merge(side, 1, Integer::sum);
  }
  
  // static helpers 

  public static MatchResultDetails compute(Match match) {
    MatchResultDetails matchResultDetails = new MatchResultDetails(match);
    
    match.getGames().stream().forEach(matchResultDetails::add);
    
    return matchResultDetails;
  }
}
