package kickr.web.api;

import kickr.web.model.PlayerStatisticData;
import io.dropwizard.hibernate.UnitOfWork;
import java.util.List;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import kickr.web.model.LeaderBoardData;
import kickr.web.model.ScoreData;
import kickr.db.dao.ScoreDAO;
import kickr.db.entity.PlayerStatistics;
import kickr.db.entity.ScoreWithChanges;
import support.web.api.AbstractResource;
import kickr.web.view.ScoreLeaderboardView;

/**
 *
 * @author nikku
 */
@Path("score")
public class ScoreResource extends AbstractResource {
  
  private final ScoreDAO scoreDao;

  public ScoreResource(ScoreDAO scoreDao) {
    this.scoreDao = scoreDao;
  }

  @GET
  @UnitOfWork
  @Path("leaderboard")
  public ScoreLeaderboardView getLeaderBoard() {

    List<ScoreWithChanges> scores = scoreDao.getScoresWithChanges(0, 5);

    List<PlayerStatistics> playerStatistics = scoreDao.getStatistics(0, 5);

    return new ScoreLeaderboardView(new LeaderBoardData(
        ScoreData.fromScores(scores),
        PlayerStatisticData.fromStatistics(playerStatistics)));
  }
}
