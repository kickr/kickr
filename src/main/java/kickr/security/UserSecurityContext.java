package kickr.security;

import kickr.db.entity.user.User;
import support.security.TypedSecurityContext;

/**
 *
 * @author nikku
 */
public class UserSecurityContext extends TypedSecurityContext<User> {
  
  public UserSecurityContext(boolean secure, String authenticationScheme, User user) {
    super(secure, authenticationScheme, user);
  }

  @Override
  public boolean isUserInRole(String role) {
    User user = getUserPrincipal();
    
    if (user != null) {
      return user.hasRole(role);
    } else {
      return false;
    }
  }
}
