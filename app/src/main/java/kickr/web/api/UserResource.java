package kickr.web.api;

import io.dropwizard.hibernate.UnitOfWork;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response;
import kickr.db.entity.user.AccessToken;
import kickr.db.entity.user.User;
import kickr.security.service.AuthenticationService;
import kickr.web.BaseResource;
import kickr.web.view.user.LoginView;
import kickr.web.view.user.SignupView;
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
  public LoginView loginForm(@QueryParam("redirectTo") String redirectTo) {
    
    return new LoginView().redirectTo(redirectTo);

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
  public Response login(
      @QueryParam("redirectTo") String redirectTo,
      MultivaluedMap<String, String> loginData) {

    String name = loginData.getFirst("name");
    String password = loginData.getFirst("password");
    Boolean rememberMe = Boolean.parseBoolean(loginData.getFirst("rememberMe"));

    if (redirectTo == null || redirectTo.isEmpty()) {
      redirectTo = "/?hello=1";
    }

    try {
      AccessToken token = authenticationService.authenticate(name, password, !rememberMe);

      Date validUntil = token.getValidUntil();

      long maxAge = 365 * 24 * 60 * 60; // one year;
      if (validUntil != null) {
        maxAge = Instant.now().until(validUntil.toInstant(), ChronoUnit.SECONDS);
      }
      
      NewCookie loginCookie = new NewCookie("__token", token.getValue(), "/", null, null, (int) maxAge, false, true);
      
      return redirect(redirectTo).cookie(loginCookie).build();

    } catch (AuthenticationException ex) {

      LoginView loginView = populateView(new LoginView().redirectTo(redirectTo).addError("Invalid credentials"));

      return unauthorized().entity(loginView).build();
    }
  }

  @GET
  @Path("logout")
  @UnitOfWork
  public void logout(@Auth User user, @QueryParam("token") String token) {
    authenticationService.unauthenticate(user, token);
  }

  @GET
  @Path("signup")
  public SignupView signupForm() {
    return new SignupView();
  }
}
