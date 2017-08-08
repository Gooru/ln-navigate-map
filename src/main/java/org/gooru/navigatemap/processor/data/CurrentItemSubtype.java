package org.gooru.navigatemap.processor.data;

import java.util.HashMap;
import java.util.Map;

/**
 * @author ashish on 26/2/17.
 */
public enum CurrentItemSubtype {

    PreTest("pre-test"),
    PostTest("post-test"),
    BenchMark("benchmark"),
    Video("video_resource"),
    Webpage("webpage_resource"),
    Interactive("interactive_resource"),
    Image("image_resource"),
    Text("text_resource"),
    Audio("audio_resource");

    private final String name;
    private static final Map<String, CurrentItemSubtype> LOOKUP = new HashMap<>(values().length);

    static {
        for (CurrentItemSubtype subtype : values()) {
            LOOKUP.put(subtype.name, subtype);
        }
    }

    CurrentItemSubtype(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public static CurrentItemSubtype builder(String subtype) {
        CurrentItemSubtype result = LOOKUP.get(subtype);
        if (result == null) {
            throw new IllegalArgumentException("Invalid subtype: " + subtype);
        }
        return result;
    }
}
