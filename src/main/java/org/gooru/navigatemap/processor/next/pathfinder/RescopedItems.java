package org.gooru.navigatemap.processor.next.pathfinder;

import java.util.List;

/**
 * @author ashish
 */
public class RescopedItems {

  private List<String> units;
  private List<String> lessons;
  private List<String> assessments;
  private List<String> collections;
  private List<String> assessmentsExternal;
  private List<String> collectionsExternal;
  private List<String> offlineActivities;

  public List<String> getUnits() {
    return units;
  }

  public void setUnits(List<String> units) {
    this.units = units;
  }

  public List<String> getLessons() {
    return lessons;
  }

  public void setLessons(List<String> lessons) {
    this.lessons = lessons;
  }

  public List<String> getAssessments() {
    return assessments;
  }

  public void setAssessments(List<String> assessments) {
    this.assessments = assessments;
  }

  public List<String> getCollections() {
    return collections;
  }

  public void setCollections(List<String> collections) {
    this.collections = collections;
  }

  public List<String> getAssessmentsExternal() {
    return assessmentsExternal;
  }

  public void setAssessmentsExternal(List<String> assessmentsExternal) {
    this.assessmentsExternal = assessmentsExternal;
  }

  public List<String> getCollectionsExternal() {
    return collectionsExternal;
  }

  public void setCollectionsExternal(List<String> collectionsExternal) {
    this.collectionsExternal = collectionsExternal;
  }

  public boolean isUnitRescoped(String unitId) {
    return (units != null && !units.isEmpty() && units.contains(unitId));
  }

  public boolean isLessonRescoped(String lessonId) {
    return (lessons != null && !lessons.isEmpty() && lessons.contains(lessonId));
  }

  public boolean isAssessmentRescoped(String assessmentId) {
    return (assessments != null && !assessments.isEmpty() && assessments.contains(assessmentId));
  }

  public boolean isCollectionRescoped(String collectionId) {
    return (collections != null && !collections.isEmpty() && collections.contains(collectionId));
  }

  public boolean isAssessmentExternalRescoped(String assessmentId) {
    return (assessmentsExternal != null && !assessmentsExternal.isEmpty() && assessmentsExternal
        .contains(assessmentId));
  }

  public boolean isCollectionExternalRescoped(String collectionId) {
    return (collectionsExternal != null && !collectionsExternal.isEmpty() && collectionsExternal
        .contains(collectionId));
  }

  public boolean isOfflineActivityRescoped(String collectionId) {
    return (offlineActivities != null && !offlineActivities.isEmpty() && offlineActivities
        .contains(collectionId));
  }

  public List<String> getOfflineActivities() {
    return offlineActivities;
  }

  public void setOfflineActivities(List<String> offlineActivities) {
    this.offlineActivities = offlineActivities;
  }
}
