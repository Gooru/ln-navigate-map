package org.gooru.navigatemap.processor.data;

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
}
