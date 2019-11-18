package org.gooru.navigatemap.processor.postprocessor;

import java.util.HashMap;
import java.util.Map;

public enum SuggestionArea {
  CourseMap("course-map"),
  ClassActivity("class-activity"),
  Proficiency("proficiency");

  private final String name;

  SuggestionArea(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }

  private static final Map<String, SuggestionArea> LOOKUP = new HashMap<>(values().length);

  static {
    for (SuggestionArea suggestionArea : values()) {
      LOOKUP.put(suggestionArea.name, suggestionArea);
    }
  }

  public static SuggestionArea builder(String type) {
    SuggestionArea result = LOOKUP.get(type);
    if (result == null) {
      throw new IllegalArgumentException("Invalid suggestion area type");
    }
    return result;
  }
}
