package kickr.db.entity.user;

/**
 *
 * @author nikku
 */
public enum Role {

  NONE(1),
  USER(2),
  ADMIN(4);

  private final int value;

  Role(int value) {
    this.value = value;
  }

  public static int toPermissions(Role... roles) {

    int permissions = 0;

    for (Role r: roles) {
      permissions |= r.value;
    }

    return permissions;
  }

  public boolean isContained(int permissions) {
    return (permissions & value) == value;
  }
}
