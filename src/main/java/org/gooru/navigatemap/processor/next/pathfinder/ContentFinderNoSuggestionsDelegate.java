package org.gooru.navigatemap.processor.next.pathfinder;

import java.util.List;

import org.gooru.navigatemap.infra.data.ContentAddress;

/**
 * @author ashish on 5/4/17.
 */
class ContentFinderNoSuggestionsDelegate {
    private final ContentFinderDao finderDao;
    private final ContentFinderVisibilityVerifierDelegate visibilityVerifierDelegate;

    ContentFinderNoSuggestionsDelegate(ContentFinderDao finderDao,
        ContentFinderVisibilityVerifierDelegate visibilityVerifierDelegate) {
        this.finderDao = finderDao;
        this.visibilityVerifierDelegate = visibilityVerifierDelegate;
    }

    ContentAddress findNextContentFromCULWithoutAlternatePaths(ContentAddress address) {
        ContentAddress result = findNextValidContent(address);
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
        List<ContentAddress> contentAddresses;
        ContentAddress result;
        for (String lesson : lessons) {
            if (lesson.equalsIgnoreCase(address.getLesson()) && unit.equalsIgnoreCase(address.getUnit())
                && address.getCollection() != null) {
                contentAddresses =
                    finderDao.findNextCollectionsInCUL(address.getCourse(), unit, lesson, address.getCollection());
                result = visibilityVerifierDelegate.findContentAddressBasedOnVisibility(contentAddresses);
            } else {
                contentAddresses = finderDao.findCollectionsInCUL(address.getCourse(), unit, lesson);
                result = visibilityVerifierDelegate.findContentAddressBasedOnVisibility(contentAddresses);
            }
            if (result != null) {
                return result;
            }
        }
        return null;
    }

}
