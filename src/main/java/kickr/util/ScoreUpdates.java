package kickr.util;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import kickr.db.entity.Match;
import kickr.db.entity.Player;
import kickr.db.entity.Score;
import kickr.db.entity.ScoreChange;
import kickr.db.entity.ScoreType;
import kickr.db.entity.Team;

/**
 *
 * @author nikku
 */
public class ScoreUpdates {
  
  private final Match match;
  
  private final ArrayList<ScoreChange> updates;
  
  private final Function<Player, Score> fetchScore;

  public ScoreUpdates(Match match, Function<Player, Score> fetchScore) {
    this.match = match;
    this.fetchScore = fetchScore;
    
    this.updates = new ArrayList<>();
  }

  public void add(Side side, ScoreType type, int value) {
    Team team = match.getTeam(side);
    
    Player offense = team.getOffense();
    
    add(offense, type, value);
    
    if (!team.isSingle()) {
      Player defense = team.getDefense();
      add(defense, type, value);
    }
  }

  protected void add(Player player, ScoreType type, int value) {
    updates.add(new ScoreChange(type, value, player, match, fetchScore.apply(player)));
  }

  public List<ScoreChange> asList() {
    return updates;
  }
}
