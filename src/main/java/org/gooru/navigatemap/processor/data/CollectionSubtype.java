package org.gooru.navigatemap.processor.data;

/**
 * @author ashish on 26/2/17.
 */
public enum CollectionSubtype {
    PreTest,
    PostTest,
    BenchMark;

    public static CollectionSubtype builder(String subtype) {
        if ("pre-test".equalsIgnoreCase(subtype)) {
            return PreTest;
        } else if ("post-test".equalsIgnoreCase(subtype)) {
            return PostTest;
        } else if ("benchmark".equalsIgnoreCase(subtype)) {
            return BenchMark;
        } else {
            throw new IllegalArgumentException("Invalid subtype: " + subtype);
        }
    }
}
