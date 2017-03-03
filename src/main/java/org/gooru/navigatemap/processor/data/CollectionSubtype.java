package org.gooru.navigatemap.processor.data;

import java.util.HashMap;
import java.util.Map;

/**
 * @author ashish on 26/2/17.
 */
public enum CollectionSubtype {
    PreTest("pre-test"),
    PostTest("post-test"),
    BenchMark("benchmark");

    private final String name;
    private static final Map<String, CollectionSubtype> LOOKUP = new HashMap<>(values().length);

    static {
        for (CollectionSubtype subtype : values()) {
            LOOKUP.put(subtype.name, subtype);
        }
    }

    CollectionSubtype(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public static CollectionSubtype builder(String subtype) {
        CollectionSubtype result = LOOKUP.get(subtype);
        if (result == null) {
            throw new IllegalArgumentException("Invalid subtype: " + subtype);
        }
        return result;
    }
}
