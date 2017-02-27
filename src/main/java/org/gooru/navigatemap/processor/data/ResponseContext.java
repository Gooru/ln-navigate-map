package org.gooru.navigatemap.processor.data;

import java.util.UUID;

import io.vertx.core.json.JsonObject;

/**
 * @author ashish on 28/2/17.
 */
public final class ResponseContext {
    private UUID classId;
    private UUID courseId;
    private UUID unitId;
    private UUID lessonId;
    private UUID collectionId;
    private CollectionType collectionType;
    private CollectionSubtype collectionSubType;
    private ActionType actionType;
    private Long pathId;
    private Double scorePercent;

    public ResponseContext(RequestContext context) {
        this.classId = context.getClassId();
        this.courseId = context.getCourseId();
        this.unitId = context.getUnitId();
        this.lessonId = context.getLessonId();
        this.collectionId = context.getCollectionId();
        this.collectionType = context.getCollectionType();
        this.collectionSubType = context.getCollectionSubType();
        this.actionType = context.getActionType();
        this.pathId = context.getPathId();
        this.scorePercent = context.getScorePercent();
    }

    public JsonObject toJson() {
        JsonObject context = new JsonObject();

        context.put("classId", classId);
        context.put("courseId", courseId);
        context.put("unitId", unitId);
        context.put("lessonId", lessonId);
        context.put("collectionId", collectionId);
        context.put("collectionType", collectionType);
        context.put("collectionSubType", collectionSubType);
        context.put("actionType", actionType);
        context.put("pathId", pathId);
        context.put("scorePercent", scorePercent);

        return context;
    }

    public UUID getClassId() {
        return classId;
    }

    public UUID getCourseId() {
        return courseId;
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

    public CollectionType getCollectionType() {
        return collectionType;
    }

    public void setCollectionType(CollectionType collectionType) {
        this.collectionType = collectionType;
    }

    public CollectionSubtype getCollectionSubType() {
        return collectionSubType;
    }

    public void setCollectionSubType(CollectionSubtype collectionSubType) {
        this.collectionSubType = collectionSubType;
    }

    public ActionType getActionType() {
        return actionType;
    }

    public void setActionType(ActionType actionType) {
        this.actionType = actionType;
    }

    public Long getPathId() {
        return pathId;
    }

    public void setPathId(Long pathId) {
        this.pathId = pathId;
    }

    public Double getScorePercent() {
        return scorePercent;
    }
}
