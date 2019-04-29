package org.gooru.navigatemap.infra.data;

/**
 * @author ashish on 3/3/17.
 */
public final class ContentAddress {

  private String course;
  private String unit;
  private String lesson;
  private String collection;
  private String milestoneId;
  private String visibility;
  private Long pathId;
  private PathType pathType;
  private String currentItem;
  private CurrentItemType currentItemType;
  private CurrentItemSubtype currentItemSubtype;

  public ContentAddress() {
  }

  public ContentAddress(ContentAddress address) {
    this.course = address.course;
    this.unit = address.unit;
    this.lesson = address.lesson;
    this.collection = address.collection;
    this.milestoneId = address.milestoneId;
    this.pathId = address.pathId;
    this.pathType = address.pathType;
    this.currentItem = address.currentItem;
    this.currentItemType = address.currentItemType;
    this.currentItemSubtype = address.currentItemSubtype;
    this.visibility = address.visibility;
  }

  public String getCourse() {
    return course;
  }

  public void setCourse(String course) {
    this.course = course;
  }

  public String getUnit() {
    return unit;
  }

  public void setUnit(String unit) {
    this.unit = unit;
  }

  public String getLesson() {
    return lesson;
  }

  public void setLesson(String lesson) {
    this.lesson = lesson;
  }

  public String getCollection() {
    return collection;
  }

  public void setCollection(String collection) {
    this.collection = collection;
  }

  public Long getPathId() {
    return pathId;
  }

  public PathType getPathType() {
    return pathType;
  }

  public String getMilestoneId() {
    return milestoneId;
  }

  public void setMilestoneId(String milestoneId) {
    this.milestoneId = milestoneId;
  }

  public void setPathType(PathType pathType) {
    this.pathType = pathType;
  }

  public void setPathId(Long pathId) {
    this.pathId = pathId;
  }

  public String getCurrentItem() {
    return currentItem;
  }

  public void setCurrentItem(String currentItem) {
    this.currentItem = currentItem;
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

  public String getVisibility() {
    return visibility;
  }

  public void setVisibility(String visibility) {
    this.visibility = visibility;
  }

  public void setCurrentItemSubtype(CurrentItemSubtype currentItemSubtype) {
    this.currentItemSubtype = currentItemSubtype;
  }

  public boolean isOnTeacherOrSystemPath() {
    return isOnSystemPath() || isOnTeacherPath();
  }

  public boolean isOnTeacherPath() {
    return (pathId != null && pathId > 0 && pathType == PathType.Teacher);
  }

  public boolean isOnSystemPath() {
    return (pathId != null && pathId > 0 && pathType == PathType.System);
  }

  public boolean isMilestoneAware() {
    return milestoneId != null;
  }

  public boolean isOnRoute0() {
    return pathType == PathType.Route0;
  }

  public boolean isOnMainPath() {
    return (pathId == null || pathId == 0) && pathType == null;
  }

  public boolean isCurrentItemSignatureAssessment() {
    return isOnSystemPath() && getCurrentItemSubtype() == CurrentItemSubtype.SignatureAssessment;
  }

  public boolean isCurrentItemSignatureCollection() {
    return isOnSystemPath() && getCurrentItemSubtype() == CurrentItemSubtype.SignatureCollection;
  }

  public boolean isValidAddress() {
    return course != null && unit != null && lesson != null && collection != null;
  }

}
