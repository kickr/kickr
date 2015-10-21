package kickr.db.entity;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Table(name = "kickr_player")
public class Player extends BaseEntity {

  protected String name;
  
  @NotNull
  @Column(unique = true)
  protected String alias;
  
  protected String email;
  
  public Player() {}
  
  public Player(String alias, String name, String email) {
    this.alias = alias;
    this.name = name;
    this.email = email;
  }

  public String getName() {
    return name;
  }
  
  public void setName(String name) {
    this.name = name;
  }
  
  public String getAlias() {
    return alias;
  }
  
  public void setAlias(String alias) {
    this.alias = alias;
  }
  
  public String getEmail() {
    return email;
  }
  
  public void setEmail(String email) {
    this.email = email;
  }

  @Override
  public String toString() {
    return String.format("Player{ alias: %s }", alias);
  }
}
