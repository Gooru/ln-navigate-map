package org.gooru.navigatemap.processor.data;

/**
 * @author ashish on 3/3/17.
 */
public final class ContentAddress {
    private String course;
    private String unit;
    private String lesson;
    private String collection;
    private Long pathId;
    private CollectionType collectionType;
    private CollectionSubtype collectionSubtype;

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

    public CollectionType getCollectionType() {
        return collectionType;
    }

    public void setCollectionType(CollectionType collectionType) {
        this.collectionType = collectionType;
    }

    public CollectionSubtype getCollectionSubtype() {
        return collectionSubtype;
    }

    public void setCollectionSubtype(CollectionSubtype collectionSubtype) {
        this.collectionSubtype = collectionSubtype;
    }

    public Long getPathId() {
        return pathId;
    }

    public void setPathId(Long pathId) {
        this.pathId = pathId;
    }

    public boolean isOnAlternatePath() {
        return (pathId != null && pathId != 0);
    }

    public boolean isValidAddress() {
        return course != null && unit != null && lesson != null && collection != null;
    }

}
