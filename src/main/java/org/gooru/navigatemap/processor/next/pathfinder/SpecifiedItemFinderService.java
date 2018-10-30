package org.gooru.navigatemap.processor.next.pathfinder;

import java.util.List;
import org.gooru.navigatemap.app.constants.HttpConstants;
import org.gooru.navigatemap.app.exceptions.HttpResponseWrapperException;
import org.gooru.navigatemap.infra.data.AlternatePath;
import org.gooru.navigatemap.infra.data.ContentAddress;
import org.skife.jdbi.v2.DBI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This is encapsulation for straight path navigation when user is trying to play a
 * collection/assessment/external assessment. Only used when reroute is off
 *
 * @author ashish on 11/5/18.
 */
class SpecifiedItemFinderService implements PathFinder {

  private final DBI dbi;
  private ContentAddress specifiedContentAddress;
  private static final Logger LOGGER = LoggerFactory.getLogger(StraightPathFinderService.class);
  private PathFinderContext context;
  private ContentFinderDao contentFinderDao;

  SpecifiedItemFinderService(DBI dbi) {
    this.dbi = dbi;
  }

  @Override
  public PathFinderResult findPath(PathFinderContext context) {
    this.context = context;
    if (context.getContentAddress().isOnSystemPath() || context.getContentAddress().isOnRoute0()) {
      throw new HttpResponseWrapperException(HttpConstants.HttpStatus.BAD_REQUEST,
          "Route0 or system path navigation is disabled");
    }
    specifiedContentAddress = context.getContentAddress();
    ContentAddress result;
    verifyCULValues(specifiedContentAddress);
    if (specifiedContentAddress.getCollection() == null) {
      LOGGER.debug("User asking to start a lesson.");
      result = fetchFirstItemFromLesson();
    } else if (specifiedContentAddress.isOnTeacherPath()) {
      LOGGER.debug("User asking to start a system/teacher suggestion.");
      result = fetchSpecifiedAlternatePath();
    } else {
      // User wanted to play something on course path
      LOGGER.debug("User asking to start an item on main path.");
      result = fetchSpecifiedContentFromCoursePath();
    }
    return new PathFinderResult(result);

  }

  private ContentAddress fetchFirstItemFromLesson() {
    // Note that Teacher suggestions are pre to actual item. So this could be teacher suggestion on first visible
    // item, or it could be first visible item. If there is no visible item in lesson, then it is an error.
    ContentVerifier visibilityVerifier =
        ContentVerifierBuilder.buildContentVisibilityVerifier(context.getClassId(), dbi);

    ContentAddress firstVisibleItem = visibilityVerifier
        .findFirstVerifiedContent(getContentFinderDao()
            .findFirstCollectionInLesson(specifiedContentAddress.getCourse(),
                specifiedContentAddress.getUnit(),
                specifiedContentAddress.getLesson()));

    if (firstVisibleItem == null) {
      throw new HttpResponseWrapperException(HttpConstants.HttpStatus.BAD_REQUEST,
          "No visible content in lesson");
    }

    ContentAddress teacherSuggestedItemForContext =
        findFirstTeacherSuggestedItemForSpecifiedContext(firstVisibleItem);
    if (teacherSuggestedItemForContext != null) {
      return teacherSuggestedItemForContext;
    } else {
      return firstVisibleItem;
    }
  }

  private ContentAddress findFirstTeacherSuggestedItemForSpecifiedContext(
      ContentAddress firstVisibleItem) {
    if (context.getClassId() == null) {
      return null; // No teacher path for IL
    }
    AlternatePathDao alternatePathDao = dbi.onDemand(AlternatePathDao.class);
    List<AlternatePath> teacherPathsForContext = alternatePathDao
        .findTeacherPathsForSpecifiedContext(firstVisibleItem, context.getUserId(),
            context.getClassId().toString());
    if (teacherPathsForContext == null || teacherPathsForContext.isEmpty()) {
      return null;
    } else {
      return teacherPathsForContext.get(0).toContentAddress();
    }
  }

  private ContentAddress fetchSpecifiedAlternatePath() {
    AlternatePathDao alternatePathDao = dbi.onDemand(AlternatePathDao.class);
    AlternatePath specifiedPath;
    if (context.getClassId() == null) {
      specifiedPath = alternatePathDao
          .findAlternatePathByPathIdAndUserForIL(specifiedContentAddress.getPathId(),
              context.getUserId());
    } else {
      specifiedPath = alternatePathDao
          .findAlternatePathByPathIdAndUserInClass(specifiedContentAddress.getPathId(),
              context.getUserId(),
              context.getClassId().toString());
    }

    if (specifiedPath == null) {
      throw new HttpResponseWrapperException(HttpConstants.HttpStatus.BAD_REQUEST,
          "Invalid content");
    }
    return specifiedPath.toContentAddress();
  }

  private ContentAddress fetchSpecifiedContentFromCoursePath() {
    ContentAddress result = getContentFinderDao()
        .findCULC(specifiedContentAddress.getCourse(), specifiedContentAddress.getUnit(),
            specifiedContentAddress.getLesson(), specifiedContentAddress.getCollection());
    validateVisibility(result);
    return result;
  }

  private void validateVisibility(ContentAddress result) {
    ContentVerifier visibilityVerifier =
        ContentVerifierBuilder.buildContentVisibilityVerifier(context.getClassId(), dbi);
    if (result == null || !visibilityVerifier.isContentVerified(result)) {
      throw new HttpResponseWrapperException(HttpConstants.HttpStatus.BAD_REQUEST,
          "Invalid content");
    }
  }

  private void verifyCULValues(ContentAddress contentAddress) {
    if (contentAddress.getCourse() == null || contentAddress.getUnit() == null
        || contentAddress.getLesson() == null) {
      throw new HttpResponseWrapperException(HttpConstants.HttpStatus.BAD_REQUEST,
          "Invalid CUL info in context");
    }
  }

  private ContentFinderDao getContentFinderDao() {
    if (contentFinderDao == null) {
      contentFinderDao = dbi.onDemand(ContentFinderDao.class);
    }
    return contentFinderDao;
  }

}
