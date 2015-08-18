package kickr.web.api;

import kickr.web.view.IndexView;
import javax.ws.rs.GET;
import javax.ws.rs.Path;

/**
 *
 * @author nikku
 */
@Path("/")
public class RootResource {

  @GET
  public IndexView index() {
    return new IndexView();
  }
}
