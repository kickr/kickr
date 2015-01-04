package kickr.core.model.user;

/**
 *
 * @author nikku
 */
public class LoginData {

  private String name;

  private String password;

  public LoginData() { }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }
}
