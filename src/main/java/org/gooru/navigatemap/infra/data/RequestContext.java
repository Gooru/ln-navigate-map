package org.gooru.navigatemap.infra.data;

import java.util.UUID;

import org.gooru.navigatemap.app.constants.HttpConstants;
import org.gooru.navigatemap.app.exceptions.HttpResponseWrapperException;
import org.gooru.navigatemap.infra.data.context.ContextAttributes;

import io.vertx.core.json.JsonObject;

/**
 * @author ashish on 26/2/17.
 */
public final class RequestContext {
    private UUID classId;
    private UUID courseId;
    private UUID unitId;
    private UUID lessonId;
    private UUID collectionId;

    private UUID currentItemId;
    private CurrentItemType currentItemType;
    private CurrentItemSubtype currentItemSubtype;

    private State state;
    private Long pathId;
    private Double scorePercent;

    public UUID getClassId() {
        return classId;
    }

    public UUID getCourseId() {
        return courseId;
    }

    public UUID getUnitId() {
        return unitId;
    }

    public UUID getLessonId() {
        return lessonId;
    }

    public UUID getCollectionId() {
        return collectionId;
    }

    public State getState() {
        return state;
    }

    public Long getPathId() {
        return pathId;
    }

    public Double getScorePercent() {
        return scorePercent;
    }

    public UUID getCurrentItemId() {
        return currentItemId;
    }

    public CurrentItemType getCurrentItemType() {
        return currentItemType;
    }

    public CurrentItemSubtype getCurrentItemSubtype() {
        return currentItemSubtype;
    }

    public boolean needsLastState() {
        return (state == State.Continue);
    }

    public boolean needToStartLesson() {
        return (state == State.Start && currentItemId == null);
    }

    private void validate() {
        if (courseId == null) {
            throw new HttpResponseWrapperException(HttpConstants.HttpStatus.BAD_REQUEST, "Invalid course id");
        }
        if (((unitId == null || lessonId == null) && state == State.Start)) {
            throw new HttpResponseWrapperException(HttpConstants.HttpStatus.BAD_REQUEST,
                "Invalid context for Start flow");
        }
    }

    public static RequestContext builder(JsonObject input) {
        RequestContext result = buildFromJsonObject(input);
        result.validate();
        return result;
    }

    private static RequestContext buildFromJsonObject(JsonObject input) {
        RequestContext context = new RequestContext();

        try {
            context.classId = toUuid(input, ContextAttributes.CLASS_ID);
            context.courseId = toUuid(input, ContextAttributes.COURSE_ID);
            context.unitId = toUuid(input, ContextAttributes.UNIT_ID);
            context.lessonId = toUuid(input, ContextAttributes.LESSON_ID);
            context.collectionId = toUuid(input, ContextAttributes.COLLECTION_ID);
            context.currentItemId = toUuid(input, ContextAttributes.CURRENT_ITEM_ID);
            context.pathId = input.getLong(ContextAttributes.PATH_ID);
            context.scorePercent = input.getDouble(ContextAttributes.SCORE_PERCENT);
            String value;
            value = input.getString(ContextAttributes.CURRENT_ITEM_TYPE);
            context.currentItemType = (value != null && !value.isEmpty()) ? CurrentItemType.builder(value) : null;
            value = input.getString(ContextAttributes.CURRENT_ITEM_SUBTYPE);
            context.currentItemSubtype = (value != null && !value.isEmpty()) ? CurrentItemSubtype.builder(value) : null;
            value = input.getString(ContextAttributes.STATE);
            context.state = State.builder(value);
        } catch (IllegalArgumentException e) {
            throw new HttpResponseWrapperException(HttpConstants.HttpStatus.BAD_REQUEST, e.getMessage());
        }

        return context;
    }

    private static UUID toUuid(JsonObject input, String key) {
        String value = input.getString(key);
        if (value == null || value.isEmpty()) {
            return null;
        }
        return UUID.fromString(value);
    }
}
