package org.gooru.navigatemap.processor.next.pathfinder;

import java.util.List;

import org.gooru.navigatemap.infra.data.ContentAddress;

/**
 * The object which can verify if the content is verified and thus is valid to be served as next content item in the
 * algorithm.
 *
 * @author ashish on 8/5/18.
 */
interface ContentVerifier {

    boolean isContentVerified(ContentAddress contentAddress);

    ContentAddress findFirstVerifiedContent(List<ContentAddress> contentAddresses);

    List<ContentAddress> filterVerifiedContent(List<ContentAddress> contentAddresses);

}
