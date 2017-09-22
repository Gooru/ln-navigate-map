package org.gooru.navigatemap.processor.data;

/**
 * @author ashish on 22/9/17.
 */
public final class SignatureResource {
    private String resourceId;
    private String resourceType;
    private String publisher;
    private String language;
    private Double engagement;
    private Double efficacy;
    private Double weight;
    private Long suggestedCount;

    public String getResourceId() {
        return resourceId;
    }

    public void setResourceId(String resourceId) {
        this.resourceId = resourceId;
    }

    public String getResourceType() {
        return resourceType;
    }

    public void setResourceType(String resourceType) {
        this.resourceType = resourceType;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public Double getEngagement() {
        return engagement;
    }

    public void setEngagement(Double engagement) {
        this.engagement = engagement;
    }

    public Double getEfficacy() {
        return efficacy;
    }

    public void setEfficacy(Double efficacy) {
        this.efficacy = efficacy;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    public Long getSuggestedCount() {
        return suggestedCount;
    }

    public void setSuggestedCount(Long suggestedCount) {
        this.suggestedCount = suggestedCount;
    }
}
