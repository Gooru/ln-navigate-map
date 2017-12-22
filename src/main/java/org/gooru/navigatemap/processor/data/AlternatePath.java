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
    private UUID ctxCourse;
    private UUID ctxUnit;
    private UUID ctxLesson;
    private UUID ctxCollection;
    private Long parentPathId;
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

    public UUID getCtxCourse() {
        return ctxCourse;
    }

    public void setCtxCourse(UUID ctxCourse) {
        this.ctxCourse = ctxCourse;
    }

    public UUID getCtxUnit() {
        return ctxUnit;
    }

    public void setCtxUnit(UUID ctxUnit) {
        this.ctxUnit = ctxUnit;
    }

    public UUID getCtxLesson() {
        return ctxLesson;
    }

    public void setCtxLesson(UUID ctxLesson) {
        this.ctxLesson = ctxLesson;
    }

    public UUID getCtxCollection() {
        return ctxCollection;
    }

    public void setCtxCollection(UUID ctxCollection) {
        this.ctxCollection = ctxCollection;
    }

    public Long getParentPathId() {
        return parentPathId;
    }

    public void setParentPathId(Long parentPathId) {
        this.parentPathId = parentPathId;
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
