package org.gooru.navigatemap.processor.data;

import java.util.HashMap;
import java.util.Map;

/**
 * @author ashish on 26/2/17.
 */
public enum CurrentItemType {

    Collection("collection"),
    Assessment("assessment"),
    Resource("resource");

    private final String name;

    CurrentItemType(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    private static final Map<String, CurrentItemType> LOOKUP = new HashMap<>(values().length);

    static {
        for (CurrentItemType currentItemType : values()) {
            LOOKUP.put(currentItemType.name, currentItemType);
        }
    }

    public static CurrentItemType builder(String type) {
        CurrentItemType result = LOOKUP.get(type);
        if (result == null) {
            throw new IllegalArgumentException("Invalid current item type: " + type);
        }
        return result;
    }
}
