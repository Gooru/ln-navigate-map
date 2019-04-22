package org.gooru.navigatemap.infra.data.context;

import java.util.Objects;
import java.util.UUID;

/**
 * @author ashish on 8/3/17.
 */
public final class ContextUtil {

  private static final String ABSENT_PLACEHOLDER = "NONE";
  /*
    v3: Added new key named path_type in context object, hence bumping the version number
    v4: Milestone view support addition
  */
  private static final String VERSION_STR = "v4:";

  private ContextUtil() {
    throw new AssertionError();
  }

  public static String createUserContextKey(String userId, UUID courseId, UUID classId) {
    return VERSION_STR + userId + ':' + courseId.toString() + ':' + Objects
        .toString(classId, ABSENT_PLACEHOLDER);
  }

  public static String createUserContextKey(String userId, String courseId, String classId) {
    return VERSION_STR + userId + ':' + trimEmptyStringAsNull(courseId) + ':' + Objects
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
