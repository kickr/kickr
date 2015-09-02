package kickr.web.api;

import io.dropwizard.hibernate.UnitOfWork;
import java.net.URI;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import kickr.web.view.IndexView;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response;
import kickr.db.entity.user.AccessToken;
import kickr.db.entity.user.User;
import kickr.security.service.AuthenticationService;
import support.web.api.AbstractResource;
import kickr.web.form.LoginForm;
import kickr.web.view.LoginView;
import support.form.FormData;
import support.security.AuthenticationException;
import support.security.annotation.Auth;

/**
 *
 * @author nikku
 */
@Path("/")
public class RootResource extends AbstractResource {

  private final AuthenticationService authenticationService;

  public RootResource(AuthenticationService authenticationService) {
    this.authenticationService = authenticationService;
  }

  @GET
  public IndexView index() {
    return createView(IndexView.class);
  }


  @GET
  @Path(Paths.LOGIN_PATH)
  @UnitOfWork
  public LoginView loginForm(@QueryParam("redirectTo") String redirectTo) {
    return new LoginView().redirectTo(redirectTo);
  }

  @POST
  @Path(Paths.LOGIN_PATH)
  @UnitOfWork
  public Response login(@FormData LoginForm loginForm) {

    String name = loginForm.name;
    String password = loginForm.password;
    String redirectTo = loginForm.redirectTo;
    Boolean rememberMe = loginForm.rememberMe;

    try {
      AccessToken token = authenticationService.authenticate(name, password, !rememberMe);

      Date validUntil = token.getValidUntil();

      long maxAge = 365 * 24 * 60 * 60; // one year;
      if (validUntil != null) {
        maxAge = Instant.now().until(validUntil.toInstant(), ChronoUnit.SECONDS);
      }

      if (redirectTo == null || redirectTo.isEmpty()) {
        redirectTo = "/?hello=1";
      }

      NewCookie userCookie = new NewCookie("__sid", token.getValue(), "/", null, null, (int) maxAge, false, true);

      return redirect(redirectTo).cookie(userCookie).build();

    } catch (AuthenticationException ex) {

      LoginView signinView = createView(LoginView.class).redirectTo(redirectTo).addError("Invalid credentials");

      return unauthorized().entity(signinView).build();
    }
  }

  @POST
  @Path(Paths.LOGOUT_PATH)
  @UnitOfWork
  public Response logout(@Auth User user, @QueryParam("token") String token) {
    authenticationService.unauthenticate(user, token);

    NewCookie removeUserCookie = new NewCookie("__sid", "", "/", null, null, -1, false, true);

    return Response.seeOther(URI.create("/")).cookie(removeUserCookie).build();
  }
}
