package kickr.core.web.model;

import kickr.web.model.match.CoreMatchData;
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
    String matchDataJSON = fixture("fixtures/json/add-match.json");

    // when
    CoreMatchData matchData = MAPPER.readValue(matchDataJSON, CoreMatchData.class);

    // then
    assertThat(matchData.getGames()).hasSize(2);
    
    assertThat(matchData.getResult()).isNull();

    assertThat(matchData.getTable()).isNotNull();

    assertThat(matchData.getTeam1().getDefense()).isNotNull();
    assertThat(matchData.getTeam1().getOffense()).isNotNull();
    assertThat(matchData.getTeam2().getDefense()).isNotNull();
    assertThat(matchData.getTeam2().getOffense()).isNotNull();
  }
}
