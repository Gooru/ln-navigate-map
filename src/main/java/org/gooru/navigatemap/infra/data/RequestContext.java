package org.gooru.navigatemap.infra.data;

import io.vertx.core.json.JsonObject;
import java.util.UUID;
import org.gooru.navigatemap.app.constants.HttpConstants;
import org.gooru.navigatemap.app.exceptions.HttpResponseWrapperException;
import org.gooru.navigatemap.infra.data.context.ContextAttributes;
import org.gooru.navigatemap.infra.data.context.RouteContextData;

/**
 * @author ashish on 26/2/17.
 */
public final class RequestContext {

  private UUID classId;
  private UUID courseId;
  private UUID unitId;
  private UUID lessonId;
  private UUID collectionId;
  private String milestoneId;

  private UUID currentItemId;
  private CurrentItemType currentItemType;
  private CurrentItemSubtype currentItemSubtype;

  private State state;
  private Long pathId;
  private PathType pathType;
  private Double scorePercent;

  private RouteContextData routeContextData;

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

  public String getMilestoneId() {
    return milestoneId;
  }

  public State getState() {
    return state;
  }

  public Long getPathId() {
    return pathId;
  }

  public PathType getPathType() {
    return pathType;
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

  public RouteContextData getRouteContextData() {
    return routeContextData;
  }

  public boolean needsLastState() {
    return (state == State.Continue);
  }

  public boolean explicitStartRequested() {
    return (state == State.Start);
  }

  public boolean onRoute0() {
    return (pathType == PathType.Route0 && isValidPath()) ||
        (pathType == PathType.Route0 && needToStartLesson());
  }

  public boolean isContextIL() {
    return classId == null;
  }

  public boolean isContextInClass() {
    return classId != null;
  }

  public boolean needToStartCollection() {
    return (state == State.Start && currentItemId != null);
  }

  public boolean isCourseCompleted() {
    return state == State.Done;
  }

  public boolean onMainPath() {
    return (!isValidPath() && getPathType() == null);
  }

  public boolean onTeacherPath() {
    return (isValidPath() && getPathType() == PathType.Teacher);
  }

  public boolean onSystemPath() {
    return (isValidPath() && getPathType() == PathType.System);
  }

  public boolean needToStartCourse() {
    return getState() == State.Continue;
  }

  public boolean userWasSuggestedAnItem() {
    return getState() == State.ContentEndSuggested;
  }

  public boolean needToStartLesson() {
    return (state == State.Start && currentItemId == null);
  }

  private boolean isValidPath() {
    return pathId != null && pathId != 0;
  }

  private void validate() {
    if (courseId == null) {
      throw new HttpResponseWrapperException(HttpConstants.HttpStatus.BAD_REQUEST,
          "Invalid course id");
    }
    if (((unitId == null || lessonId == null) && state == State.Start)) {
      throw new HttpResponseWrapperException(HttpConstants.HttpStatus.BAD_REQUEST,
          "Invalid context for Start flow");
    }

    if (pathId != null && pathId > 0) {
      if (pathType == null) {
        throw new HttpResponseWrapperException(HttpConstants.HttpStatus.BAD_REQUEST,
            "Invalid context, pathType should be present for pathId, if pathId is present");
      }
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
      context.milestoneId = input.getString(ContextAttributes.MILESTONE_ID);
      if (context.scorePercent == null) {
        context.scorePercent = 0D;
      }
      String value;
      value = input.getString(ContextAttributes.CURRENT_ITEM_TYPE);
      context.currentItemType =
          (value != null && !value.isEmpty()) ? CurrentItemType.builder(value) : null;
      value = input.getString(ContextAttributes.CURRENT_ITEM_SUBTYPE);
      context.currentItemSubtype =
          (value != null && !value.isEmpty()) ? CurrentItemSubtype.builder(value) : null;
      value = input.getString(ContextAttributes.STATE);
      context.state = State.builder(value);
      value = input.getString(ContextAttributes.PATH_TYPE);
      context.pathType = (value != null && !value.isEmpty()) ? PathType.builder(value) : null;
      context.routeContextData = new RouteContextData(
          input.getString(ContextAttributes.CONTEXT_DATA));
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
