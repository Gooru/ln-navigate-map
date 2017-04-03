package org.gooru.navigatemap.processor.coursepath.repositories;

import java.util.*;

import org.gooru.navigatemap.processor.coursepath.repositories.dao.ContentFinderDao;
import org.gooru.navigatemap.processor.data.ContentAddress;
import org.gooru.navigatemap.processor.data.RequestContext;
import org.gooru.navigatemap.processor.utilities.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.vertx.core.json.DecodeException;
import io.vertx.core.json.JsonObject;

/**
 * @author ashish on 7/3/17.
 */
final class ContentFinderRepositoryImpl extends AbstractContentRepository implements ContentFinderRepository {

    private ContentFinderDao finderDao;
    private static final Logger LOGGER = LoggerFactory.getLogger(ContentFinderRepository.class);

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
    public ContentAddress findNextContentFromCUL(ContentAddress address) {
        finderDao = dbi.onDemand(ContentFinderDao.class);

        return new ContentFinderWithoutSuggestions(finderDao).findNextContentFromCULWithoutAlternatePaths(address);
    }

    @Override
    public List<String> findBenchmarkAssessments(List<String> competencies) {
        ContentFinderDao dao = dbi.onDemand(ContentFinderDao.class);
        return dao.findBenchmarksForCompetencyList(CollectionUtils.convertToSqlArrayOfString(competencies));
    }

    @Override
    public Set<String> findPreTestsAssessments(Set<String> competencies) {
        List<String> competencyList = new ArrayList<>(competencies);
        ContentFinderDao dao = dbi.onDemand(ContentFinderDao.class);
        List<String> result =
            dao.findPreTestsForCompetencyList(CollectionUtils.convertToSqlArrayOfString(competencyList));
        return new HashSet<>(result);
    }

    @Override
    public Set<String> findPostTestsAssessments(Set<String> competencies) {
        ContentFinderDao dao = dbi.onDemand(ContentFinderDao.class);
        List<String> competencyList = new ArrayList<>(competencies);
        List<String> result =
            dao.findPostTestsForCompetencyList(CollectionUtils.convertToSqlArrayOfString(competencyList));
        return new HashSet<>(result);
    }

    @Override
    public List<String> findCompetenciesForPostTest(UUID postTestId) {
        ContentFinderDao dao = dbi.onDemand(ContentFinderDao.class);
        return dao.findCompetenciesForPostTest(postTestId.toString());
    }

    @Override
    public ContentAddress findNextContent(ContentAddress contentAddress, RequestContext requestContext) {
        throw new AssertionError("Not implemented");
    }

    @Override
    public Set<String> findCompetenciesForLesson(ContentAddress contentAddress) {
        ContentFinderDao dao = dbi.onDemand(ContentFinderDao.class);
        String lessonTaxonomy = dao.findCompetenciesForLesson(contentAddress.getCourse(), contentAddress.getUnit(),
            contentAddress.getLesson());
        return parseLessonTaxonomy(contentAddress, lessonTaxonomy);
    }

    private static Set<String> parseLessonTaxonomy(ContentAddress contentAddress, String lessonTaxonomy) {
        if (lessonTaxonomy != null && !lessonTaxonomy.isEmpty()) {
            // Lesson taxonomy is supposed to be a JsonObject with keys as competencies' internal code
            // Note that they are not GDT aware but FW specific
            try {
                JsonObject taxonomy = new JsonObject(lessonTaxonomy);
                return taxonomy.fieldNames();
            } catch (DecodeException ex) {
                LOGGER.warn("Invalid taxonomy string for address: Course='{}', Unit='{}', Lesson='{}'",
                    contentAddress.getCourse(), contentAddress.getUnit(), contentAddress.getLesson());
            }
        }
        return new HashSet<>();
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

    private static class ContentFinderWithoutSuggestions {
        private final ContentFinderDao finderDao;

        ContentFinderWithoutSuggestions(ContentFinderDao finderDao) {
            this.finderDao = finderDao;
        }

        private ContentAddress findNextContentFromCULWithoutAlternatePaths(ContentAddress address) {
            List<ContentAddress> result = finderDao
                .findNextCollectionsInCUL(address.getCourse(), address.getUnit(), address.getLesson(),
                    address.getCollection());
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
                        contentAddresses = finderDao
                            .findNextCollectionsInCUL(address.getCourse(), unit, lesson, address.getCollection());
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
}
