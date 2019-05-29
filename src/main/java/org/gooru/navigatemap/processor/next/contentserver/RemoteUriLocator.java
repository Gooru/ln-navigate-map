package org.gooru.navigatemap.processor.next.contentserver;

import org.gooru.navigatemap.infra.data.CurrentItemType;

/**
 * @author ashish on 16/5/17.
 */
public class RemoteUriLocator {

  private final String assessmentUri;
  private final String collectionUri;
  private final String assessmentExternalUri;
  private final String collectionExternalUri;
  private final String offlineActivityUri;

  public RemoteUriLocator(String assessmentUri, String collectionUri,
      String assessmentExternalUri, String collectionExternalUri,
      String offlineActivityUri) {
    this.assessmentUri = assessmentUri;
    this.collectionUri = collectionUri;
    this.assessmentExternalUri = assessmentExternalUri;
    this.collectionExternalUri = collectionExternalUri;
    this.offlineActivityUri = offlineActivityUri;
  }

  public String getUriForItemType(CurrentItemType itemType) {
    if (itemType == CurrentItemType.Collection) {
      return collectionUri;
    } else if (itemType == CurrentItemType.Assessment) {
      return assessmentUri;
    } else if (itemType == CurrentItemType.AssessmentExternal) {
      return assessmentExternalUri;
    } else if (itemType == CurrentItemType.CollectionExternal) {
      return collectionExternalUri;
    } else if (itemType == CurrentItemType.OfflineActivity) {
      return offlineActivityUri;
    }
    return null;
  }

}
