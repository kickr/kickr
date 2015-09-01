package kickr.web.model.user;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.FormParam;

/**
 *
 * @author nikku
 */
public class LoginData {

  @FormParam("name")
  private String name;

  @FormParam("password")
  private String password;

  @FormParam("rememberMe") @DefaultValue("false")
  private boolean rememberMe;

  public LoginData() { }

  public String getName() {
    return name;
  }

  public String getPassword() {
    return password;
  }

  public Boolean getRememberMe() {
    return rememberMe;
  }
}
