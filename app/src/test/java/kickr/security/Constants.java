package kickr.security;

import kickr.db.entity.user.Role;
import kickr.db.entity.user.User;
import support.security.TypedSecurityContext;

/**
 *
 * @author nikku
 */
public class Constants {

  public static User user(String name, Role ... roles) {
    User user = new User("name", name + "@" + name);

    if (roles.length > 0) {
      user.setPermissions(Role.toPermissions(roles));
    }

    return user;
  }

  public static TypedSecurityContext<User> loggedIn(User user) {

    return new TypedSecurityContext<User>(true, "kickr", user) {
      @Override
      public boolean isUserInRole(String role) {
        return user.hasRole(role);
      }
    };
  }

  public static TypedSecurityContext<User> loggedOut() {

    return new TypedSecurityContext<User>(true, "kickr", null) {
      
      @Override
      public boolean isUserInRole(String role) {
        return false;
      }
    };
  }
}
