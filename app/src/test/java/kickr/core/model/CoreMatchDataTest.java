package kickr.core.model;

import static io.dropwizard.testing.FixtureHelpers.*;
import static org.assertj.core.api.Assertions.assertThat;
import io.dropwizard.jackson.Jackson;
import org.junit.Test;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 *
 * @author nikku
 */
public class CoreMatchDataTest {

  private static final ObjectMapper MAPPER = Jackson.newObjectMapper();

  @Test
  public void shouldDeserializesAddMatchData() throws Exception {

    // given
    String matchDataJSON = fixture("fixtures/add-match.json");

    // when
    CoreMatchData matchData = MAPPER.readValue(matchDataJSON, CoreMatchData.class);

    TeamsData teamsData = matchData.getTeams();

    // then
    assertThat(matchData.games).hasSize(2);
    
    assertThat(matchData.getResult()).isNull();

    assertThat(matchData.getTable()).isNotNull();

    assertThat(teamsData).isNotNull();

    assertThat(teamsData.getTeam1().getDefense()).isNotNull();
    assertThat(teamsData.getTeam1().getOffense()).isNotNull();
    assertThat(teamsData.getTeam2().getDefense()).isNotNull();
    assertThat(teamsData.getTeam2().getOffense()).isNotNull();
  }
}
