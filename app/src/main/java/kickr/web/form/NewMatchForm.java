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
package kickr.web.form;

import java.util.Date;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import org.hibernate.validator.constraints.NotBlank;

/**
 *
 * @author nikku
 */
public class NewMatchForm extends Form<NewMatchForm> {

  public static class Team {

    @NotBlank
    private String offense;

    private String defense;

    public String getOffense() {
      return offense;
    }

    public String getDefense() {
      return defense;
    }
  }

  public static class Table {

    @NotNull
    private Long id;

    private String name;

    private String team1Alias;

    private String team2Alias;

    public Table() { }

    public Table(Long id, String name, String team1Alias, String team2Alias) {
      this.id = id;
      this.name = name;
      this.team1Alias = team1Alias;
      this.team2Alias = team2Alias;
    }

    public Long getId() {
      return id;
    }

    public String getName() {
      return name;
    }

    public String getTeam1Alias() {
      return team1Alias;
    }

    public String getTeam2Alias() {
      return team2Alias;
    }

  }

  @Valid
  @NotNull
  private Table table;

  @Valid
  @NotNull
  private Team team1;

  @Valid
  @NotNull
  private Team team2;

  @NotBlank
  @Pattern(regexp = GamesUtil.GAMES_PATTERN_STRING)
  private String games;
  
  private Date played;

  @Override
  public String toString() {
    return team1.offense + "," + team1.defense +
              " vs " +
           team2.offense + "," + team2.defense + ";" +
              " played at " + played + ";" +
              " games: " + games;
  }

  public Team getTeam1() {
    return team1;
  }

  public Team getTeam2() {
    return team2;
  }

  public String getGames() {
    return games;
  }

  public Date getPlayed() {
    return played;
  }

  public NewMatchForm withTable(Table table) {
    return chain(() -> this.table = table);
  }

  public Table getTable() {
    return table;
  }
}
