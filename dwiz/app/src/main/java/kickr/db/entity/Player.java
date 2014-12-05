package kickr.db.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

@Entity
public class Player {

  @GeneratedValue
  @Id
  protected Long id;
  
  protected String name;
  
  @NotNull
  protected String alias;
  
  public Player(String alias, String name) {
    this.alias = alias;
    this.name = name;
  }
  
  public Long getId() {
    return id;
  }
  public void setId(Long id) {
    this.id = id;
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
}
