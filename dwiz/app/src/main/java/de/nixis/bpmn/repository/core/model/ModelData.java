package de.nixis.bpmn.repository.core.model;

import de.nixis.bpmn.repository.db.entity.Model;
import java.io.Serializable;
import java.util.stream.Stream;
import javax.validation.constraints.NotNull;

/**
 *
 * @author nico.rehwaldt
 */
public class ModelData implements Serializable {

  private String id;
  
  private long revision;

  @NotNull
  private String type;
  
  @NotNull
  private String content;
  
  private String parent;
  
  public ModelData() {}

  public ModelData(String id, long revision, String content, String type) {
    this.id = id;
    this.revision = revision;
    this.content = content;
    this.type = type;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public long getRevision() {
    return revision;
  }

  public void setRev(long rev) {
    this.revision = rev;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }
  
  public static ModelData fromModel(Model m) {
    return new ModelData(m.getId(), m.getRevision(), m.getContent(), m.getType());
  }
}