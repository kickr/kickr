package kickr.web;

import java.net.URI;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import kickr.web.form.Form;
import kickr.web.view.FormView;

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

  /**
   * Indicate invalid form submit.
   *
   * @param <T>
   * @param <F>
   * @param formCls
   * @param form
   *
   * @return
   */
  public <F extends Form, Z extends FormView<Z, F>> InvalidFormSubmitException invalidForm(Class<Z> formCls, F form) {

    Z formView = createView(formCls).withForm(form);

    return new InvalidFormSubmitException(formView);
  }
}
