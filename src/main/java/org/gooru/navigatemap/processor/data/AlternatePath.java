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
    private String parentPathType;
    private UUID targetCourse;
    private UUID targetUnit;
    private UUID targetLesson;
    private UUID targetCollection;
    private String targetContentType;
    private String targetContentSubtype;

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

    public String getParentPathType() {
        return parentPathType;
    }

    public void setParentPathType(String parentPathType) {
        this.parentPathType = parentPathType;
    }

    public UUID getTargetCourse() {
        return targetCourse;
    }

    public void setTargetCourse(UUID targetCourse) {
        this.targetCourse = targetCourse;
    }

    public UUID getTargetUnit() {
        return targetUnit;
    }

    public void setTargetUnit(UUID targetUnit) {
        this.targetUnit = targetUnit;
    }

    public UUID getTargetLesson() {
        return targetLesson;
    }

    public void setTargetLesson(UUID targetLesson) {
        this.targetLesson = targetLesson;
    }

    public UUID getTargetCollection() {
        return targetCollection;
    }

    public void setTargetCollection(UUID targetCollection) {
        this.targetCollection = targetCollection;
    }

    public String getTargetContentType() {
        return targetContentType;
    }

    public boolean isTargetContentTypeLesson() {
        return Objects.equals(targetContentType, "lesson");
    }

    public boolean isTargetContentTypeCollection() {
        return Objects.equals(targetContentType, "collection");
    }

    public boolean isTargetContentTypeAssessment() {
        return Objects.equals(targetContentType, "assessment");
    }

    public void setTargetContentType(String targetContentType) {
        this.targetContentType = targetContentType;
    }

    public String getTargetContentSubtype() {
        return targetContentSubtype;
    }

    public boolean isTargetContentSubtypePreTest() {
        return Objects.equals(targetContentSubtype, "pre-test");
    }

    public boolean isTargetContentSubtypePostTest() {
        return Objects.equals(targetContentSubtype, "post-test");
    }

    public boolean isTargetContentSubtypeBenchmark() {
        return Objects.equals(targetContentSubtype, "benchmark");
    }

    public void setTargetContentSubtype(String targetContentSubtype) {
        this.targetContentSubtype = targetContentSubtype;
    }

    public UUID getClassId() {
        return classId;
    }

    public void setClassId(UUID classId) {
        this.classId = classId;
    }

}
