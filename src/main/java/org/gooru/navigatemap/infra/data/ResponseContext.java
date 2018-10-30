package org.gooru.navigatemap.infra.data;

import io.vertx.core.json.JsonObject;
import java.util.Objects;
import java.util.UUID;
import org.gooru.navigatemap.infra.data.context.ContextAttributes;
import org.gooru.navigatemap.infra.data.context.RouteContextData;

/**
 * @author ashish on 28/2/17.
 */
public final class ResponseContext {

  private final UUID classId;
  private final UUID courseId;
  private UUID unitId;
  private UUID lessonId;
  private UUID collectionId;
  private UUID currentItemId;
  private CurrentItemType currentItemType;
  private CurrentItemSubtype currentItemSubtype;
  private State state;
  private Long pathId;
  private String pathType;
  private final Double scorePercent;
  private RouteContextData routeContextData;

  public ResponseContext(RequestContext context) {
    this.classId = context.getClassId();
    this.courseId = context.getCourseId();
    this.unitId = context.getUnitId();
    this.lessonId = context.getLessonId();
    this.collectionId = context.getCollectionId();
    this.state = context.getState();
    this.pathId = context.getPathId();
    this.pathType = context.getPathType() != null ? context.getPathType().getName() : null;
    this.scorePercent = 0D;
    this.currentItemId = context.getCurrentItemId();
    this.currentItemType = context.getCurrentItemType();
    this.currentItemSubtype = context.getCurrentItemSubtype();
    this.routeContextData = context.getRouteContextData();
  }

  public JsonObject toJson() {
    JsonObject context = new JsonObject();

    context.put(ContextAttributes.CLASS_ID, Objects.toString(classId, null));
    context.put(ContextAttributes.COURSE_ID, courseId.toString());
    context.put(ContextAttributes.UNIT_ID, Objects.toString(unitId, null));
    context.put(ContextAttributes.LESSON_ID, Objects.toString(lessonId, null));
    context.put(ContextAttributes.COLLECTION_ID, Objects.toString(collectionId, null));
    context.put(ContextAttributes.STATE, state.getName());
    context.put(ContextAttributes.PATH_ID, pathId);
    context.put(ContextAttributes.PATH_TYPE, pathType);
    context.put(ContextAttributes.SCORE_PERCENT, scorePercent);
    context.put(ContextAttributes.CURRENT_ITEM_ID, Objects.toString(currentItemId, null));
    context.put(ContextAttributes.CURRENT_ITEM_TYPE,
        currentItemType != null ? currentItemType.getName() : null);
    context.put(ContextAttributes.CURRENT_ITEM_SUBTYPE,
        currentItemSubtype != null ? currentItemSubtype.getName() : null);
    context.put(ContextAttributes.CONTEXT_DATA, routeContextData.encode());
    return context;
  }

  public void setContentAddress(ContentAddress contentAddress) {
    this.unitId = UUID.fromString(contentAddress.getUnit());
    this.lessonId = UUID.fromString(contentAddress.getLesson());

    this.collectionId =
        contentAddress.getCollection() == null ? null
            : UUID.fromString(contentAddress.getCollection());
    this.currentItemId =
        contentAddress.getCurrentItem() == null ? null
            : UUID.fromString(contentAddress.getCurrentItem());
    this.currentItemType = contentAddress.getCurrentItemType();
    this.currentItemSubtype = contentAddress.getCurrentItemSubtype();

    this.pathId = contentAddress.getPathId();
    this.pathType =
        contentAddress.getPathType() != null ? contentAddress.getPathType().getName() : null;
  }

  public void setCurrentItemAddress(UUID itemId, CurrentItemType itemType,
      CurrentItemSubtype itemSubtype) {
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

  public String getPathType() {
    return pathType;
  }

  public void setPathType(String pathType) {
    this.pathType = pathType;
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

  public CurrentItemType getCurrentItemType() {
    return currentItemType;
  }

  public void setCurrentItemType(CurrentItemType currentItemType) {
    this.currentItemType = currentItemType;
  }

  public CurrentItemSubtype getCurrentItemSubtype() {
    return currentItemSubtype;
  }

  public void setCurrentItemSubtype(CurrentItemSubtype currentItemSubtype) {
    this.currentItemSubtype = currentItemSubtype;
  }

}
