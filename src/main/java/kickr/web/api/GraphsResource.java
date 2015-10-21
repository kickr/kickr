/*
 * The MIT License
 *
 * Copyright 2015 nikku.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package kickr.web.api;

import io.dropwizard.hibernate.UnitOfWork;
import java.time.Instant;
import java.time.Period;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.security.RolesAllowed;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import kickr.db.dao.ScoreDAO;
import kickr.db.entity.Player;
import kickr.db.entity.Score;
import kickr.web.model.timeline.TimelineResult;
import kickr.web.model.timeline.TimelineResults;
import kickr.web.view.graphs.GraphsView;
import support.web.api.AbstractResource;

/**
 *
 * @author nikku
 */
@Path("graphs")
public class GraphsResource extends AbstractResource {

  private final ScoreDAO scoreDao;

  public GraphsResource(ScoreDAO scoreDao) {
    this.scoreDao = scoreDao;
  }

  List<String> existingTabs = Arrays.asList("scores", "performance", "matches");

  @GET
  @RolesAllowed("user")
  public GraphsView get(@QueryParam("tab") @DefaultValue("scores") String tab) {

    if (!existingTabs.contains(tab)) {
      throw new NotFoundException("non-existing tab");
    }

    return createView(GraphsView.class).withTab(tab);
  }
  
  @GET
  @Path("timeline")
  @RolesAllowed("user")
  @Produces(MediaType.APPLICATION_JSON)
  @UnitOfWork
  public TimelineResults getData(@QueryParam("month") @DefaultValue("9") int month) {

    Instant end = Instant.now();

    int weeks = month * 4;

    Instant start = end.minus(Period.ofWeeks(weeks));

    Map<Player, List<TimelineResult>> playerScores = new HashMap<>();

    for (int i = 0; i < weeks; i++) {
      Instant currentDate = start.plus(Period.ofWeeks((i + 1)));

      List<Score> monthScores = scoreDao.getScores(Date.from(currentDate));

      for (Score score: monthScores) {
        Player player = score.getPlayer();

        List<TimelineResult> playerResults = playerScores.computeIfAbsent(player, p -> new ArrayList<>());

        if (playerResults.isEmpty() && i > 0) {
          playerResults.add(new TimelineResult(0, currentDate.minus(Period.ofWeeks(1)).toEpochMilli()));
        }

        playerResults.add(new TimelineResult(score.getValue(), currentDate.toEpochMilli()));
      }

    }

    return TimelineResults.fromScores(Date.from(start), month, playerScores);
  }
}
