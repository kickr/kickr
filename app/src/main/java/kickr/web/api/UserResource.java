package kickr.web.api;

import kickr.web.BaseResource;
import io.dropwizard.hibernate.UnitOfWork;
import javax.ws.rs.DELETE;
import kickr.web.model.user.LoginData;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import kickr.web.model.user.UserData;
import kickr.db.entity.user.AccessToken;
import kickr.db.entity.user.User;
import kickr.security.service.AuthenticationService;
import kickr.web.BaseResource;
import kickr.web.view.LoginView;
import support.security.AuthenticationException;
import support.security.annotation.Auth;

/**
 *
 * @author nikku
 */
@Path("user")
public class UserResource extends BaseResource {
  
  private final AuthenticationService authenticationService;

  public UserResource(AuthenticationService authenticationService) {
    this.authenticationService = authenticationService;
  }

  @GET
  @Path("login")
  @UnitOfWork
  public LoginView get(@QueryParam("token") String token) {
    
    return new LoginView();

    /*
    try {
      User user = authenticationService.authenticate(token);
      return new UserData(user.getName(), user.getPermissions(), null);
    } catch (AuthenticationException ex) {
      throw unauthorized(ex);
    }*/
  }

  @POST
  @Path("login")
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

  @GET
  @Path("logout")
  @UnitOfWork
  public void logout(@Auth User user, @QueryParam("token") String token) {
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
