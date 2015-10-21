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
package kickr.web.model.timeline;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import kickr.db.entity.Player;
import kickr.web.model.PlayerData;

/**
 *
 * @author nikku
 */
public class TimelineResults {

  private final List<TimelineSeries> series;
  
  private final Date start;
  private final long month;

  private TimelineResults(Date start, long month, ArrayList<TimelineSeries> series) {
    this.start = start;
    this.month = month;
    this.series = series;
  }

  public Date getStart() {
    return start;
  }

  public long getMonth() {
    return month;
  }

  public List<TimelineSeries> getSeries() {
    return series;
  }


  /////// static helpers /////////////////////////////////

  public static TimelineResults fromScores(Date start, long month, Map<Player, List<TimelineResult>> playerScores) {

    ArrayList<TimelineSeries> series = new ArrayList<>();

    playerScores.forEach((p, results) -> {
      series.add(new TimelineSeries(new PlayerData(p.getAlias(), null, p.getName()), results));
    });

    return new TimelineResults(start, month, series);
  }
}
