package kickr.web;

import java.net.URI;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

/**
 *
 * @author nikku
 */
public class BaseResource extends ViewProvider {

  protected void assertValidPagination(int firstResult, int maxResults) {
    if (firstResult > 0 || maxResults < 1) {
      throw new WebApplicationException("Bad pagination data", Response.Status.BAD_REQUEST);
    }
  }

  protected Response.ResponseBuilder redirect(String uri) {
    return Response.seeOther(URI.create(uri));
  }

  protected Response.ResponseBuilder unauthorized() {
    return Response.status(Response.Status.UNAUTHORIZED);
  }
}
