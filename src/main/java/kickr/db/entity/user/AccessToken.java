package kickr.db.entity.user;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import kickr.db.entity.BaseEntity;

/**
 *
 * @author nikku
 */
@Entity
@Table(name = "kickr_access_token")
@NamedQueries({
  @NamedQuery(
    name = "AccessToken.byValue",
    query = "SELECT t FROM AccessToken t JOIN FETCH t.user WHERE t.value = :value")
})
public class AccessToken extends BaseEntity {

  @ManyToOne
  private User user;

  @Column(unique = true)
  private String value;

  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "valid_until")
  private Date validUntil;

  public AccessToken() {}

  public AccessToken(String value, User user) {
    this.value = value;
    this.user = user;
  }

  public User getUser() {
    return user;
  }

  public String getValue() {
    return value;
  }

  public Date getValidUntil() {
    return validUntil;
  }

  public void setValidUntil(Date validUntil) {
    this.validUntil = validUntil;
  }
}
