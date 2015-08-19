package kickr.web;

import java.net.URI;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import kickr.web.view.BaseView;

/**
 *
 * @author nikku
 */
public class BaseResource {

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
  
  protected <T extends BaseView> T populateView(T view) {
    return view;
  }
}
