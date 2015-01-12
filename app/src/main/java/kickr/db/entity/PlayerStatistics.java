package kickr.db.entity;

/**
 *
 * @author nikku
 */
public class PlayerStatistics {

  private final Player player;
  private final long score;
  private final long games;

  public PlayerStatistics(Player player, Long score, Long games) {
    this.player = player;

    this.score = score;
    this.games = games;
  }

  public long getGames() {
    return games;
  }

  public Player getPlayer() {
    return player;
  }

  public long getScore() {
    return score;
  }
}
