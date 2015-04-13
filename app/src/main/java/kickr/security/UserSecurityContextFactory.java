package kickr.security;

import kickr.security.service.AuthenticationService;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import kickr.db.entity.user.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import support.security.AuthenticationException;
import support.security.SecurityContextFactory;
import support.security.TypedSecurityContext;
import support.transactional.WithTransaction;

/**
 *
 * @author nikku
 */
public class UserSecurityContextFactory extends SecurityContextFactory<User> {

  private static final Logger LOGGER = LoggerFactory.getLogger(UserSecurityContextFactory.class);
  private static final String SCHEME = "Token";
  private static final String CHALLENGE_FORMAT = SCHEME + " realm=\"%s\"";

  private final String realm;

  private final WithTransaction transactional;

  private final AuthenticationService authenticationService;
  
  public UserSecurityContextFactory(
        String realm,
        WithTransaction transactional,
        AuthenticationService authenticationService) {

    this.realm = realm;
    this.transactional = transactional;
    this.authenticationService = authenticationService;
  }

  @Override
  public TypedSecurityContext<User> createSecurityContext(HttpServletRequest request) {

    if (request == null) {
      return null;
    }

    User principal = null;

    TokenCredentials token = extractAccessToken(request);

    // if token is provided, we must be able to authenticate
    if (token != null) {
      try {
        principal = authenticate(token);
      } catch (AuthenticationException ex) {
        
        LOGGER.warn("Failed to authenticate <" + token + ">");

        // indicate authentication failure
        throw new WebApplicationException(
              "Failed to authenticate",
              Response
                .status(Response.Status.UNAUTHORIZED)
                .header(HttpHeaders.WWW_AUTHENTICATE, String.format(CHALLENGE_FORMAT, realm))
                .entity("Invalid credentials")
                .build());
      }
    }

    return createContext(request.isSecure(), SCHEME, principal);
  }

  protected TokenCredentials extractAccessToken(HttpServletRequest request) {

    final String header = request.getHeader(HttpHeaders.AUTHORIZATION);

    if (header != null) {
      final int space = header.indexOf(" ");
      if (space > 0) {
        final String method = header.substring(0, space);
        if (SCHEME.equalsIgnoreCase(method)) {
          return new TokenCredentials(header.substring(space + 1), request.getRemoteAddr());
        }
      }
    }

    return null;
  }

  protected User authenticate(TokenCredentials credentials) {
    return transactional.get(() -> {
      return authenticationService.authenticate(credentials.token);
    });
  }

  private TypedSecurityContext<User> createContext(boolean secure, String scheme, User principal) {
    return new UserSecurityContext(secure, scheme, principal);
  }

  
  protected static class TokenCredentials {

    public final String token;
    public final String address;

    public TokenCredentials(String token, String address) {
      this.token = token;
      this.address = address;
    }
  }

}
