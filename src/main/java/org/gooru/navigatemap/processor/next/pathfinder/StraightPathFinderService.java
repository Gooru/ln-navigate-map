package org.gooru.navigatemap.processor.next.pathfinder;

import org.gooru.navigatemap.infra.data.ContentAddress;
import org.skife.jdbi.v2.DBI;

/**
 * This is encapsulation for straight path navigation. If user provides just the course (though we don't allow it
 * from our validators of {@link org.gooru.navigatemap.infra.data.RequestContext}, then we do depth first traversal
 * of unit, lesson and collection in that order till we find content that is visible and can be shown to user. That
 * content is returned. In case we do not find such content we return an empty {@link ContentAddress} based on which
 * caller can decide what to do (e.g. tell user that course is done).
 *
 * @author ashish on 4/5/18.
 */
class StraightPathFinderService implements PathFinder {
    private final DBI dbi;
    private ContentFinderDao finderDao;

    StraightPathFinderService(DBI dbi) {
        this.dbi = dbi;
        finderDao = dbi.onDemand(ContentFinderDao.class);
    }

    @Override
    public PathFinderResult findPath(PathFinderContext context) {
        return new PathFinderResult(ContentFinderFactory
            .buildAlternatePathUnawareMainPathContentFinder(dbi, ContentFinderCriteria.CRITERIA_VISIBLE)
            .findContent(context));
    }

}
