package org.gooru.navigatemap.processor.postprocessor;

import java.util.Date;
import java.util.UUID;

/**
 * @author ashish on 25/7/18.
 */
public class SuggestionTrackerModel {
    private Long id;
    private UUID userId;
    private UUID courseId;
    private UUID unitId;
    private UUID lessonId;
    private UUID classId;
    private UUID collectionId;
    private UUID suggestedContentId;
    private String suggestionOrigin;
    private String suggestionOriginatorId;
    private String suggestionCriteria;
    private String suggestedContentType;
    private String suggestedTo;
    private boolean accepted;
    private Date acceptedAt;
    private String ctx;

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

    public UUID getClassId() {
        return classId;
    }

    public void setClassId(UUID classId) {
        this.classId = classId;
    }

    public UUID getCollectionId() {
        return collectionId;
    }

    public void setCollectionId(UUID collectionId) {
        this.collectionId = collectionId;
    }

    public UUID getSuggestedContentId() {
        return suggestedContentId;
    }

    public void setSuggestedContentId(UUID suggestedContentId) {
        this.suggestedContentId = suggestedContentId;
    }

    public String getSuggestionOrigin() {
        return suggestionOrigin;
    }

    public void setSuggestionOrigin(String suggestionOrigin) {
        this.suggestionOrigin = suggestionOrigin;
    }

    public String getSuggestionOriginatorId() {
        return suggestionOriginatorId;
    }

    public void setSuggestionOriginatorId(String suggestionOriginatorId) {
        this.suggestionOriginatorId = suggestionOriginatorId;
    }

    public String getSuggestionCriteria() {
        return suggestionCriteria;
    }

    public void setSuggestionCriteria(String suggestionCriteria) {
        this.suggestionCriteria = suggestionCriteria;
    }

    public String getSuggestedContentType() {
        return suggestedContentType;
    }

    public void setSuggestedContentType(String suggestedContentType) {
        this.suggestedContentType = suggestedContentType;
    }

    public String getSuggestedTo() {
        return suggestedTo;
    }

    public void setSuggestedTo(String suggestedTo) {
        this.suggestedTo = suggestedTo;
    }

    public boolean isAccepted() {
        return accepted;
    }

    public void setAccepted(boolean accepted) {
        this.accepted = accepted;
    }

    public Date getAcceptedAt() {
        return acceptedAt;
    }

    public void setAcceptedAt(Date acceptedAt) {
        this.acceptedAt = acceptedAt;
    }

    public String getCtx() {
        return ctx;
    }

    public void setCtx(String ctx) {
        this.ctx = ctx;
    }
}
