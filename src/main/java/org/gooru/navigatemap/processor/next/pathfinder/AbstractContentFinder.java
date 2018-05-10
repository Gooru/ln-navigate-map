package org.gooru.navigatemap.processor.next.pathfinder;

import java.util.UUID;

import org.skife.jdbi.v2.DBI;

/**
 * @author ashish on 7/5/18.
 */
abstract class AbstractContentFinder implements ContentFinder {

    private final DBI dbi;

    AbstractContentFinder(DBI dbi) {
        this.dbi = dbi;
    }

    protected ContentFinderDao getContentFinderDao() {
        return dbi.onDemand(ContentFinderDao.class);
    }

    protected AlternatePathDao getAlternatePathDao() {
        return dbi.onDemand(AlternatePathDao.class);
    }

    protected ContentVerifier getVisibilityVerifier(UUID classId) {
        return ContentVerifierBuilder.buildContentVisibilityVerifier(classId, dbi);
    }

    protected UserCompetencyCompletionDao getUserCompetencyCompletionDao() {
        return dbi.onDemand(UserCompetencyCompletionDao.class);
    }

    protected ContentVerifier getNonSkippabilityVerifier(String user) {
        return ContentVerifierBuilder.buildContentNonSkippabilityVerifier(dbi, user);
    }

    protected DBI getDbi() {
        return dbi;
    }
}
