package org.gooru.navigatemap.processor.next.pathfinder;

import java.util.List;
import org.gooru.navigatemap.infra.data.ContentAddress;
import org.skife.jdbi.v2.DBI;

/**
 * @author ashish.
 */

class AlternatePathUnawareCULViewContentFinder {

  private final DBI dbi;
  private final ContentFinderCriteria criteria;
  private final PathFinderContext context;
  private ContentFinderDao finderDao;

  AlternatePathUnawareCULViewContentFinder(DBI dbi, ContentFinderCriteria criteria,
      PathFinderContext context) {
    this.dbi = dbi;
    this.criteria = criteria;
    this.context = context;
  }


  ContentAddress findNext(ContentAddress address) {
    List<String> units;
    if (address.getUnit() != null) {
      units = getContentFinderDao().findNextUnitsInCourse(address.getCourse(), address.getUnit());
    } else {
      units = getContentFinderDao().findUnitsInCourse(address.getCourse());
    }

    return findNextValidContentInUnits(address, units);
  }

  private ContentAddress findNextValidContentInUnits(ContentAddress address, List<String> units) {
    List<String> lessons;
    for (String unit : units) {
      if (unit.equalsIgnoreCase(address.getUnit())) {
        lessons = getContentFinderDao()
            .findNextLessonsInCU(address.getCourse(), unit, address.getLesson());
      } else {
        lessons = getContentFinderDao().findLessonsInCU(address.getCourse(), unit);
      }
      ContentAddress result = findNextValidContentInLessons(address, unit, lessons);
      if (result != null) {
        return result;
      }
    }
    return null;
  }

  private ContentAddress findNextValidContentInLessons(ContentAddress address, String unit,
      List<String> lessons) {
    switch (criteria) {
      case CRITERIA_VISIBLE:
        return findNextVisibleContentInLessons(address, unit, lessons);
      case CRITERIA_NONE:
        return findNextContentInLessons(address, unit, lessons);
      case CRITERIA_NON_SKIPPABLE:
        return findNextNonSkippableContentInLesson(address, unit, lessons);
      default:
        throw new IllegalStateException("Invalid criteria for finding content");
    }
  }

  private ContentAddress findNextNonSkippableContentInLesson(ContentAddress address, String unit,
      List<String> lessons) {
    List<ContentAddress> contentAddresses;
    ContentAddress result;
    ContentVerifier nonSkippabilityVerifier = getNonSkippabilityVerifier();

    for (String lesson : lessons) {
      if (lesson.equalsIgnoreCase(address.getLesson()) && unit.equalsIgnoreCase(address.getUnit())
          && address.getCollection() != null) {
        contentAddresses =
            finderDao.findNextCollectionsInCUL(address.getCourse(), unit, lesson,
                address.getCollection());
        result = nonSkippabilityVerifier.findFirstVerifiedContent(contentAddresses);
      } else {
        contentAddresses = finderDao.findCollectionsInCUL(address.getCourse(), unit, lesson);
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
            finderDao.findNextCollectionsInCUL(address.getCourse(), unit, lesson,
                address.getCollection());
        if (contentAddresses != null && !contentAddresses.isEmpty()) {
          result = contentAddresses.get(0);
        }
      } else {
        contentAddresses = finderDao.findCollectionsInCUL(address.getCourse(), unit, lesson);
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
            finderDao.findNextCollectionsInCUL(address.getCourse(), unit, lesson,
                address.getCollection());
        result = visibilityVerifier.findFirstVerifiedContent(contentAddresses);
      } else {
        contentAddresses = finderDao.findCollectionsInCUL(address.getCourse(), unit, lesson);
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

  protected ContentVerifier getVisibilityVerifier() {
    return ContentVerifierBuilder.buildContentVisibilityVerifier(context.getClassId(), dbi);
  }

  protected ContentVerifier getNonSkippabilityVerifier() {
    return ContentVerifierBuilder
        .buildContentNonSkippabilityVerifier(dbi, context.getUserId(),
            context.getContentAddress().getCourse(), context.getClassId());
  }

}
