package org.gooru.navigatemap.infra.data;

/**
 * @author ashish on 31/5/17.
 */
public class CollectionRQCount {
    private String collectionId;
    private int resourceCount;
    private int questionCount;

    public String getCollectionId() {
        return collectionId;
    }

    public void setCollectionId(String collectionId) {
        this.collectionId = collectionId;
    }

    public int getResourceCount() {
        return resourceCount;
    }

    public void setResourceCount(int resourceCount) {
        this.resourceCount = resourceCount;
    }

    public int getQuestionCount() {
        return questionCount;
    }

    public void setQuestionCount(int questionCount) {
        this.questionCount = questionCount;
    }
}
