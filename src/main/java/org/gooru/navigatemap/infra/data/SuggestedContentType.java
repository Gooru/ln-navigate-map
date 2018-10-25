package org.gooru.navigatemap.infra.data;

import java.util.HashMap;
import java.util.Map;

/**
 * @author ashish on 17/11/17.
 */
public enum SuggestedContentType {
  Course("course"),
  Unit("unit"),
  Lesson("lesson"),
  Collection("collection"),
  Assessment("assessment"),
  Resource("resource");

  private final String name;

  SuggestedContentType(String name) {
    this.name = name;
  }

  public String getName() {
    return this.name;
  }

  private static final Map<String, SuggestedContentType> LOOKUP = new HashMap<>(values().length);

  static {
    for (SuggestedContentType suggestedContentType : values()) {
      LOOKUP.put(suggestedContentType.name, suggestedContentType);
    }
  }

  public static SuggestedContentType builder(String type) {
    SuggestedContentType result = LOOKUP.get(type);
    if (result == null) {
      throw new IllegalArgumentException("Invalid suggested content type: " + type);
    }
    return result;
  }

}
