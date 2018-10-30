package org.gooru.navigatemap.processor.next.pathfinder;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import org.gooru.navigatemap.infra.data.ContentAddress;
import org.gooru.navigatemap.infra.utilities.CollectionUtils;
import org.skife.jdbi.v2.DBI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author ashish on 8/5/18.
 */
class ContentNonSkippabilityVerifier implements ContentVerifier {

  private final String user;
  private final UUID classId;
  private final String courseId;
  private final boolean isRoute0;
  private final ContentFinderDao finderDao;
  private final UserCompetencyCompletionDao userCompetencyCompletionDao;
  private RescopedItems rescopedItems;
  private static final Logger LOGGER = LoggerFactory
      .getLogger(ContentNonSkippabilityVerifier.class);

  private ContentNonSkippabilityVerifier(DBI dbi, String user, String courseid, UUID classId,
      boolean isRoute0) {
    this.finderDao = dbi.onDemand(ContentFinderDao.class);
    this.userCompetencyCompletionDao = dbi.onDemand(UserCompetencyCompletionDao.class);
    this.user = user;
    this.courseId = courseid;
    this.classId = classId;
    this.isRoute0 = isRoute0;
  }

  static ContentNonSkippabilityVerifier build(DBI dbi, String user, String courseId, UUID classId) {
    return new ContentNonSkippabilityVerifier(dbi, user, courseId, classId, false);
  }

  static ContentVerifier buildForRoute0(DBI dbi, String user, String courseId, UUID classId) {
    return new ContentNonSkippabilityVerifier(dbi, user, courseId, classId, true);
  }

  @Override
  public boolean isContentVerified(ContentAddress contentAddress) {
    if (contentAddress.isOnMainPath()) {
      if (isContentVerifiedBasedOnRescope(contentAddress)) {
        return isContentVerifiedBasedOnCompetencyStatus(contentAddress);
      } else {
        return false;
      }
    } else {
      return isContentVerifiedBasedOnCompetencyStatus(contentAddress);
    }
  }

  @Override
  public ContentAddress findFirstVerifiedContent(List<ContentAddress> contentAddresses) {
    // NOTE: Currently this implementation does one query for one content item. If there are way too many
    // skippable items, it might be more performant to query all the content in batches and do processing
    if (contentAddresses != null && !contentAddresses.isEmpty()) {
      for (ContentAddress address : contentAddresses) {
        if (isContentVerified(address)) {
          return address;
        }
      }
    }
    return null;
  }

  @Override
  public List<ContentAddress> filterVerifiedContent(List<ContentAddress> contentAddresses) {
    // NOTE: Currently this implementation does one query for one content item. If there are way too many
    // skippable items, it might be more performant to query all the content in batches and do processing
    List<ContentAddress> result = new ArrayList<>();
    if (contentAddresses != null && !contentAddresses.isEmpty()) {
      for (ContentAddress address : contentAddresses) {
        if (isContentVerified(address)) {
          result.add(address);
        }
      }
    }
    return result;
  }

  private boolean isContentVerifiedBasedOnRescope(ContentAddress contentAddress) {
    // Route0 items will never be present in rescoped items, hence default verification status is true for them
    if (isRoute0) {
      return true;
    }
    RescopedItems toBeSkippedItems = fetchRescopedItems();
    if (toBeSkippedItems.isUnitRescoped(contentAddress.getUnit())) {
      return false;
    } else if (toBeSkippedItems.isLessonRescoped(contentAddress.getLesson())) {
      return false;
    } else {
      switch (contentAddress.getCurrentItemType()) {
        case Assessment:
          if (toBeSkippedItems.isAssessmentRescoped(contentAddress.getCurrentItem())) {
            return false;
          }
          break;
        case Collection:
          if (toBeSkippedItems.isCollectionRescoped(contentAddress.getCurrentItem())) {
            return false;
          }
          break;
        case AssessmentExternal:
          if (toBeSkippedItems.isAssessmentExternalRescoped(contentAddress.getCurrentItem())) {
            return false;
          }
          break;
        default:
          return true;
      }
      return true;
    }

  }

  private boolean isContentVerifiedBasedOnCompetencyStatus(ContentAddress contentAddress) {
    List<List<String>> listOfListOfComps = finderDao
        .findCompetenciesForCollection(contentAddress.getCourse(), contentAddress.getUnit(),
            contentAddress.getLesson(), contentAddress.getCollection());

    List<String> competencyList =
        (listOfListOfComps != null && !listOfListOfComps.isEmpty()) ? listOfListOfComps.get(0) :
            Collections.emptyList();

    if (competencyList.isEmpty()) {
      return true;
    } else {
      List<String> completedCompetenciesByUser = userCompetencyCompletionDao
          .findCompletedOrMasteredCompetenciesForUserInGivenList(user,
              CollectionUtils.convertToSqlArrayOfString(competencyList));
      competencyList.removeAll(completedCompetenciesByUser);
      return !competencyList.isEmpty();
    }
  }

  private RescopedItems fetchRescopedItems() {
    if (rescopedItems == null) {
      initializeRescopedItems();
    }
    return rescopedItems;
  }

  private void initializeRescopedItems() {
    String rescopedItemsAsString;
    if (classId != null) {
      rescopedItemsAsString = finderDao
          .fetchRescopedContentForUserInClass(UUID.fromString(user), UUID.fromString(courseId),
              classId);

    } else {
      rescopedItemsAsString = finderDao
          .fetchRescopedContentForUserInIL(UUID.fromString(user), UUID.fromString(courseId));
    }
    if (rescopedItemsAsString == null || rescopedItemsAsString.isEmpty()) {
      rescopedItems = new RescopedItems();
    } else {
      try {
        rescopedItems = new ObjectMapper().readValue(rescopedItemsAsString, RescopedItems.class);
      } catch (IOException e) {
        LOGGER.warn("Invalid rescoped JSON for user: '{}', course: '{}', class: '{}' ", user,
            courseId, classId);
        throw new IllegalStateException("Rescoped items JSON is invalid", e);
      }
    }
  }
}
