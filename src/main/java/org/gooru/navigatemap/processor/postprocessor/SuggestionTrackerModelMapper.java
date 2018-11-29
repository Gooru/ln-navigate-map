package org.gooru.navigatemap.processor.postprocessor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

/**
 * @author ashish on 25/7/18.
 */
public class SuggestionTrackerModelMapper implements ResultSetMapper<SuggestionTrackerModel> {

  @Override
  public SuggestionTrackerModel map(int index, ResultSet r, StatementContext ctx)
      throws SQLException {
    SuggestionTrackerModel model = new SuggestionTrackerModel();
    model.setId(r.getLong(ContextAttributes.ID));
    model.setUserId(nullSafeUuidConverter(r.getString(ContextAttributes.USER_ID)));
    model.setCourseId(nullSafeUuidConverter(r.getString(ContextAttributes.COURSE_ID)));
    model.setUnitId(nullSafeUuidConverter(r.getString(ContextAttributes.UNIT_ID)));
    model.setLessonId(nullSafeUuidConverter(r.getString(ContextAttributes.LESSON_ID)));
    model.setClassId(nullSafeUuidConverter(r.getString(ContextAttributes.CLASS_ID)));
    model.setCollectionId(nullSafeUuidConverter(r.getString(ContextAttributes.COLLECTION_ID)));
    model.setSuggestedContentId(
        nullSafeUuidConverter(r.getString(ContextAttributes.SUGGESTED_CONTENT_ID)));
    model.setSuggestionOrigin(r.getString(ContextAttributes.SUGGESTION_ORIGIN));
    model.setSuggestionOriginatorId(r.getString(ContextAttributes.SUGGESTION_ORIGINATOR_ID));
    model.setSuggestionCriteria(r.getString(ContextAttributes.SUGGESTION_CRITERIA));
    model.setSuggestedContentType(r.getString(ContextAttributes.SUGGESTED_CONTENT_TYPE));
    model.setSuggestedTo(r.getString(ContextAttributes.SUGGESTED_TO));
    model.setAccepted(r.getBoolean(ContextAttributes.ACCEPTED));
    model.setAcceptedAt(r.getDate(ContextAttributes.ACCEPTED_AT));

    return model;
  }

  private UUID nullSafeUuidConverter(String string) {
    return (string != null && !string.isEmpty()) ? UUID.fromString(string) : null;
  }

  static class ContextAttributes {

    public static final String ID = "id";
    public static final String USER_ID = "user_id";
    public static final String COURSE_ID = "course_id";
    public static final String UNIT_ID = "unit_id";
    public static final String LESSON_ID = "lesson_id";
    public static final String CLASS_ID = "class_id";
    public static final String COLLECTION_ID = "collection_id";
    public static final String SUGGESTED_CONTENT_ID = "suggested_content_id";
    public static final String SUGGESTION_ORIGIN = "suggestion_origin";
    public static final String SUGGESTION_ORIGINATOR_ID = "suggestion_originator_id";
    public static final String SUGGESTION_CRITERIA = "suggestion_criteria";
    public static final String SUGGESTED_CONTENT_TYPE = "suggested_content_type";
    public static final String SUGGESTED_TO = "suggested_to";
    public static final String ACCEPTED = "accepted";
    public static final String ACCEPTED_AT = "accepted_at";
    public static final String CTX = "ctx";
  }
}
