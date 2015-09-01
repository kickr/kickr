package kickr.security.service;

import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import kickr.db.dao.AccessTokenDAO;
import kickr.db.dao.UserDAO;
import kickr.db.entity.user.AccessToken;
import kickr.db.entity.user.User;
import support.security.AuthenticationException;

/**
 *
 * @author nikku
 */
public class AuthenticationService {

  private static final Duration SESSION_DURATION = Duration.ofMinutes(60);

  private final AccessTokenDAO accessTokenDao;
  private final CredentialsService credentialsService;
  private final UserDAO userDao;

  public AuthenticationService(CredentialsService credentialsService, UserDAO userDao, AccessTokenDAO accessTokenDao) {
    this.credentialsService = credentialsService;

    this.userDao = userDao;
    this.accessTokenDao = accessTokenDao;
  }

  public User authenticate(String token) {

    AccessToken accessToken = accessTokenDao.get(token);

    if (accessToken != null) {

      Date validUntil = accessToken.getValidUntil();

      // update valid access token, otherwise delete invalid
      if (validUntil == null || validUntil.after(new Date())) {

        if (validUntil != null) {
          limitValidity(accessToken);
        }

        return accessToken.getUser();
      } else {
        accessTokenDao.remove(accessToken);
      }
    }

    throw authenticationFailed();
  }

  public AccessToken authenticate(String name, String password, boolean limited) {

    User user = userDao.getByName(name);

    if (user != null) {
      if (credentialsService.matches(password, user.getPassword())) {
        return createAccessToken(user, limited);
      }
    }

    throw authenticationFailed();
  }

  protected AccessToken createAccessToken(User user, boolean limited) {
    String sessionToken = credentialsService.createSessionToken(user);

    AccessToken accessToken = new AccessToken(sessionToken, user);
    
    if (limited) {
      limitValidity(accessToken);
    }

    accessTokenDao.create(accessToken);

    return accessToken;
  }

  private AuthenticationException authenticationFailed() {
    return new AuthenticationException("Failed to authenticate");
  }

  public void unauthenticate(User user, String token) {
    if (token == null) {
      accessTokenDao.removeByUser(user);
    } else {
      accessTokenDao.removeByToken(user, token);
    }
  }

  private void limitValidity(AccessToken accessToken) {
    accessToken.setValidUntil(Date.from(Instant.now().plus(SESSION_DURATION)));
  }
}
