package org.gooru.navigatemap.processor.data;

/**
 * @author ashish on 26/2/17.
 */
public enum CollectionType {

    Collection,
    Assessment;

    public static CollectionType builder(String type) {
        if ("collection".equalsIgnoreCase(type)) {
            return Collection;
        } else if ("assessment".equalsIgnoreCase(type)) {
            return Assessment;
        } else {
            throw new IllegalArgumentException("Invalid type: " + type);
        }
    }
}
