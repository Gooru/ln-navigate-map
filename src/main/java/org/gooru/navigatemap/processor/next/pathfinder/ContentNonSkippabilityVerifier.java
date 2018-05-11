package org.gooru.navigatemap.processor.next.pathfinder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.gooru.navigatemap.infra.data.ContentAddress;
import org.gooru.navigatemap.infra.utilities.CollectionUtils;
import org.skife.jdbi.v2.DBI;

/**
 * @author ashish on 8/5/18.
 */
class ContentNonSkippabilityVerifier implements ContentVerifier {

    private final String user;
    private final ContentFinderDao finderDao;
    private final UserCompetencyCompletionDao userCompetencyCompletionDao;

    private ContentNonSkippabilityVerifier(DBI dbi, String user) {
        this.finderDao = dbi.onDemand(ContentFinderDao.class);
        this.userCompetencyCompletionDao = dbi.onDemand(UserCompetencyCompletionDao.class);
        this.user = user;
    }

    static ContentNonSkippabilityVerifier build(DBI dbi, String user) {
        return new ContentNonSkippabilityVerifier(dbi, user);
    }

    @Override
    public boolean isContentVerified(ContentAddress contentAddress) {
        List<List<String>> listOfListOfComps = finderDao
            .findCompetenciesForCollection(contentAddress.getCourse(), contentAddress.getUnit(),
                contentAddress.getLesson(), contentAddress.getCollection());

        List<String> competencyList =
            (listOfListOfComps != null && !listOfListOfComps.isEmpty()) ? listOfListOfComps.get(0) :
                Collections.emptyList();

        if (competencyList.isEmpty()) {
            return true;
        } else {
            List<String> completedCompetenciesByUser = userCompetencyCompletionDao
                .findCompletedOrMasteredCompetenciesForUserInGivenList(user,
                    CollectionUtils.convertToSqlArrayOfString(competencyList));
            competencyList.removeAll(completedCompetenciesByUser);
            return !competencyList.isEmpty();
        }
    }

    @Override
    public ContentAddress findFirstVerifiedContent(List<ContentAddress> contentAddresses) {
        // NOTE: Currently this implementation does one query for one content item. If there are way too many
        // skippable items, it might be more performant to query all the content in batches and do processing
        if (contentAddresses != null && !contentAddresses.isEmpty()) {
            for (ContentAddress address : contentAddresses) {
                if (isContentVerified(address)) {
                    return address;
                }
            }
        }
        return null;
    }

    @Override
    public List<ContentAddress> filterVerifiedContent(List<ContentAddress> contentAddresses) {
        // NOTE: Currently this implementation does one query for one content item. If there are way too many
        // skippable items, it might be more performant to query all the content in batches and do processing
        List<ContentAddress> result = new ArrayList<>();
        if (contentAddresses != null && !contentAddresses.isEmpty()) {
            for (ContentAddress address : contentAddresses) {
                if (isContentVerified(address)) {
                    result.add(address);
                }
            }
        }
        return result;
    }
}
