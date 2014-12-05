package de.nixis.bpmn.repository.db.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

/**
 *
 * @author nico.rehwaldt
 */
@Entity
@NamedQueries({
  @NamedQuery(
    name = "Model.findById",
    query = "SELECT m FROM Model m WHERE m.id = :id ORDER BY m.revision DESC"),
  @NamedQuery(
    name = "Model.findLatest",
    query = "SELECT m FROM Model m ORDER BY m.created DESC"),
  @NamedQuery(
    name = "Model.findByRevision",
    query = "SELECT m FROM Model m WHERE m.id = :id AND m.revision = :revision")
})
@Table(
  uniqueConstraints = {
    @UniqueConstraint(columnNames = { "id", "revision" })
  }
)
public class Model implements Serializable {

  @Id
  @NotNull
  private String key;
  
  @NotNull
  private String id;
  
  private long revision = 0;
  
  @ManyToOne
  private Model parent;
  
  private String type;
  
  @Lob
  @NotNull
  private String content;

  @Temporal(TemporalType.TIMESTAMP)
  private Date created;

  
  public Model() {}

  public Model(String key, String id, long revision, String content, String type, Model parent) {
    this(content, type, parent);
    
    this.key = key;
    this.id = id;
    this.revision = revision;
  }
  
  public Model(String content, String type, Model parent) {
    this.content = content;
    this.type = type;
    this.parent = parent;
  
    this.created = new Date();
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getKey() {
    return key;
  }

  public void setKey(String key) {
    this.key = key;
  }

  public long getRevision() {
    return revision;
  }

  public void setRevision(long revision) {
    this.revision = revision;
  }

  public Model getParent() {
    return parent;
  }

  public void setParent(Model parent) {
    this.parent = parent;
  }
  
  public String getType() {
    return type;
  }

  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }

  public Date getCreated() {
    return created;
  }
}