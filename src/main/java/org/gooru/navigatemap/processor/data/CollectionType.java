package org.gooru.navigatemap.processor.data;

import java.util.HashMap;
import java.util.Map;

/**
 * @author ashish on 26/2/17.
 */
public enum CollectionType {

    Collection("collection"),
    Assessment("assessment");

    private final String name;

    CollectionType(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    private static final Map<String, CollectionType> LOOKUP = new HashMap<>(values().length);

    static {
        for (CollectionType collectionType : values()) {
            LOOKUP.put(collectionType.name, collectionType);
        }
    }

    public static CollectionType builder(String type) {
        CollectionType result = LOOKUP.get(type);
        if (result == null) {
            throw new IllegalArgumentException("Invalid type: " + type);
        }
        return result;
    }
}
