package kickr.web.api;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import support.web.api.AbstractResource;
import kickr.web.view.user.SignUpView;

/**
 *
 * @author nikku
 */
@Path("user")
public class UserResource extends AbstractResource {

  @GET
  @Path("signup")
  public SignUpView signupForm() {
    return createView(SignUpView.class);
  }
}
