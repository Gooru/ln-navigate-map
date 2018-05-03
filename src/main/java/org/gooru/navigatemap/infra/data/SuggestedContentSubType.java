package org.gooru.navigatemap.infra.data;

import java.util.HashMap;
import java.util.Map;

/**
 * @author ashish on 17/11/17.
 */
public enum SuggestedContentSubType {
    PreTest("pre-test"),
    PostTest("post-test"),
    Benchmark("benchmark");

    private final String name;

    SuggestedContentSubType(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    private static final Map<String, SuggestedContentSubType> LOOKUP = new HashMap<>(values().length);

    static {
        for (SuggestedContentSubType suggestedContentSubType : values()) {
            LOOKUP.put(suggestedContentSubType.name, suggestedContentSubType);
        }
    }

    public static SuggestedContentSubType builder(String type) {
        SuggestedContentSubType result = LOOKUP.get(type);
        if (result == null) {
            throw new IllegalArgumentException("Invalid suggested content sub type: " + type);
        }
        return result;
    }

}
