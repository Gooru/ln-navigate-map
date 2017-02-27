package org.gooru.navigatemap.processor.data;

/**
 * @author ashish on 26/2/17.
 */
public enum ActionType {
    Suggested,
    ContentServed;

    public static ActionType builder(String action) {
        if ("suggested".equalsIgnoreCase(action)) {
            return Suggested;
        } else {
            return ContentServed;
        }
    }
}
