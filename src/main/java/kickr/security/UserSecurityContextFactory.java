package kickr.security;

import java.util.stream.Stream;
import javax.servlet.http.Cookie;
import kickr.security.service.AuthenticationService;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.NotAuthorizedException;
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

    String address = request.getRemoteAddr();
    boolean secure = request.isSecure();

    String headerToken = extractHeaderToken(request);

    if (headerToken != null) {
      // if header token is provided,
      // we must be able to authenticate
      TokenCredentials headerCredentials = new TokenCredentials(headerToken, address);
      try {
        // create context based on header token auth
        return createContext(secure, SCHEME, authenticate(headerCredentials));
      } catch (AuthenticationException ex) {

        LOGGER.warn("failed to authenticate <" + headerCredentials + ">");

        // indicate authentication failure
        throw new NotAuthorizedException(
              "failed to authenticate",
              Response
                .status(Response.Status.UNAUTHORIZED)
                .header(HttpHeaders.WWW_AUTHENTICATE, String.format(CHALLENGE_FORMAT, realm))
                .entity("invalid credentials")
                .build());
      }
    }

    String cookieToken = extractCookieToken(request);

    if (cookieToken != null) {

      // if cookie token is provided,
      // authentication is optional
      TokenCredentials cookieCredentials = new TokenCredentials(cookieToken, address);
      try {
        // create context based on header token auth
        return createContext(secure, SCHEME, authenticate(cookieCredentials));
      } catch (AuthenticationException ex) {
        LOGGER.warn("failed to authenticate <" + cookieCredentials + ">");
      }
    }
    
    return createContext(secure, SCHEME, null);
  }

  protected String extractHeaderToken(HttpServletRequest request) {

    final String header = request.getHeader(HttpHeaders.AUTHORIZATION);

    if (header != null) {
      final int space = header.indexOf(" ");
      if (space > 0) {
        final String method = header.substring(0, space);
        if (SCHEME.equalsIgnoreCase(method)) {
          return header.substring(space + 1);
        }
      }
    }

    return null;
  }

  protected String extractCookieToken(HttpServletRequest request) {

    Cookie[] cookies = request.getCookies();

    if (cookies != null) {
      return Stream.of(cookies)
                .filter(c -> "__sid".equals(c.getName()))
                .map(c -> c.getValue())
                .findAny()
                .orElse(null);
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
