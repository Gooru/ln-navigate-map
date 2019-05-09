package org.gooru.navigatemap.infra.data;

import java.util.Objects;
import java.util.UUID;

/**
 * @author ashish on 6/4/17.
 */
public final class AlternatePath {

  private Long id;
  private UUID userId;
  private UUID classId;
  private UUID courseId;
  private UUID unitId;
  private UUID lessonId;
  private UUID collectionId;
  private String suggestionType;
  private long serveCount;

  private UUID suggestedContentId;
  private String suggestedContentType;
  private String suggestedContentSubtype;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public UUID getUserId() {
    return userId;
  }

  public void setUserId(UUID userId) {
    this.userId = userId;
  }

  public UUID getCourseId() {
    return courseId;
  }

  public void setCourseId(UUID courseId) {
    this.courseId = courseId;
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

  public UUID getClassId() {
    return classId;
  }

  public void setClassId(UUID classId) {
    this.classId = classId;
  }

  public UUID getSuggestedContentId() {
    return suggestedContentId;
  }

  public void setSuggestedContentId(UUID suggestedContentId) {
    this.suggestedContentId = suggestedContentId;
  }

  public String getSuggestedContentType() {
    return suggestedContentType;
  }

  public void setSuggestedContentType(String suggestedContentType) {
    this.suggestedContentType = suggestedContentType;
  }

  public String getSuggestedContentSubtype() {
    return suggestedContentSubtype;
  }

  public boolean isSuggestionSignatureAssessment() {
    if (suggestedContentSubtype != null) {
      return SuggestedContentSubType.builder(suggestedContentSubtype)
          == SuggestedContentSubType.SignatureAssessment;
    }
    return false;
  }

  public boolean isSuggestionSignatureCollection() {
    if (suggestedContentSubtype != null) {
      return SuggestedContentSubType.builder(suggestedContentSubtype)
          == SuggestedContentSubType.SignatureCollection;
    }
    return false;
  }

  public void setSuggestedContentSubtype(String suggestedContentSubtype) {
    this.suggestedContentSubtype = suggestedContentSubtype;
  }

  public String getSuggestionType() {
    return suggestionType;
  }

  public void setSuggestionType(String suggestionType) {
    this.suggestionType = suggestionType;
  }

  public long getServeCount() {
    return serveCount;
  }

  public void setServeCount(long serveCount) {
    this.serveCount = serveCount;
  }

  public boolean isSuggestedContentLesson() {
    return Objects.equals(suggestedContentType, "lesson");
  }

  public boolean isSuggestedContentCollection() {
    return Objects.equals(suggestedContentType, "collection");
  }

  public boolean isSuggestedContentAssessment() {
    return Objects.equals(suggestedContentType, "assessment");
  }

  public boolean isSuggestionTeacherSuggestion() {
    return Objects.equals(suggestionType, "teacher");
  }

  public boolean isSuggestionSystemSuggestion() {
    return Objects.equals(suggestionType, "system");
  }

  public boolean isSuggestionRoute0Suggestion() {
    return Objects.equals(suggestionType, "route0");
  }

  public ContentAddress toContentAddress() {
    return toContentAddress(null);
  }

  public ContentAddress toContentAddress(String milestoneId) {
    ContentAddress result = new ContentAddress();
    result.setCourse(courseId.toString());
    result.setUnit(Objects.toString(unitId, null));
    result.setLesson(Objects.toString(lessonId, null));
    result.setCollection(Objects.toString(collectionId, null));
    result.setMilestoneId(milestoneId);
    result.setPathId(id);
    result.setPathType(suggestionType != null ? PathType.builder(suggestionType) : null);
    result.setCurrentItem(Objects.toString(suggestedContentId, null));
    result.setCurrentItemType(
        suggestedContentType != null ? CurrentItemType.builder(suggestedContentType) : null);
    result.setCurrentItemSubtype(
        suggestedContentSubtype != null ? CurrentItemSubtype.builder(suggestedContentSubtype)
            : null);
    return result;
  }

}
