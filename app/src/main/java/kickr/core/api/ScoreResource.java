package kickr.core.api;

import kickr.core.model.PlayerStatisticData;
import io.dropwizard.hibernate.UnitOfWork;
import java.util.List;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import kickr.core.model.LeaderBoardData;
import kickr.core.model.ScoreData;
import kickr.db.ScoreDAO;
import kickr.db.entity.PlayerStatistics;
import kickr.db.entity.ScoreWithChanges;

/**
 *
 * @author nikku
 */
@Path("score")
public class ScoreResource extends BaseResource {
  
  private final ScoreDAO scoreDao;

  public ScoreResource(ScoreDAO scoreDao) {
    this.scoreDao = scoreDao;
  }

  @GET
  @UnitOfWork
  public LeaderBoardData getLeaderBoard() {

    List<ScoreWithChanges> scores = scoreDao.getScoresWithChanges(0, 5);

    List<PlayerStatistics> playerStatistics = scoreDao.getStatistics(0, 5);

    return new LeaderBoardData(
        ScoreData.fromScores(scores),
        PlayerStatisticData.fromStatistics(playerStatistics));
  }
}
