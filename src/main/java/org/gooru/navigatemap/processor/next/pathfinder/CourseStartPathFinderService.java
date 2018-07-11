package org.gooru.navigatemap.processor.next.pathfinder;

import org.skife.jdbi.v2.DBI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author ashish on 11/7/18.
 */
class CourseStartPathFinderService implements PathFinder {

    private final DBI dbi;
    private PathFinderContext context;
    private static final Logger LOGGER = LoggerFactory.getLogger(CourseStartPathFinderService.class);

    CourseStartPathFinderService(DBI dbi) {
        this.dbi = dbi;
    }

    @Override
    public PathFinderResult findPath(PathFinderContext context) {
        this.context = context;

        if (route0ExistsAndAcceptedByUser()) {
            LOGGER.debug("Route0 exists for specified course/class/user and is accepted by user");
            // TODO: Provide implementation
            return null;
        } else {
            LOGGER.debug("Route0 does not exist. Will start course normally");
            return startCourseNormally();
        }
    }

    private PathFinderResult startCourseNormally() {
        return new PathFinderResult(
            ContentFinderFactory.buildTeacherPathAwareMainPathContentFinder(dbi).findContent(context));
    }

    private boolean route0ExistsAndAcceptedByUser() {
        // TODO: Provide implementation
        return false;
    }
}
