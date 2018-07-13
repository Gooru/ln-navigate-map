package org.gooru.navigatemap.processor.next.pathfinder;

import java.util.UUID;

import org.skife.jdbi.v2.DBI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class is responsible for trigger of course start in case suggestions are enabled.
 *
 * @author ashish on 11/7/18.
 */
class CourseStartPathFinderService implements PathFinder {

    private final DBI dbi;
    private PathFinderContext context;
    private static final Logger LOGGER = LoggerFactory.getLogger(CourseStartPathFinderService.class);
    private Route0ContentFinderDao route0ContentFinderDao;

    CourseStartPathFinderService(DBI dbi) {
        this.dbi = dbi;
    }

    @Override
    public PathFinderResult findPath(PathFinderContext context) {
        this.context = context;

        if (route0ExistsAndAcceptedByUser()) {
            LOGGER.debug("Route0 exist. Will start course from route0");
            return PathFinderFactory.buildRoute0PathFinderService().findPath(context);
        } else {
            LOGGER.debug("Route0 does not exist. Will start course normally");
            return startCourseNormally();
        }
    }

    private PathFinderResult startCourseNormally() {
        return new PathFinderResult(ContentFinderFactory
            .buildTeacherPathAwareMainPathContentFinder(dbi, ContentFinderCriteria.CRITERIA_NON_SKIPPABLE)
            .findContent(context));
    }

    private boolean route0ExistsAndAcceptedByUser() {
        if (context.getClassId() == null) {
            return getRoute0ContentFinderDao().route0ExistsAndAcceptedByUserForIL(UUID.fromString(context.getUserId()),
                UUID.fromString(context.getContentAddress().getCourse()));
        } else {
            return getRoute0ContentFinderDao()
                .route0ExistsAndAcceptedByUserInClass(UUID.fromString(context.getUserId()),
                    UUID.fromString(context.getContentAddress().getCourse()), context.getClassId());
        }
    }

    private Route0ContentFinderDao getRoute0ContentFinderDao() {
        if (route0ContentFinderDao == null) {
            route0ContentFinderDao = dbi.onDemand(Route0ContentFinderDao.class);
        }
        return route0ContentFinderDao;
    }
}
