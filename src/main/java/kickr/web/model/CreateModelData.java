package kickr.web.model;

import org.hibernate.validator.constraints.NotEmpty;

/**
 *
 * @author nikku
 */
public class CreateModelData {

  @NotEmpty
  protected String type;

  public CreateModelData() { }

  public CreateModelData(String type) {
    this.type = type;
  }
  
  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }
}
