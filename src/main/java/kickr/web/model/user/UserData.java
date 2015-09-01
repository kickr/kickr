package kickr.web.model.user;

/**
 *
 * @author nikku
 */
public class UserData {

  private String name;

  private String token;

  private int permissions;

  public UserData(String name) {
    this(name, 1, null);
  }

  public UserData(String name, int permissions, String token) {
    this.name = name;
    this.permissions = permissions;
    this.token = token;
  }

  public String getName() {
    return name;
  }

  public String getToken() {
    return token;
  }

  public int getPermissions() {
    return permissions;
  }
}
