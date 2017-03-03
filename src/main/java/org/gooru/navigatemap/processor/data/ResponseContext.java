package org.gooru.navigatemap.processor.data;

import java.util.UUID;

import io.vertx.core.json.JsonObject;

/**
 * @author ashish on 28/2/17.
 */
public final class ResponseContext {
    private final UUID classId;
    private final UUID courseId;
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
    private final Double scorePercent;

    public ResponseContext(RequestContext context) {
        this.classId = context.getClassId();
        this.courseId = context.getCourseId();
        this.unitId = context.getUnitId();
        this.lessonId = context.getLessonId();
        this.collectionId = context.getCollectionId();
        this.collectionType = context.getCollectionType();
        this.collectionSubType = context.getCollectionSubType();
        this.state = context.getState();
        this.pathId = context.getPathId();
        this.scorePercent = context.getScorePercent();
        this.currentItemId = context.getCurrentItemId();
        this.currentItemType = context.getCurrentItemType();
        this.currentItemSubtype = context.getCurrentItemSubtype();
    }

    public JsonObject toJson() {
        JsonObject context = new JsonObject();

        context.put("class_id", classId);
        context.put("course_id", courseId);
        context.put("unit_id", unitId);
        context.put("lesson_id", lessonId);
        context.put("collection_id", collectionId);
        context.put("collection_type", collectionType.getName());
        context.put("collection_subtype", collectionSubType.getName());
        context.put("state", state.getName());
        context.put("path_id", pathId);
        context.put("score_percent", scorePercent);
        context.put("current_item_id", currentItemId);
        context.put("current_item_type", currentItemType);
        context.put("current_item_subtype", currentItemSubtype);
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

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
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

    public UUID getCurrentItemId() {
        return currentItemId;
    }

    public void setCurrentItemId(UUID currentItemId) {
        this.currentItemId = currentItemId;
    }

    public CollectionType getCurrentItemType() {
        return currentItemType;
    }

    public void setCurrentItemType(CollectionType currentItemType) {
        this.currentItemType = currentItemType;
    }

    public CollectionSubtype getCurrentItemSubtype() {
        return currentItemSubtype;
    }

    public void setCurrentItemSubtype(CollectionSubtype currentItemSubtype) {
        this.currentItemSubtype = currentItemSubtype;
    }
}
