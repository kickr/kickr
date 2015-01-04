package support.security.auth;

import java.security.Principal;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.SecurityContext;
import org.glassfish.hk2.api.Factory;
import support.security.annotation.Auth;
import support.security.auth.AuthFactoryProvider.Binder;

/**
 * @param <P>
 *
 * @author nikku
 */
public class AuthFactory<P extends Principal> implements Factory<P> {

  private final Class<P> principalClass;
  
  @Context
  private SecurityContext securityContext;

  public AuthFactory(Class<P> principalClass) {
    this.principalClass = principalClass;
  }

  public Class<P> getPrincipalClass() {
    return principalClass;
  }

  @Override
  public P provide() {
    Principal principal = securityContext.getUserPrincipal();
    return principalClass.cast(principal);
  }

  @Override
  public void dispose(P instance) { }


  /**
   * Static utility to setup injection of the user principal into
   * {@link Auth} annotated resource method parameter.
   * 
   * @param <P>
   * @param authFactory
   *
   * @return
   */
  public static <P extends Principal> Binder<P> binder(AuthFactory<P> authFactory) {
    return new AuthFactoryProvider.Binder(authFactory);
  }
}
