package kickr.db.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

/**
 *
 * @author nikku
 */
@MappedSuperclass
public class BaseEntity implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  protected Long id;
  
  @NotNull
  @Temporal(TemporalType.TIMESTAMP)
  protected Date created;
  
  public BaseEntity() {
    this.created = new Date();
  }
  
  public Long getId() {
    return id;
  }

  public Date getCreated() {
    return created;
  }

  public void setCreated(Date created) {
    this.created = created;
  }

  @Override
  public int hashCode() {
    return id != null ? id.hashCode() : -1;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    
    if (getClass() != obj.getClass()) {
      return false;
    }
    
    final BaseEntity other = (BaseEntity) obj;

    return Objects.equals(this.id, other.id);
  }
}
