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
package kickr.web.view.match;

import java.util.List;
import kickr.web.model.match.MatchData;

/**
 *
 * @author nikku
 */
public class MatchesView extends BaseMatchesView<MatchesView> {
  
  private String filter;
  
  private String search;

  private int page = 1;

  public MatchesView() {
    super("list.ftl");
  }

  public MatchesView withFilter(String filter) {
    return chain(() -> this.filter = filter);
  }

  public MatchesView withSearch(String search) {
    return chain(() -> this.search = search);
  }
  
  public MatchesView withPage(int page) {
    return chain(() -> this.page = page);
  }

  public String getFilter() {
    return filter;
  }

  public String getSearch() {
    return search;
  }

  public int getPage() {
    return page;
  }

}
