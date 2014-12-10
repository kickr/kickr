package kickr.db.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

@Entity
public class Player {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  protected Long id;
  
  protected String name;
  
  @NotNull
  @Column(unique = true)
  protected String alias;
  
  protected String email;
  
  public Player() {
  }
  
  public Player(String alias, String name, String email) {
    this.alias = alias;
    this.name = name;
    this.email = email;
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
  public String getEmail() {
    return email;
  }
  public void setEmail(String email) {
    this.email = email;
  }
}
