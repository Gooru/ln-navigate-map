package org.gooru.navigatemap.processor.contentserver;

import org.gooru.navigatemap.processor.data.CurrentItemType;

/**
 * @author ashish on 16/5/17.
 */
public class RemoteUriLocator {
    private final String assessmentUri;
    private final String collectionUri;
    private final String resourceUri;
    private final String assessmentExternalUri;

    public RemoteUriLocator(String assessmentUri, String collectionUri, String resourceUri,
        String assessmentExternalUri) {
        this.assessmentUri = assessmentUri;
        this.collectionUri = collectionUri;
        this.resourceUri = resourceUri;
        this.assessmentExternalUri = assessmentExternalUri;
    }

    public String getUriForItemType(CurrentItemType itemType) {
        if (itemType == CurrentItemType.Collection) {
            return collectionUri;
        } else if (itemType == CurrentItemType.Assessment) {
            return assessmentUri;
        } else if (itemType == CurrentItemType.Resource) {
            return resourceUri;
        } else if (itemType == CurrentItemType.AssessmentExternal) {
            return assessmentExternalUri;
        }
        return null;
    }

}
