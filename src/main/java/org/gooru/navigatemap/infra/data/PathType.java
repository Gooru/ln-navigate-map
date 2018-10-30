package org.gooru.navigatemap.infra.data;

import java.util.HashMap;
import java.util.Map;

/**
 * @author ashish on 2/5/18.
 */
public enum PathType {

  System("system"),
  Teacher("teacher"),
  Route0("route0");

  private final String name;

  PathType(String name) {
    this.name = name;
  }

  public String getName() {
    return this.name;
  }

  private static final Map<String, PathType> LOOKUP = new HashMap<>(values().length);

  static {
    for (PathType suggestionType : values()) {
      LOOKUP.put(suggestionType.name, suggestionType);
    }
  }

  public static PathType builder(String type) {
    PathType result = LOOKUP.get(type);
    if (result == null) {
      throw new IllegalArgumentException("Invalid path type: " + type);
    }
    return result;
  }

}
