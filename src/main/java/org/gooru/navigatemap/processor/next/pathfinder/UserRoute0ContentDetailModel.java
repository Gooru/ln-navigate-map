package org.gooru.navigatemap.processor.next.pathfinder;

import java.util.UUID;

import org.gooru.navigatemap.infra.data.ContentAddress;
import org.gooru.navigatemap.infra.data.CurrentItemType;
import org.gooru.navigatemap.infra.data.PathType;

/**
 * @author ashish.
 */
public class UserRoute0ContentDetailModel {

    private Long id;
    private long userRoute0ContentId;
    private UUID unitId;
    private String unitTitle;
    private int unitSequence;
    private UUID lessonId;
    private String lessonTitle;
    private int lessonSequence;
    private UUID collectionId;
    private CurrentItemType collectionType;
    private int collectionSequence;
    private int route0Sequence;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public long getUserRoute0ContentId() {
        return userRoute0ContentId;
    }

    public void setUserRoute0ContentId(long userRoute0ContentId) {
        this.userRoute0ContentId = userRoute0ContentId;
    }

    public UUID getUnitId() {
        return unitId;
    }

    public void setUnitId(UUID unitId) {
        this.unitId = unitId;
    }

    public String getUnitTitle() {
        return unitTitle;
    }

    public void setUnitTitle(String unitTitle) {
        this.unitTitle = unitTitle;
    }

    public int getUnitSequence() {
        return unitSequence;
    }

    public void setUnitSequence(int unitSequence) {
        this.unitSequence = unitSequence;
    }

    public UUID getLessonId() {
        return lessonId;
    }

    public void setLessonId(UUID lessonId) {
        this.lessonId = lessonId;
    }

    public String getLessonTitle() {
        return lessonTitle;
    }

    public void setLessonTitle(String lessonTitle) {
        this.lessonTitle = lessonTitle;
    }

    public int getLessonSequence() {
        return lessonSequence;
    }

    public void setLessonSequence(int lessonSequence) {
        this.lessonSequence = lessonSequence;
    }

    public UUID getCollectionId() {
        return collectionId;
    }

    public void setCollectionId(UUID collectionId) {
        this.collectionId = collectionId;
    }

    public CurrentItemType getCollectionType() {
        return collectionType;
    }

    public void setCollectionType(CurrentItemType collectionType) {
        this.collectionType = collectionType;
    }

    public int getCollectionSequence() {
        return collectionSequence;
    }

    public void setCollectionSequence(int collectionSequence) {
        this.collectionSequence = collectionSequence;
    }

    public int getRoute0Sequence() {
        return route0Sequence;
    }

    public void setRoute0Sequence(int route0Sequence) {
        this.route0Sequence = route0Sequence;
    }

    /**
     * Note: This just denotes ContentAddress and there is no guarantee that this will actually exist.
     *
     * For R0 since we UL are actually placeholders, if the following object is used as validator to locate the content,
     * it will fail.
     * @param courseId
     * @return
     */
    public ContentAddress asContentAddress(String courseId) {
        ContentAddress result = new ContentAddress();
        result.setCourse(courseId);
        result.setUnit(unitId.toString());
        result.setLesson(lessonId.toString());
        result.setCollection(collectionId.toString());
        result.setCurrentItem(collectionId.toString());
        result.setCurrentItemType(collectionType);
        result.setPathId(id);
        result.setPathType(PathType.Route0);
        return result;
    }


}
