package org.gooru.navigatemap.processor.next.pathfinder;

import java.util.List;
import org.gooru.navigatemap.infra.data.ContentAddress;
import org.skife.jdbi.v2.DBI;

/**
 * @author ashish.
 */

class AlternatePathUnawareMilestoneViewContentFinder {

  private final DBI dbi;
  private final ContentFinderCriteria criteria;
  private final PathFinderContext context;
  private ContentFinderDao finderDao;
  private ContentFinderForMilestoneDao milestoneDao;

  AlternatePathUnawareMilestoneViewContentFinder(DBI dbi, ContentFinderCriteria criteria,
      PathFinderContext context) {
    this.dbi = dbi;
    this.criteria = criteria;
    this.context = context;
  }


  ContentAddress findNext(ContentAddress address) {
    List<String> milestones;
    if (address.getMilestoneId() != null) {
      milestones = getMilestoneDao()
          .findNextMilestonesInCourse(address.getCourse(), address.getMilestoneId(),
              context.getFwCode());
    } else {
      milestones = getMilestoneDao()
          .findMilestonesInCourse(address.getCourse(), context.getFwCode());
    }

    return findNextValidContentInMilestones(address, milestones);
  }

  private ContentAddress findNextValidContentInMilestones(ContentAddress address,
      List<String> milestones) {
    List<String> lessons;
    for (String milestone : milestones) {
      if (milestone.equalsIgnoreCase(address.getMilestoneId())) {
        lessons = getMilestoneDao()
            .findNextLessonsInCM(address.getCourse(), milestone, address.getLesson());
      } else {
        lessons = getMilestoneDao().findLessonsInCM(address.getCourse(), milestone);
      }
      ContentAddress result = findNextValidContentInLessons(address, milestone, lessons);
      if (result != null) {
        return result;
      }
    }
    return null;
  }

  private ContentAddress findNextValidContentInLessons(ContentAddress address, String milestone,
      List<String> lessons) {
    switch (criteria) {
      case CRITERIA_VISIBLE:
        return findNextVisibleContentInLessons(address, milestone, lessons);
      case CRITERIA_NONE:
        return findNextContentInLessons(address, milestone, lessons);
      case CRITERIA_NON_SKIPPABLE:
        return findNextNonSkippableContentInLesson(address, milestone, lessons);
      default:
        throw new IllegalStateException("Invalid criteria for finding content");
    }
  }

  private ContentAddress findNextNonSkippableContentInLesson(ContentAddress address,
      String milestone, List<String> lessons) {
    List<ContentAddress> contentAddresses;
    ContentAddress result;
    ContentVerifier nonSkippabilityVerifier = getNonSkippabilityVerifier();

    for (String lesson : lessons) {
      if (lesson.equalsIgnoreCase(address.getLesson()) && milestone
          .equalsIgnoreCase(address.getMilestoneId())
          && address.getCollection() != null) {
        contentAddresses =
            getMilestoneDao().findNextCollectionsInCL(address.getCourse(), milestone, lesson,
                address.getCollection());
        result = nonSkippabilityVerifier.findFirstVerifiedContent(contentAddresses);
      } else {
        contentAddresses = getMilestoneDao()
            .findCollectionsInCL(address.getCourse(), milestone, lesson);
        result = nonSkippabilityVerifier.findFirstVerifiedContent(contentAddresses);
      }
      if (result != null) {
        return result;
      }
    }
    return null;
  }

  private ContentAddress findNextContentInLessons(ContentAddress address, String unit,
      List<String> lessons) {
    List<ContentAddress> contentAddresses;
    ContentAddress result = null;

    for (String lesson : lessons) {
      if (lesson.equalsIgnoreCase(address.getLesson()) && unit.equalsIgnoreCase(address.getUnit())
          && address.getCollection() != null) {
        contentAddresses =
            getContentFinderDao().findNextCollectionsInCUL(address.getCourse(), unit, lesson,
                address.getCollection());
        if (contentAddresses != null && !contentAddresses.isEmpty()) {
          result = contentAddresses.get(0);
        }
      } else {
        contentAddresses = getContentFinderDao()
            .findCollectionsInCUL(address.getCourse(), unit, lesson);
        if (contentAddresses != null && !contentAddresses.isEmpty()) {
          result = contentAddresses.get(0);
        }
      }
      if (result != null) {
        return result;
      }
    }
    return null;
  }

  private ContentAddress findNextVisibleContentInLessons(ContentAddress address, String unit,
      List<String> lessons) {
    List<ContentAddress> contentAddresses;
    ContentAddress result;
    ContentVerifier visibilityVerifier = getVisibilityVerifier();

    for (String lesson : lessons) {
      if (lesson.equalsIgnoreCase(address.getLesson()) && unit.equalsIgnoreCase(address.getUnit())
          && address.getCollection() != null) {
        contentAddresses =
            getContentFinderDao().findNextCollectionsInCUL(address.getCourse(), unit, lesson,
                address.getCollection());
        result = visibilityVerifier.findFirstVerifiedContent(contentAddresses);
      } else {
        contentAddresses = getContentFinderDao()
            .findCollectionsInCUL(address.getCourse(), unit, lesson);
        result = visibilityVerifier.findFirstVerifiedContent(contentAddresses);
      }
      if (result != null) {
        return result;
      }
    }
    return null;
  }

  private ContentFinderDao getContentFinderDao() {
    if (finderDao == null) {
      finderDao = dbi.onDemand(ContentFinderDao.class);
    }
    return finderDao;
  }

  private ContentFinderForMilestoneDao getMilestoneDao() {
    if (milestoneDao == null) {
      milestoneDao = dbi.onDemand(ContentFinderForMilestoneDao.class);
    }
    return milestoneDao;
  }

  protected ContentVerifier getVisibilityVerifier() {
    return ContentVerifierBuilder.buildContentVisibilityVerifier(context.getClassId(), dbi);
  }

  protected ContentVerifier getNonSkippabilityVerifier() {
    return ContentVerifierBuilder
        .buildContentNonSkippabilityVerifier(dbi, context.getUserId(),
            context.getContentAddress().getCourse(), context.getClassId());
  }

}
