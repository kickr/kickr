package kickr.web.api;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import kickr.web.BaseResource;
import kickr.web.view.user.SignUpView;

/**
 *
 * @author nikku
 */
@Path("user")
public class UserResource extends BaseResource {

  @GET
  @Path("signup")
  public SignUpView signupForm() {
    return createView(SignUpView.class);
  }
}
