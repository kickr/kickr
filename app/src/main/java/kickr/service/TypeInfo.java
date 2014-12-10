package kickr.service;

/**
 *
 * @author nikku
 */
public class TypeInfo {
  
  private final String extension;
  private final String contentType;
  private final String id;

  public TypeInfo(String id, String extension, String contentType) {
    this.id = id;
    
    this.extension = extension;
    this.contentType = contentType;
  }

  public String getId() {
    return id;
  }
  
  public String getExtension() {
    return extension;
  }

  public String getContentType() {
    return contentType;
  }
}
