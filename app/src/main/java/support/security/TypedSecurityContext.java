package support.security;

import java.security.Principal;
import javax.ws.rs.core.SecurityContext;

/**
 *
 * @author nikku
 * 
 * @param <T>
 */
public abstract class TypedSecurityContext<T extends Principal> implements SecurityContext {

  private final boolean secure;
  private final String authenticationScheme;
  
  private final T user;

  public TypedSecurityContext(boolean secure, String authenticationScheme, T user) {

    this.secure = secure;
    this.authenticationScheme = authenticationScheme;
    this.user = user;
  }

  @Override
  public T getUserPrincipal() {
    return user;
  }

  @Override
  public boolean isSecure() {
    return secure;
  }

  @Override
  public String getAuthenticationScheme() {
    return authenticationScheme;
  }
}