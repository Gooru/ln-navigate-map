package org.gooru.navigatemap.processor.postprocessor;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.vertx.core.json.JsonObject;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import org.skife.jdbi.v2.DBI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author ashish on 24/7/18.
 */
class PostProcessTeacherSuggestionAddHandler implements PostProcessorHandler {

  private final DBI dbi;
  private static final Logger LOGGER = LoggerFactory
      .getLogger(PostProcessTeacherSuggestionAddHandler.class);
  private TeacherSuggestionPayload command;
  private PostProcessorDao postProcessorDao;

  PostProcessTeacherSuggestionAddHandler(DBI dbi) {
    this.dbi = dbi;
  }

  @Override
  public void handle(JsonObject requestData) {
    LOGGER.info("Processing teacher suggestion accept handler for payload: '{}'", requestData);
    initialize(requestData);
    if (command.getUserIds() != null && !command.getUserIds().isEmpty()) {
      List<SuggestionTrackerModel> suggestionTrackerModels =
          SuggestionTrackerModelsBuilder.buildForTeacherSuggestion(command).build();
      trackSuggestion(suggestionTrackerModels);
      NotificationCoordinator.buildForTeacherSuggestionAdded(command, requestData)
          .coordinateNotification();
    } else {
      LOGGER.warn("User id list is null or empty for teacher suggestions.");
    }

  }

  private void trackSuggestion(List<SuggestionTrackerModel> suggestionTrackerModels) {
    try {
      getPostProcessorDao().insertAllSuggestions(suggestionTrackerModels);
    } catch (Throwable e) {
      LOGGER.warn("Not able to track suggestion in db.", e);
    }
  }

  private void initialize(JsonObject requestData) {
    command = buildFromJson(requestData);
  }

  private PostProcessorDao getPostProcessorDao() {
    if (postProcessorDao == null) {
      postProcessorDao = dbi.onDemand(PostProcessorDao.class);
    }
    return postProcessorDao;
  }

  private TeacherSuggestionPayload buildFromJson(JsonObject request) {
    return request.mapTo(TeacherSuggestionPayload.class);
  }

  @JsonIgnoreProperties(ignoreUnknown = true)
  public static class TeacherSuggestionPayload {

    @JsonProperty("ctx_user_ids")
    private List<UUID> userIds;
    @JsonProperty("ctx_class_id")
    private UUID classId;
    @JsonProperty("ctx_course_id")
    private UUID courseId;
    @JsonProperty("ctx_unit_id")
    private UUID unitId;
    @JsonProperty("ctx_lesson_id")
    private UUID lessonId;
    @JsonProperty("ctx_collection_id")
    private UUID collectionId;
    @JsonProperty("suggested_content_id")
    private UUID suggestedContentId;
    @JsonProperty("suggested_content_type")
    private String suggestedContentType;
    @JsonProperty("user_id")
    private UUID teacherId;

    public List<UUID> getUserIds() {
      return Collections.unmodifiableList(userIds);
    }

    public void setUserIds(List<UUID> userIds) {
      this.userIds = userIds;
    }

    public UUID getClassId() {
      return classId;
    }

    public void setClassId(UUID classId) {
      this.classId = classId;
    }

    public UUID getCourseId() {
      return courseId;
    }

    public void setCourseId(UUID courseId) {
      this.courseId = courseId;
    }

    public UUID getUnitId() {
      return unitId;
    }

    public void setUnitId(UUID unitId) {
      this.unitId = unitId;
    }

    public UUID getLessonId() {
      return lessonId;
    }

    public void setLessonId(UUID lessonId) {
      this.lessonId = lessonId;
    }

    public UUID getCollectionId() {
      return collectionId;
    }

    public void setCollectionId(UUID collectionId) {
      this.collectionId = collectionId;
    }

    public UUID getSuggestedContentId() {
      return suggestedContentId;
    }

    public void setSuggestedContentId(UUID suggestedContentId) {
      this.suggestedContentId = suggestedContentId;
    }

    public String getSuggestedContentType() {
      return suggestedContentType;
    }

    public void setSuggestedContentType(String suggestedContentType) {
      this.suggestedContentType = suggestedContentType;
    }

    public UUID getTeacherId() {
      return teacherId;
    }

    public void setTeacherId(UUID teacherId) {
      this.teacherId = teacherId;
    }

  }

}
