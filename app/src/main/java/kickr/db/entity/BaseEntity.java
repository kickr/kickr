package kickr.db.entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

/**
 *
 * @author nikku
 */
@MappedSuperclass
public class BaseEntity implements Serializable {
  
  protected boolean removed = false;

  public boolean isRemoved() {
    return removed;
  }

  public void setRemoved(boolean removed) {
    this.removed = removed;
  }  
}
