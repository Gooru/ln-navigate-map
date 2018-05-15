package org.gooru.navigatemap.infra.data;

/**
 * @author ashish on 4/4/17.
 */
public final class SuggestionCard {
    private String id;
    private String title;
    private String format;
    private String subformat;
    private String thumbnail;
    private String metadata;
    private String taxonomy;
    private String suggestedContentSubType;
    private int resourceCount;
    private int questionCount;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public String getSubformat() {
        return subformat;
    }

    public void setSubformat(String subformat) {
        this.subformat = subformat;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getMetadata() {
        return metadata;
    }

    public void setMetadata(String metadata) {
        this.metadata = metadata;
    }

    public String getTaxonomy() {
        return taxonomy;
    }

    public void setTaxonomy(String taxonomy) {
        this.taxonomy = taxonomy;
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

    public String getSuggestedContentSubType() {
        return suggestedContentSubType;
    }

    public void setSuggestedContentSubType(String suggestedContentSubType) {
        this.suggestedContentSubType = suggestedContentSubType;
    }
}
