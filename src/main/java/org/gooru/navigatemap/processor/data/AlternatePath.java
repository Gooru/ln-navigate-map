package org.gooru.navigatemap.processor.data;

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
    private int pathIndex;
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

    public void setSuggestedContentSubtype(String suggestedContentSubtype) {
        this.suggestedContentSubtype = suggestedContentSubtype;
    }

    public int getPathIndex() {
        return pathIndex;
    }

    public void setPathIndex(int pathIndex) {
        this.pathIndex = pathIndex;
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

    public boolean isSuggestedContentPreTest() {
        return Objects.equals(suggestedContentSubtype, "pre-test");
    }

    public boolean isSuggestedContentPostTest() {
        return Objects.equals(suggestedContentSubtype, "post-test");
    }

    public boolean isSuggestedContentBenchmark() {
        return Objects.equals(suggestedContentSubtype, "benchmark");
    }

}
