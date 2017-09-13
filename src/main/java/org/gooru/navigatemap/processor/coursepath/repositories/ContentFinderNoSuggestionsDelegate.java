package org.gooru.navigatemap.processor.coursepath.repositories;

import java.util.List;

import org.gooru.navigatemap.processor.coursepath.repositories.dao.ContentFinderDao;
import org.gooru.navigatemap.processor.data.ContentAddress;

/**
 * @author ashish on 5/4/17.
 */
public class ContentFinderNoSuggestionsDelegate {
    private final ContentFinderDao finderDao;
    private final ContentFinderVisibilityVerifierDelegate visibilityVerifierDelegate;

    public ContentFinderNoSuggestionsDelegate(ContentFinderDao finderDao,
        ContentFinderVisibilityVerifierDelegate visibilityVerifierDelegate) {
        this.finderDao = finderDao;
        this.visibilityVerifierDelegate = visibilityVerifierDelegate;
    }

    public ContentAddress findNextContentFromCULWithoutAlternatePaths(ContentAddress address) {
        ContentAddress result;
        if (address.getCollection() != null && !address.isOnAlternatePath()) {
            result = visibilityVerifierDelegate.findContentAddressBasedOnVisibility(finderDao
                .findNextCollectionsInCUL(address.getCourse(), address.getUnit(), address.getLesson(),
                    address.getCollection()));
        } else if (address.isOnAlternatePathAtLessonEnd()) {
            // Trigger find next valid content flow
            result = null;
        } else {
            result = visibilityVerifierDelegate.findContentAddressBasedOnVisibility(
                finderDao.findFirstCollectionInLesson(address.getCourse(), address.getUnit(), address.getLesson()));
        }
        if (result != null) {
            return result;
        }
        return findNextValidContent(address);
    }

    private ContentAddress findNextValidContent(ContentAddress address) {
        List<String> lessons;
        List<ContentAddress> contentAddresses;
        ContentAddress result;
        List<String> units = finderDao.findNextUnitsInCourse(address.getCourse(), address.getUnit());
        for (String unit : units) {
            if (unit.equalsIgnoreCase(address.getUnit())) {
                lessons = finderDao.findNextLessonsInCU(address.getCourse(), unit, address.getLesson());
            } else {
                lessons = finderDao.findLessonsInCU(address.getCourse(), unit);
            }
            for (String lesson : lessons) {
                if (lesson.equalsIgnoreCase(address.getLesson()) && unit.equalsIgnoreCase(address.getUnit())) {
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
        }
        return new ContentAddress();
    }

}
