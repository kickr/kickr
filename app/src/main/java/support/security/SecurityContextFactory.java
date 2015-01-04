package support.security;

import java.security.Principal;
import javax.servlet.http.HttpServletRequest;

/**
 * @author nikku
 *
 * @param <P> principal
 */
public abstract class SecurityContextFactory<P extends Principal> {
  
  /**
   * Create a security context for the given request.
   * 
   * @param request
   * @return
   */
  protected abstract TypedSecurityContext<P> createSecurityContext(HttpServletRequest request);
}
