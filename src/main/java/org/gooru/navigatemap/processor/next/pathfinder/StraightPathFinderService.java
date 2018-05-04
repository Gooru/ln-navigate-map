package org.gooru.navigatemap.processor.next.pathfinder;

import java.util.UUID;

import org.gooru.navigatemap.infra.data.ContentAddress;
import org.skife.jdbi.v2.DBI;

/**
 * @author ashish on 4/5/18.
 */
class StraightPathFinderService {
    private final DBI dbi;
    private ContentFinderDao finderDao;

    StraightPathFinderService(DBI dbi) {
        this.dbi = dbi;
        finderDao = dbi.onDemand(ContentFinderDao.class);
    }

    public ContentAddress findNextContentFromCUL(ContentAddress address, UUID classId) {
        ContentFinderVisibilityVerifierDelegate visibilityVerifier =
            ContentFinderVisibilityVerifierDelegate.build(classId, dbi);

        finderDao = dbi.onDemand(ContentFinderDao.class);
        return new ContentFinderNoSuggestionsDelegate(finderDao, visibilityVerifier)
            .findNextContentFromCULWithoutAlternatePaths(address);
    }

}
