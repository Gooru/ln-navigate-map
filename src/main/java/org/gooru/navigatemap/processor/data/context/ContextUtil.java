package org.gooru.navigatemap.processor.data.context;

import java.util.Objects;
import java.util.UUID;

/**
 * @author ashish on 8/3/17.
 */
public final class ContextUtil {

    private static final String ABSENT_PLACEHOLDER = "NONE";

    private ContextUtil() {
        throw new AssertionError();
    }

    public static String createUserContextKey(String userId, UUID courseId, UUID classId) {
        return userId + ':' + Objects.toString(courseId, ABSENT_PLACEHOLDER) + ':' + Objects
            .toString(classId, ABSENT_PLACEHOLDER);
    }

    public static String createUserContextKey(String userId, String courseId, String classId) {
        return userId + ':' + Objects.toString(trimEmptyStringAsNull(courseId), ABSENT_PLACEHOLDER) + ':' + Objects
            .toString(trimEmptyStringAsNull(classId), ABSENT_PLACEHOLDER);
    }

    private static String trimEmptyStringAsNull(String input) {
        if (input == null || input.isEmpty()) {
            return null;
        } else {
            return input;
        }
    }
}
