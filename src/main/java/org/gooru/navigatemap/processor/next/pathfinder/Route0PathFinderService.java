package org.gooru.navigatemap.processor.next.pathfinder;

import org.gooru.navigatemap.infra.data.ContentAddress;
import org.skife.jdbi.v2.DBI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author ashish on 11/7/18.
 */
class Route0PathFinderService implements PathFinder {

    private final DBI dbi;
    private static final Logger LOGGER = LoggerFactory.getLogger(Route0PathFinderService.class);

    Route0PathFinderService(DBI dbi) {
        this.dbi = dbi;
    }

    @Override
    public PathFinderResult findPath(PathFinderContext context) {
        ContentAddress result = ContentFinderFactory.buildRoute0NextContentFinder(dbi).findContent(context);
        if (result == null) {
            LOGGER.debug("No more content available on Route0. Continue on main path.");
            return new PathFinderResult(ContentFinderFactory.buildTeacherPathAwareMainPathContentFinder(dbi)
                .findContent(createInitialContext(context)));
        }
        return new PathFinderResult(result);
    }

    private PathFinderContext createInitialContext(PathFinderContext context) {
        ContentAddress contentAddress = new ContentAddress();
        contentAddress.setCourse(context.getContentAddress().getCourse());
        return new PathFinderContext(contentAddress, context.getClassId(), context.getUserId(), null);
    }
}
