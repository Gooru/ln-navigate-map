package org.gooru.navigatemap.infra.data;

import java.util.HashMap;
import java.util.Map;

/**
 * @author ashish on 2/5/18.
 */
public enum SuggestionType {

    System("system"),
    Teacher("teacher");

    private final String name;

    SuggestionType(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    private static final Map<String, SuggestionType> LOOKUP = new HashMap<>(values().length);

    static {
        for (SuggestionType suggestionType : values()) {
            LOOKUP.put(suggestionType.name, suggestionType);
        }
    }

    public static SuggestionType builder(String type) {
        SuggestionType result = LOOKUP.get(type);
        if (result == null) {
            throw new IllegalArgumentException("Invalid suggested content type: " + type);
        }
        return result;
    }

}
