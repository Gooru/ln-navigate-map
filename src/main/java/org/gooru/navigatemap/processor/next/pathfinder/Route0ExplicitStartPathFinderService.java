package org.gooru.navigatemap.processor.next.pathfinder;

import org.gooru.navigatemap.app.constants.HttpConstants;
import org.gooru.navigatemap.app.exceptions.HttpResponseWrapperException;
import org.skife.jdbi.v2.DBI;

/**
 * Handle explicit start of items on Route0.
 * <p>
 * In case pathId is provided, it is used to find specified item. In case path id is not provided,
 * it is assumed that UL are provided and user is asking to start specified lesson. In this case,
 * the first item on specified lesson is returned.
 *
 * @author ashish on 12/7/18.
 */
class Route0ExplicitStartPathFinderService implements PathFinder {

  private final DBI dbi;
  private PathFinderContext context;
  private Route0ContentFinderContext route0ContentFinderContext;
  private Route0ContentFinderDao route0ContentFinderDao;

  Route0ExplicitStartPathFinderService(DBI dbi) {
    this.dbi = dbi;
  }

  @Override
  public PathFinderResult findPath(PathFinderContext context) {
    this.context = context;
    UserRoute0ContentDetailModel userRoute0ContentDetailModel;
    initialize();
    if (context.getContentAddress().getPathId() == null
        || context.getContentAddress().getPathId() == 0) {
      userRoute0ContentDetailModel = startSpecifiedLesson();
    } else {
      userRoute0ContentDetailModel = startSpecifiedCollection();
    }
    if (userRoute0ContentDetailModel != null) {
      return new PathFinderResult(
          userRoute0ContentDetailModel.asContentAddress(context.getContentAddress().getCourse()));
    } else {
      throw new HttpResponseWrapperException(HttpConstants.HttpStatus.BAD_REQUEST,
          "No item available for explicit start on specified path.");
    }
  }

  private void initialize() {
    route0ContentFinderContext = Route0ContentFinderContext.buildFromPathFinderContext(context);
  }

  private UserRoute0ContentDetailModel startSpecifiedCollection() {
    if (context.getClassId() != null) {
      return getRoute0ContentFinderDao()
          .fetchSpecifiedItemFromRoute0InClass(route0ContentFinderContext);
    } else {
      return getRoute0ContentFinderDao()
          .fetchSpecifiedItemFromRoute0ForIL(route0ContentFinderContext);
    }
  }

  private UserRoute0ContentDetailModel startSpecifiedLesson() {
    if (context.getClassId() != null) {
      return getRoute0ContentFinderDao()
          .fetchFirstItemFromLessonForRoute0InClass(route0ContentFinderContext);
    } else {
      return getRoute0ContentFinderDao()
          .fetchFirstItemFromLessonForRoute0ForIL(route0ContentFinderContext);
    }
  }

  private Route0ContentFinderDao getRoute0ContentFinderDao() {
    if (route0ContentFinderDao == null) {
      route0ContentFinderDao = dbi.onDemand(Route0ContentFinderDao.class);
    }
    return route0ContentFinderDao;
  }
}
