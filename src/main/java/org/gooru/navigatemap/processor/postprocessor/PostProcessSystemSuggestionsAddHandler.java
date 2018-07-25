package org.gooru.navigatemap.processor.postprocessor;

import java.util.Objects;
import java.util.UUID;

import org.skife.jdbi.v2.DBI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.vertx.core.json.JsonObject;

/**
 * @author ashish on 24/7/18.
 */
class PostProcessSystemSuggestionsAddHandler implements PostProcessorHandler {
    private final DBI dbi;
    private static final Logger LOGGER = LoggerFactory.getLogger(PostProcessSystemSuggestionsAddHandler.class);
    private SystemSuggestionPayload command;
    private PostProcessorDao postProcessorDao;
    private SuggestionTrackerModel suggestionTrackerModel;

    PostProcessSystemSuggestionsAddHandler(DBI dbi) {
        this.dbi = dbi;
    }

    @Override
    public void handle(JsonObject requestData) {
        LOGGER.info("Processing system suggestion add handler for payload: '{}'", Objects.toString(requestData));
        initialize(requestData);
        if (command.getClassId() == null) {
            handleForIL();
        } else {
            handleInClass();
        }
        doAcceptanceIfNeeded();
    }

    private void doAcceptanceIfNeeded() {
        if (suggestionTrackerModel != null) {
            if (!suggestionTrackerModel.isAccepted()) {
                getPostProcessorDao().acceptSystemSuggestion(suggestionTrackerModel.getId());
            } else {
                LOGGER.info("Suggestion is already accepted");
            }
        } else {
            LOGGER.warn("Not able to find suggestion to accept");
        }
    }

    private void initialize(JsonObject requestData) {
        command = buildFromJson(requestData);
    }

    private void handleInClass() {
        if (command.getCollectionId() == null) {
            suggestionTrackerModel = getPostProcessorDao().fetchSystemSuggestionInClassAtLesson(command);
        } else {
            suggestionTrackerModel = getPostProcessorDao().fetchSystemSuggestionInClassAtCollection(command);
        }
    }

    private void handleForIL() {
        if (command.getCollectionId() == null) {
            suggestionTrackerModel = getPostProcessorDao().fetchSystemSuggestionForILAtLesson(command);
        } else {
            suggestionTrackerModel = getPostProcessorDao().fetchSystemSuggestionForILAtCollection(command);
        }
    }

    private PostProcessorDao getPostProcessorDao() {
        if (postProcessorDao == null) {
            postProcessorDao = dbi.onDemand(PostProcessorDao.class);
        }
        return postProcessorDao;
    }

    private SystemSuggestionPayload buildFromJson(JsonObject request) {
        return request.mapTo(SystemSuggestionPayload.class);
    }

    private static UUID nullSafeUuidConverter(String str) {
        return (str != null && !str.isEmpty()) ? UUID.fromString(str) : null;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class SystemSuggestionPayload {
        @JsonProperty("ctx_user_id")
        private UUID userId;
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

        public UUID getUserId() {
            return userId;
        }

        public void setUserId(UUID userId) {
            this.userId = userId;
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
    }
}
