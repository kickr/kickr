package kickr.core.api;

import io.dropwizard.hibernate.UnitOfWork;
import javax.ws.rs.DELETE;
import kickr.core.model.user.LoginData;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import kickr.core.model.user.UserData;
import kickr.db.entity.user.AccessToken;
import kickr.db.entity.user.User;
import kickr.security.service.AuthenticationService;
import support.security.AuthenticationException;
import support.security.annotation.Auth;

/**
 *
 * @author nikku
 */
@Path("auth")
public class AuthResource extends BaseResource {
  
  private final AuthenticationService authenticationService;

  public AuthResource(AuthenticationService authenticationService) {
    this.authenticationService = authenticationService;
  }

  @GET
  @UnitOfWork
  public UserData get(@QueryParam("token") String token) {
    
    try {
      User user = authenticationService.authenticate(token);
      return new UserData(user.getName(), user.getPermissions(), null);
    } catch (AuthenticationException ex) {
      throw unauthorized(ex);
    }
  }

  @POST
  @UnitOfWork
  public UserData create(LoginData loginData) {

    try {
      AccessToken token = authenticationService.authenticate(loginData.getName(), loginData.getPassword(), !loginData.getRememberMe());
      User user = token.getUser();

      return new UserData(user.getName(), user.getPermissions(), token.getValue());
    } catch (AuthenticationException ex) {
      throw unauthorized(ex);
    }
  }

  @DELETE
  @UnitOfWork
  public void delete(@Auth User user, @QueryParam("token") String token) {
    authenticationService.unauthenticate(user, token);
  }

  private WebApplicationException unauthorized(AuthenticationException ex) {
    return new WebApplicationException("Authentication failed",
      Response
        .status(Response.Status.UNAUTHORIZED)
        .entity("authentication failed")
        .build());
  }
}
