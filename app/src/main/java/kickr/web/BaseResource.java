package kickr.web;

import java.net.URI;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import kickr.db.entity.user.User;
import kickr.web.view.BaseView;

/**
 *
 * @author nikku
 */
public class BaseResource {

  @Context
  private SecurityContext securityContext;

  @Context
  private HttpServletRequest request;

  protected void assertValidPagination(int firstResult, int maxResults) {
    if (firstResult > 0 || maxResults < 1) {
      throw new WebApplicationException("Bad pagination data", Response.Status.BAD_REQUEST);
    }
  }

  protected Response.ResponseBuilder redirect(String uri) {
    return Response.temporaryRedirect(URI.create(uri));
  }

  protected Response.ResponseBuilder unauthorized() {
    return Response.status(Response.Status.UNAUTHORIZED);
  }
  
  protected <T extends BaseView> T createView(Class<T> cls) {
    boolean layout = request.getHeader("X-PJAX") == null;

    try {
      T view = (T) cls.newInstance();

      return (T) view.useLayout(layout).withUser((User) securityContext.getUserPrincipal());
    } catch (InstantiationException | IllegalAccessException e) {
      throw new IllegalArgumentException("Failed to instantiate view", e);
    }
  }
}
