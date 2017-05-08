package org.gooru.navigatemap.processor.coursepath.repositories;

import java.util.List;

import org.gooru.navigatemap.processor.coursepath.repositories.dao.ContentFinderDao;
import org.gooru.navigatemap.processor.data.ContentAddress;

/**
 * @author ashish on 5/4/17.
 */
public class ContentFinderNoSuggestionsDelegate {
    private final ContentFinderDao finderDao;

    public ContentFinderNoSuggestionsDelegate(ContentFinderDao finderDao) {
        this.finderDao = finderDao;
    }

    public ContentAddress findNextContentFromCULWithoutAlternatePaths(ContentAddress address) {
        List<ContentAddress> result;
        if (address.getCollection() != null && !address.isOnAlternatePath()) {
            result = finderDao.findNextCollectionsInCUL(address.getCourse(), address.getUnit(), address.getLesson(),
                address.getCollection());
        } else if (address.isOnAlternatePathAtLessonEnd()) {
            // Trigger find next valid content flow
            result = null;
        } else {
            result = finderDao.findFirstCollectionInLesson(address.getCourse(), address.getUnit(), address.getLesson());
        }
        if (result != null && !result.isEmpty()) {
            return result.get(0);
        }
        return findNextValidContent(address);
    }

    private ContentAddress findNextValidContent(ContentAddress address) {
        List<String> lessons;
        List<ContentAddress> contentAddresses;
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
                } else {
                    contentAddresses = finderDao.findCollectionsInCUL(address.getCourse(), unit, lesson);
                }
                if (contentAddresses != null && !contentAddresses.isEmpty()) {
                    return contentAddresses.get(0);
                }
            }
        }
        return new ContentAddress();
    }

}
