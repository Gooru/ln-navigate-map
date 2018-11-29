package org.gooru.navigatemap.processor.postprocessor;

import java.util.HashMap;
import java.util.Map;

/**
 * @author ashish on 25/7/18.
 */
final class SuggestionTrackerTypes {

  private SuggestionTrackerTypes() {
    throw new AssertionError();
  }

  public enum SuggestedContentType {

    Course("course"),
    Unit("unit"),
    Lesson("lesson"),
    Collection("collection"),
    Assessment("assessment"),
    Resource("resource"),
    Question("question");

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

  public enum SuggestedTo {
    Student("student"),
    Teacher("teacher");

    private final String name;

    SuggestedTo(String name) {
      this.name = name;
    }

    public String getName() {
      return name;
    }

    private static final Map<String, SuggestedTo> LOOKUP = new HashMap<>(values().length);

    static {
      for (SuggestedTo suggestedTo : values()) {
        LOOKUP.put(suggestedTo.name, suggestedTo);
      }
    }

    public static SuggestedTo builder(String type) {
      SuggestedTo result = LOOKUP.get(type);
      if (result == null) {
        throw new IllegalArgumentException("Invalid suggested to type");
      }
      return result;
    }
  }

  public enum SuggestionOrigin {
    System("system"),
    Teacher("teacher");

    private final String name;

    SuggestionOrigin(String name) {
      this.name = name;
    }

    public String getName() {
      return name;
    }

    private static final Map<String, SuggestionOrigin> LOOKUP = new HashMap<>(values().length);

    static {
      for (SuggestionOrigin suggestionOrigin : values()) {
        LOOKUP.put(suggestionOrigin.name, suggestionOrigin);
      }
    }

    public static SuggestionOrigin builder(String type) {
      SuggestionOrigin result = LOOKUP.get(type);
      if (result == null) {
        throw new IllegalArgumentException("Invalid suggestion origin type");
      }
      return result;
    }
  }

  public enum SuggestionCriteria {
    Performance("performance"),
    Location("location");

    private final String name;

    SuggestionCriteria(String name) {
      this.name = name;
    }

    public String getName() {
      return name;
    }

    private static final Map<String, SuggestionCriteria> LOOKUP = new HashMap<>(values().length);

    static {
      for (SuggestionCriteria suggestionCriteria : values()) {
        LOOKUP.put(suggestionCriteria.name, suggestionCriteria);
      }
    }

    public static SuggestionCriteria builder(String type) {
      SuggestionCriteria result = LOOKUP.get(type);
      if (result == null) {
        throw new IllegalArgumentException("Invalid suggestion criteria type");
      }
      return result;
    }
  }

}
