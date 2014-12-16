package kickr.util;

/**
 *
 * @author nikku
 */
public enum Side {
  TEAM1,
  TEAM2;
  
  public Side opposite() {
    if (this == TEAM1) {
      return TEAM2;
    } else {
      return TEAM1;
    }
  }
}
