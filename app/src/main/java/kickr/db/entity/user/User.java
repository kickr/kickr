package kickr.db.entity.user;

import java.security.Principal;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import kickr.db.entity.BaseEntity;
import org.hibernate.validator.constraints.Email;

/**
 *
 * @author nikku
 */
@Entity
@Table(name="kickr_user")
@NamedQueries({
  @NamedQuery(name = "User.byName", query = "SELECT u FROM User u WHERE u.name = :name")
})
public class User extends BaseEntity implements Principal {

  @NotNull
  @Column(unique = true)
  private String name;
  
  @NotNull
  private String password;
  
  @Email
  @Column(unique = true)
  private String email;

  private int permissions = Role.toPermissions(Role.USER);

  public User() { }
  
  public User(String name, String email) {
    this.name = name;
    this.email = email;
  }

  @Override
  public String getName() {
    return name;
  }
  
  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public int getPermissions() {
    return permissions;
  }

  public void setPermissions(int permissions) {
    this.permissions = permissions;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }
  
  public boolean hasRole(String roleName) {
    Role role = Role.valueOf(roleName.toUpperCase());
    return role.isContained(permissions);
  }
}
