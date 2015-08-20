package kickr.core.api;

import kickr.web.api.MatchResource;
import static io.dropwizard.testing.FixtureHelpers.*;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.Test;
import io.dropwizard.testing.junit.ResourceTestRule;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import kickr.db.FoosballTableDAO;
import kickr.db.GameDAO;
import kickr.db.MatchDAO;
import kickr.db.PlayerDAO;
import kickr.db.entity.FoosballTable;
import kickr.db.entity.Match;
import kickr.db.entity.Player;
import kickr.db.entity.user.User;
import kickr.service.MatchService;
import org.glassfish.jersey.server.filter.RolesAllowedDynamicFeature;
import org.junit.Before;
import org.junit.ClassRule;
import static org.mockito.Mockito.*;
import static kickr.security.Constants.*;
import support.security.SecurityContextFactory;
import support.security.SecurityContextInitializer;
import support.security.auth.AuthFactory;


/**
 *
 * @author nikku
 */
public class MatchResourceTest {

  private static final MatchDAO matchDao = mock(MatchDAO.class);
  
  private static final PlayerDAO playerDao = mock(PlayerDAO.class);
  
  private static final GameDAO gameDao = mock(GameDAO.class);
  
  private static final FoosballTableDAO tableDao = mock(FoosballTableDAO.class);

  private static final MatchService matchService = new MatchService(matchDao, gameDao, playerDao, tableDao);

  private static final SecurityContextFactory<User> securityContextFactory = mock(SecurityContextFactory.class);

  @ClassRule
  public static final ResourceTestRule resources = ResourceTestRule.builder()
          .addResource(new MatchResource(matchService, matchDao))
          .addProvider(new SecurityContextInitializer(securityContextFactory))
          .addProvider(new RolesAllowedDynamicFeature())
          .addProvider(AuthFactory.binder(new AuthFactory<>(User.class)))
          .build();

  @Before
  public void setup() {
    reset(matchDao, playerDao, gameDao, tableDao, securityContextFactory);
  }

  @Test
  public void shouldAddMatch() throws Exception {

    when(securityContextFactory.createSecurityContext(any())).thenReturn(loggedIn(user("walter")));

    when(tableDao.findTableById(1l)).thenReturn(new FoosballTable("test"));
    when(playerDao.findPlayerByAlias(any())).thenAnswer((invokation) -> {
      return new Player(invokation.getArgumentAt(0, String.class), null, null);
    });

    // given
    String matchDataJSON = fixture("fixtures/add-match.json");
    Entity<String> matchDataEntity = Entity.entity(matchDataJSON, MediaType.APPLICATION_JSON);

    // when
    Response response = resources.client().target("/matches")
                                          .request()
                                          .post(matchDataEntity);

    // then
    assertThat(response.getStatus()).isEqualTo(204);
    
    verify(matchDao).create(any(Match.class));
  }
}
