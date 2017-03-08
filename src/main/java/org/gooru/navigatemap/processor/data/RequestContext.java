package org.gooru.navigatemap.processor.data;

import java.util.UUID;

import org.gooru.navigatemap.processor.context.ContextAttributes;

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
    private CollectionType collectionType;
    private CollectionSubtype collectionSubType;

    private UUID currentItemId;
    private CollectionType currentItemType;
    private CollectionSubtype currentItemSubtype;

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

    public CollectionType getCollectionType() {
        return collectionType;
    }

    public CollectionSubtype getCollectionSubType() {
        return collectionSubType;
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

    public CollectionType getCurrentItemType() {
        return currentItemType;
    }

    public CollectionSubtype getCurrentItemSubtype() {
        return currentItemSubtype;
    }

    public boolean needsLastState() {
        return (state == State.Continue);
    }

    private void validate() {
        if ((courseId == null) || ((unitId == null || lessonId == null || currentItemId == null)
            && state == State.Start)) {
            throw new IllegalArgumentException("Invalid context");
        }
    }

    public static RequestContext builder(JsonObject input) {
        RequestContext result = buildFromJsonObject(input);
        result.validate();
        return result;
    }

    private static RequestContext buildFromJsonObject(JsonObject input) {
        RequestContext context = new RequestContext();

        context.classId = toUuid(input, ContextAttributes.CLASS_ID);
        context.courseId = toUuid(input, ContextAttributes.COURSE_ID);
        context.unitId = toUuid(input, ContextAttributes.UNIT_ID);
        context.lessonId = toUuid(input, ContextAttributes.LESSON_ID);
        context.collectionId = toUuid(input, ContextAttributes.COLLECTION_ID);
        context.currentItemId = toUuid(input, ContextAttributes.CURRENT_ITEM_ID);
        context.pathId = input.getLong(ContextAttributes.PATH_ID);
        context.scorePercent = input.getDouble(ContextAttributes.SCORE_PERCENT);
        String value = input.getString(ContextAttributes.COLLECTION_TYPE);
        context.collectionType = (value != null && !value.isEmpty()) ? CollectionType.builder(value) : null;
        value = input.getString(ContextAttributes.CURRENT_ITEM_TYPE);
        context.currentItemType = (value != null && !value.isEmpty()) ? CollectionType.builder(value) : null;
        value = input.getString(ContextAttributes.COLLECTION_SUBTYPE);
        context.collectionSubType = (value != null && !value.isEmpty()) ? CollectionSubtype.builder(value) : null;
        value = input.getString(ContextAttributes.CURRENT_ITEM_SUBTYPE);
        context.currentItemSubtype = (value != null && !value.isEmpty()) ? CollectionSubtype.builder(value) : null;
        value = input.getString(ContextAttributes.STATE);
        context.state = State.builder(value);

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
