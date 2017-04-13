package org.gooru.navigatemap.processor.data;

import java.util.Objects;
import java.util.UUID;

import org.gooru.navigatemap.processor.context.ContextAttributes;

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

        context.put(ContextAttributes.CLASS_ID, Objects.toString(classId, null));
        context.put(ContextAttributes.COURSE_ID, courseId.toString());
        context.put(ContextAttributes.UNIT_ID, Objects.toString(unitId, null));
        context.put(ContextAttributes.LESSON_ID, Objects.toString(lessonId, null));
        context.put(ContextAttributes.COLLECTION_ID, Objects.toString(collectionId, null));
        context.put(ContextAttributes.COLLECTION_TYPE, collectionType != null ? collectionType.getName() : null);
        context
            .put(ContextAttributes.COLLECTION_SUBTYPE, collectionSubType != null ? collectionSubType.getName() : null);
        context.put(ContextAttributes.STATE, state.getName());
        context.put(ContextAttributes.PATH_ID, pathId);
        context.put(ContextAttributes.SCORE_PERCENT, scorePercent);
        context.put(ContextAttributes.CURRENT_ITEM_ID, Objects.toString(currentItemId, null));
        context.put(ContextAttributes.CURRENT_ITEM_TYPE, currentItemType != null ? currentItemType.getName() : null);
        context.put(ContextAttributes.CURRENT_ITEM_SUBTYPE,
            currentItemSubtype != null ? currentItemSubtype.getName() : null);
        return context;
    }

    public void setContentAddress(ContentAddress contentAddress) {
        this.unitId = UUID.fromString(contentAddress.getUnit());
        this.lessonId = UUID.fromString(contentAddress.getLesson());
        this.collectionId =
            contentAddress.getCollection() == null ? null : UUID.fromString(contentAddress.getCollection());
        this.currentItemId =
            contentAddress.getCollection() == null ? null : UUID.fromString(contentAddress.getCollection());
        this.currentItemType = contentAddress.getCollectionType();
        this.collectionType = contentAddress.getCollectionType();
        this.currentItemSubtype = contentAddress.getCollectionSubtype();
        this.collectionSubType = contentAddress.getCollectionSubtype();
        this.pathId = contentAddress.getPathId();
    }

    public void setContentAddressWithoutItem(ContentAddress contentAddress) {
        this.unitId = UUID.fromString(contentAddress.getUnit());
        this.lessonId = UUID.fromString(contentAddress.getLesson());
        this.collectionId =
            contentAddress.getCollection() == null ? null : UUID.fromString(contentAddress.getCollection());
        this.currentItemId = null;
        this.currentItemType = currentItemType;
        this.collectionType = contentAddress.getCollectionType();
        this.currentItemSubtype = null;
        this.collectionSubType = contentAddress.getCollectionSubtype();
        this.pathId = contentAddress.getPathId();
    }

    public void setCurrentItemAddress(UUID itemId, CollectionType itemType, CollectionSubtype itemSubtype) {
        this.currentItemId = itemId;
        this.currentItemType = itemType;
        this.currentItemSubtype = itemSubtype;
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
