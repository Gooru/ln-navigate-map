package org.gooru.navigatemap.processor.coursepath.repositories;

import java.util.List;
import java.util.UUID;

import org.gooru.navigatemap.processor.coursepath.repositories.dao.ContentFinderDao;
import org.gooru.navigatemap.processor.data.ContentAddress;
import org.gooru.navigatemap.processor.utilities.CollectionUtils;

/**
 * @author ashish on 7/3/17.
 */
class ContentFinderRepositoryImpl extends AbstractContentRepository implements ContentFinderRepository {

    private ContentFinderDao finderDao;

    @Override
    public ContentAddress findFirstContentInCourse(UUID course) {

        finderDao = dbi.onDemand(ContentFinderDao.class);

        ContentAddress address = finderDao.findFirstContentInCourse(course.toString());
        if (address != null && address.getCollection() != null) {
            return address;
        }
        return findFirstValidContentInCourse(course.toString());
    }

    @Override
    public ContentAddress findNextContent(ContentAddress address) {
        finderDao = dbi.onDemand(ContentFinderDao.class);

        List<ContentAddress> result = finderDao
            .findNextCollectionsInCUL(address.getCourse(), address.getUnit(), address.getLesson(),
                address.getCollection());
        if (result != null && !result.isEmpty()) {
            return result.get(0);
        }
        return findNextValidContent(address);
    }

    @Override
    public List<String> findBenchmarkAssessments(List<String> competencies) {
        ContentFinderDao dao = dbi.onDemand(ContentFinderDao.class);
        return dao.findBenchmarksForCompetencyList(CollectionUtils.convertToSqlArrayOfString(competencies));
    }

    @Override
    public List<String> findCompetenciesForPostTest(UUID postTestId) {
        ContentFinderDao dao = dbi.onDemand(ContentFinderDao.class);
        return dao.findCompetenciesForPostTest(postTestId.toString());
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

    private ContentAddress findFirstValidContentInCourse(String course) {
        List<String> lessons;
        List<ContentAddress> contentAddresses;

        List<String> units = finderDao.findUnitsInCourse(course);
        for (String unit : units) {
            lessons = finderDao.findLessonsInCU(course, unit);
            for (String lesson : lessons) {
                contentAddresses = finderDao.findCollectionsInCUL(course, unit, lesson);
                if (contentAddresses != null && !contentAddresses.isEmpty()) {
                    return contentAddresses.get(0);
                }
            }
        }
        return new ContentAddress();
    }
}
