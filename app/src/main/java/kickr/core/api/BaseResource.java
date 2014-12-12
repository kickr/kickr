package kickr.core.api;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

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
}
