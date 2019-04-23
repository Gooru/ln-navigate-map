package org.gooru.navigatemap.processor.next.pathfinder;

import org.gooru.navigatemap.app.constants.HttpConstants;
import org.gooru.navigatemap.app.exceptions.HttpResponseWrapperException;
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
  private Route0ContentFinderDao route0ContentFinderDao;
  private PathFinderContext context;

  Route0PathFinderService(DBI dbi) {
    this.dbi = dbi;
  }

  @Override
  public PathFinderResult findPath(PathFinderContext context) {
    this.context = context;
    validateProvidedRoute0Context();
    handleCompetencyCompletion();
    ContentAddress result = ContentFinderFactory.buildRoute0NextContentFinder(dbi,
        ContentVerifierBuilder.buildRoute0ContentNonSkippabilityVerifier(dbi, context.getUserId(),
            context.getContentAddress().getCourse(), context.getClassId()))
        .findContent(context);
    if (result == null) {
      LOGGER.debug("No more content available on Route0. Continue on main path.");
      return new PathFinderResult(ContentFinderFactory
          .buildTeacherPathAwareMainPathContentFinder(dbi,
              ContentFinderCriteria.CRITERIA_NON_SKIPPABLE)
          .findContent(createInitialContextToContinueOnMainPath()));
    }
    return new PathFinderResult(result);
  }

  private void handleCompetencyCompletion() {
    CompetencyCompletionHandler competencyCompletionHandler = new CompetencyCompletionHandler(dbi,
        context);
    competencyCompletionHandler.handleCompetencyCompletion();
  }

  private PathFinderContext createInitialContextToContinueOnMainPath() {
    ContentAddress contentAddress = new ContentAddress();
    contentAddress.setCourse(context.getContentAddress().getCourse());
    return new PathFinderContext(contentAddress, context.getClassId(), context.getUserId(), null,
        context.getPreferredLanguage(), context.isMilestoneViewApplicable());
  }

  private void validateProvidedRoute0Context() {
    if (startingCourse()) {
      return;
    }
    Route0ContentFinderContext route0ContentFinderContext =
        Route0ContentFinderContext.buildFromPathFinderContext(context);
    if (route0ContentFinderContext.getClassId() != null) {
      if (!getRoute0ContentFinderDao().validateRoute0ContextInClass(route0ContentFinderContext)) {
        throw new HttpResponseWrapperException(HttpConstants.HttpStatus.BAD_REQUEST,
            "Route0 context not found");
      }
    } else {
      if (!getRoute0ContentFinderDao().validateRoute0ContextForIL(route0ContentFinderContext)) {
        throw new HttpResponseWrapperException(HttpConstants.HttpStatus.BAD_REQUEST,
            "Route0 context not found");
      }
    }
  }

  private boolean startingCourse() {
    return noPath() && context.getContentAddress().getCurrentItem() == null
        && context.getContentAddress().getLesson() == null
        && context.getContentAddress().getUnit() == null;
  }

  private boolean noPath() {
    return context.getContentAddress().getPathId() == null
        || context.getContentAddress().getPathId() == 0;
  }

  private Route0ContentFinderDao getRoute0ContentFinderDao() {
    if (route0ContentFinderDao == null) {
      route0ContentFinderDao = dbi.onDemand(Route0ContentFinderDao.class);
    }
    return route0ContentFinderDao;
  }

}
