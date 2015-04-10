package support.security;

import java.io.IOException;
import java.security.Principal;
import javax.annotation.Priority;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Priorities;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.ext.Provider;

/**
 *
 * @param <P>
 *
 * @author nikku
 */
@Provider
@Priority(Priorities.AUTHENTICATION)
public class SecurityContextInitializer<P extends Principal> implements ContainerRequestFilter {

  @Inject
  private HttpServletRequest request;

  private final SecurityContextFactory securityContextFactory;

  public SecurityContextInitializer(SecurityContextFactory<P> securityContextFactory) {
    this.securityContextFactory = securityContextFactory;
  }

  @Override
  public void filter(ContainerRequestContext requestContext) throws IOException {

    try {
      SecurityContext securityContext = securityContextFactory.createSecurityContext(request);
      requestContext.setSecurityContext(securityContext);
    } catch (WebApplicationException ex) {
      requestContext.abortWith(ex.getResponse());
    }
  }
}
