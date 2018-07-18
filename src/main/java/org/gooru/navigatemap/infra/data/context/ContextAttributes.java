package org.gooru.navigatemap.infra.data.context;

/**
 * @author ashish on 8/3/17.
 */
public final class ContextAttributes {

    public static final String CLASS_ID = "class_id";
    public static final String COURSE_ID = "course_id";
    public static final String UNIT_ID = "unit_id";
    public static final String LESSON_ID = "lesson_id";
    public static final String COLLECTION_ID = "collection_id";

    public static final String CURRENT_ITEM_ID = "current_item_id";
    public static final String CURRENT_ITEM_TYPE = "current_item_type";
    public static final String CURRENT_ITEM_SUBTYPE = "current_item_subtype";
    public static final String PATH_ID = "path_id";

    public static final String SCORE_PERCENT = "score_percent";
    public static final String STATE = "state";
    public static final String PATH_TYPE = "path_type";
    public static final String CONTEXT_DATA = "context_data";

    private ContextAttributes() {
        throw new AssertionError();
    }
}
