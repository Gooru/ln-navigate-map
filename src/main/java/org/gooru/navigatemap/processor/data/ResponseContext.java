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

        context.put("class_id", classId != null ? classId.toString() : null);
        context.put("course_id", courseId.toString());
        context.put("unit_id", unitId.toString());
        context.put("lesson_id", lessonId.toString());
        context.put("collection_id", collectionId != null ? collectionId.toString() : null);
        context.put("collection_type", collectionType != null ? collectionType.getName() : null);
        context.put("collection_subtype", collectionSubType != null ? collectionSubType.getName() : null);
        context.put("state", state.getName());
        context.put("path_id", pathId);
        context.put("score_percent", scorePercent);
        context.put("current_item_id", currentItemId != null ? currentItemId.toString() : null);
        context.put("current_item_type", currentItemType != null ? currentItemType.getName() : null);
        context.put("current_item_subtype", currentItemSubtype != null ? currentItemSubtype.getName() : null);
        return context;
    }

    public void setContentAddress(ContentAddress contentAddress) {
        this.unitId = UUID.fromString(contentAddress.getUnit());
        this.lessonId = UUID.fromString(contentAddress.getLesson());
        this.collectionId = UUID.fromString(contentAddress.getCollection());
        this.currentItemId = UUID.fromString(contentAddress.getCollection());
        this.currentItemType = contentAddress.getCollectionType();
        this.collectionType = contentAddress.getCollectionType();
        this.currentItemSubtype = contentAddress.getCollectionSubtype();
        this.collectionSubType = contentAddress.getCollectionSubtype();
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
