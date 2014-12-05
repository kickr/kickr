package kickr.service;

import kickr.db.entity.Model;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

/**
 *
 * @author nikku
 */
public class ModelService {

  private static final Map<String, TypeInfo> TYPE_MAP;
  
  static {
    TYPE_MAP = new HashMap<>();

    TYPE_MAP.put("bpmn20", new TypeInfo("bpmn20", "bpmn", "application/bpmn20+xml"));
  };

  
  public Model initializeModel(String type) {
    
    TypeInfo info = getTypeInfo(type);
    
    if (info == null) {
      throw new IllegalArgumentException("Invalid type specified");
    }
    
    String defaultContents = getDefaultContents(info);
    
    if (defaultContents == null) {
      throw new IllegalArgumentException("No default contents for " + type);
    }
    
    return new Model(createId(), createId(), 0, defaultContents, type, null);
  }
  
  public Model updateModel(Model model, String content) {
    return new Model(createId(), model.getId(), model.getRevision() + 1, content, model.getType(), model);
  }
  
  private String createId() {
    return UUID.randomUUID().toString();
  }
  
  private String getDefaultContents(TypeInfo info) {
    InputStream is = getClass().getClassLoader().getResourceAsStream("model/" + info.getId());
    
    try {
      java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
      return s.hasNext() ? s.next() : null;
    } finally {
      try { is.close(); } catch (IOException e) {}
    }
  }

  public TypeInfo getTypeInfo(Model model) {
    return getTypeInfo(model.getType());
  }

  private TypeInfo getTypeInfo(String type) {
    return TYPE_MAP.get(type);
  }
}
