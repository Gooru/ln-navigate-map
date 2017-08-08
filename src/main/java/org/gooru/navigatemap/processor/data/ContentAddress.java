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
        this.pathId = address.pathId;
        this.collectionType = address.collectionType;
        this.collectionSubtype = address.collectionSubtype;
        this.currentItem = address.currentItem;
        this.currentItemType = address.currentItemType;
        this.currentItemSubtype = address.currentItemSubtype;
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

    public void setCurrentItemSubtype(CurrentItemSubtype currentItemSubtype) {
        this.currentItemSubtype = currentItemSubtype;
    }

    public boolean isOnAlternatePath() {
        return (pathId != null && pathId != 0);
    }

    public boolean isValidAddress() {
        return course != null && unit != null && lesson != null && collection != null;
    }

    public boolean isOnAlternatePathAtLessonEnd() {
        return isOnAlternatePath() && currentItemSubtype != null && (CurrentItemSubtype.BenchMark == currentItemSubtype
            || CurrentItemSubtype.PostTest == currentItemSubtype);
    }

    public void populateCurrentItemsFromCollections() {
        this.currentItem = collection;
        this.currentItemType = collectionType == null ? null : CurrentItemType.builder(collectionType.getName());
        this.currentItemSubtype =
            collectionSubtype == null ? null : CurrentItemSubtype.builder(collectionSubtype.getName());

    }
}
