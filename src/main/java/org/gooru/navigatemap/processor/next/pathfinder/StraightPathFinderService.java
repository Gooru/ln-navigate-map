package org.gooru.navigatemap.processor.next.pathfinder;

import org.gooru.navigatemap.app.constants.HttpConstants;
import org.gooru.navigatemap.app.exceptions.HttpResponseWrapperException;
import org.gooru.navigatemap.infra.data.ContentAddress;
import org.skife.jdbi.v2.DBI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This is encapsulation for straight path navigation. If user provides just the course (though we
 * don't allow it from our validators of {@link org.gooru.navigatemap.infra.data.RequestContext},
 * then we do depth first traversal of unit, lesson and collection in that order till we find
 * content that is visible and can be shown to user. That content is returned. In case we do not
 * find such content we return an empty {@link ContentAddress} based on which caller can decide what
 * to do (e.g. tell user that course is done).
 * <p>
 * Only used when reroute is off.
 *
 * @author ashish on 4/5/18.
 */
class StraightPathFinderService implements PathFinder {

  private final DBI dbi;
  private static final Logger LOGGER = LoggerFactory.getLogger(StraightPathFinderService.class);
  private PathFinderContext context;

  StraightPathFinderService(DBI dbi) {
    this.dbi = dbi;
  }

  @Override
  public PathFinderResult findPath(PathFinderContext context) {
    this.context = context;
    if (context.getContentAddress().isOnSystemPath() || context.getContentAddress().isOnRoute0()) {
      throw new HttpResponseWrapperException(HttpConstants.HttpStatus.BAD_REQUEST,
          "Route0 or system path navigation is disabled");
    }
    if (context.getContentAddress().isOnTeacherPath()) {
      return loadNextItemFromTeacherpath();
    }
    return new PathFinderResult(
        ContentFinderFactory
            .buildTeacherPathAwareMainPathContentFinder(dbi, ContentFinderCriteria.CRITERIA_VISIBLE)
            .findContent(context));
  }

  private PathFinderResult loadNextItemFromTeacherpath() {
    ContentAddress teacherPathContentAddress =
        ContentFinderFactory.buildTeacherPathContentFinder(dbi).findContent(context);
    if (teacherPathContentAddress != null) {
      return new PathFinderResult(teacherPathContentAddress);
    } else {
      return loadCurrentItemFromMainpath();
    }
  }

  private PathFinderResult loadCurrentItemFromMainpath() {
    return new PathFinderResult(
        ContentFinderFactory.buildAlternatePathUnawareSpecifiedPathContentFinder(dbi)
            .findContent(context));
  }

}
