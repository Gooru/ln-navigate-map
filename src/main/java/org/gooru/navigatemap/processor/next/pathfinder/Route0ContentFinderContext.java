package org.gooru.navigatemap.processor.next.pathfinder;

import java.util.UUID;

/**
 * @author ashish on 12/7/18.
 */
public class Route0ContentFinderContext {
    private UUID userId;
    private UUID classId;
    private UUID courseId;
    private UUID unitId;
    private UUID lessonId;
    private UUID collectionId;
    private String collectionType;
    private Long pathId;

    public static Route0ContentFinderContext buildFromPathFinderContext(PathFinderContext context) {
        Route0ContentFinderContext result = new Route0ContentFinderContext();
        result.setUserId(UUID.fromString(context.getUserId()));
        result.setClassId(context.getClassId());
        result.setCourseId(UUID.fromString(context.getContentAddress().getCourse()));
        result.setUnitId(UUID.fromString(context.getContentAddress().getUnit()));
        result.setLessonId(UUID.fromString(context.getContentAddress().getLesson()));
        result.setCollectionId(UUID.fromString(context.getContentAddress().getCurrentItem()));
        result.setCollectionType(context.getContentAddress().getCurrentItemType().getName());
        result.setPathId(context.getContentAddress().getPathId());
        return result;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public UUID getClassId() {
        return classId;
    }

    public void setClassId(UUID classId) {
        this.classId = classId;
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

    public String getCollectionType() {
        return collectionType;
    }

    public void setCollectionType(String collectionType) {
        this.collectionType = collectionType;
    }

    public Long getPathId() {
        return pathId;
    }

    public void setPathId(Long pathId) {
        this.pathId = pathId;
    }
}
