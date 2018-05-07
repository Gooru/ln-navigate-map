package org.gooru.navigatemap.processor.next.pathfinder;

import java.util.List;

import org.gooru.navigatemap.infra.data.ContentAddress;
import org.skife.jdbi.v2.DBI;

/**
 * This class just looks at Course path to find the content which is to be served next. This class takes a criteria
 * based on which it decides whether the content is eligible for play. The criteria verification is implemented by
 * {@link ContentVerifier}
 *
 * @author ashish on 5/4/17.
 */
class AlternatePathUnawareMainPathContentFinder extends AbstractContentFinder {
    private final ContentFinderCriteria criteria;
    private PathFinderContext context;

    // This can be obtained from base class but we want to cache it instead of creating new every time as this is the
    // only dao we are going to use. If there is another dao which we need to use here, we should ask fresh every
    // time from base class
    private final ContentFinderDao finderDao;

    AlternatePathUnawareMainPathContentFinder(DBI dbi, ContentFinderCriteria criteria) {
        super(dbi);
        this.criteria = criteria;
        finderDao = getContentFinderDao();
    }

    @Override
    public ContentAddress findContent(PathFinderContext context) {
        this.context = context;
        ContentAddress result = findNextValidContent(context.getContentAddress());
        if (result != null) {
            return result;
        }
        return new ContentAddress();

    }

    private ContentAddress findNextValidContent(ContentAddress address) {
        List<String> units;
        if (address.getUnit() != null) {
            units = finderDao.findNextUnitsInCourse(address.getCourse(), address.getUnit());
        } else {
            units = finderDao.findUnitsInCourse(address.getCourse());
        }

        return findNextValidContentInUnits(address, units);
    }

    private ContentAddress findNextValidContentInUnits(ContentAddress address, List<String> units) {
        List<String> lessons;
        for (String unit : units) {
            if (unit.equalsIgnoreCase(address.getUnit())) {
                lessons = finderDao.findNextLessonsInCU(address.getCourse(), unit, address.getLesson());
            } else {
                lessons = finderDao.findLessonsInCU(address.getCourse(), unit);
            }
            ContentAddress result = findNextValidContentInLessons(address, unit, lessons);
            if (result != null) {
                return result;
            }
        }
        return null;
    }

    private ContentAddress findNextValidContentInLessons(ContentAddress address, String unit, List<String> lessons) {
        switch (criteria) {
        case CRITERIA_VISIBLE:
            return findNextVisibleContentInLessons(address, unit, lessons);
        case CRITERIA_ALL:
            return findNextContentInLessons(address, unit, lessons);
        case CRITERIA_VISIBLE_NON_SKIPPABLE:
            throw new IllegalStateException("Not implemented");
        default:
            throw new IllegalStateException("Invalid criteria for finding content");
        }
    }

    private ContentAddress findNextContentInLessons(ContentAddress address, String unit, List<String> lessons) {
        List<ContentAddress> contentAddresses;
        ContentAddress result = null;

        for (String lesson : lessons) {
            if (lesson.equalsIgnoreCase(address.getLesson()) && unit.equalsIgnoreCase(address.getUnit())
                && address.getCollection() != null) {
                contentAddresses =
                    finderDao.findNextCollectionsInCUL(address.getCourse(), unit, lesson, address.getCollection());
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

    private ContentAddress findNextVisibleContentInLessons(ContentAddress address, String unit, List<String> lessons) {
        List<ContentAddress> contentAddresses;
        ContentAddress result;
        ContentVerifier visibilityVerifier = getVisibilityVerifier(context.getClassId());

        for (String lesson : lessons) {
            if (lesson.equalsIgnoreCase(address.getLesson()) && unit.equalsIgnoreCase(address.getUnit())
                && address.getCollection() != null) {
                contentAddresses =
                    finderDao.findNextCollectionsInCUL(address.getCourse(), unit, lesson, address.getCollection());
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

    private ContentAddress findNextVisibleAndNonSkippableContentInLessons(ContentAddress address, String unit,
        List<String> lessons) {
        List<ContentAddress> contentAddresses;
        ContentAddress result;
        ContentVerifier visibilityVerifier = getVisibilityVerifier(context.getClassId());

        for (String lesson : lessons) {
            if (lesson.equalsIgnoreCase(address.getLesson()) && unit.equalsIgnoreCase(address.getUnit())
                && address.getCollection() != null) {
                contentAddresses =
                    finderDao.findNextCollectionsInCUL(address.getCourse(), unit, lesson, address.getCollection());
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
}
