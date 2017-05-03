package org.gooru.navigatemap.processor.coursepath.repositories;

import java.util.*;

import org.gooru.navigatemap.processor.coursepath.repositories.dao.ContentFinderDao;
import org.gooru.navigatemap.processor.data.AlternatePath;
import org.gooru.navigatemap.processor.data.ContentAddress;
import org.gooru.navigatemap.processor.data.SuggestionCard4Collection;
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

        return new ContentFinderNoSuggestionsDelegate(finderDao).findNextContentFromCULWithoutAlternatePaths(address);
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
    public Set<String> findCompetenciesForLesson(ContentAddress contentAddress) {
        ContentFinderDao dao = dbi.onDemand(ContentFinderDao.class);
        String lessonTaxonomy = dao.findCompetenciesForLesson(contentAddress.getCourse(), contentAddress.getUnit(),
            contentAddress.getLesson());
        return parseLessonTaxonomy(contentAddress, lessonTaxonomy);
    }

    @Override
    public List<SuggestionCard4Collection> createSuggestionsCardForCollections(Set<String> collections) {
        ContentFinderDao dao = dbi.onDemand(ContentFinderDao.class);
        List<String> collectionsList = new ArrayList<>(collections);

        return dao.createSuggestionsCardForCollections(CollectionUtils.convertToSqlArrayOfUUID(collectionsList));
    }

    @Override
    public List<String> findBackfillsForPreTestAndScoreRange(UUID preTestId, String scoreRangeName) {
        ContentFinderDao dao = dbi.onDemand(ContentFinderDao.class);
        return dao.findBackfillsForPreTestAndScoreRange(preTestId, scoreRangeName);
    }

    @Override
    public AlternatePath findAlternatePathForUser(ContentAddress currentAddress, String user) {
        ContentFinderDao dao = dbi.onDemand(ContentFinderDao.class);
        return dao.findAlternatePathByPathIdAndUser(currentAddress.getPathId(), user);
    }

    @Override
    public AlternatePath findAlternatePathForUserInClass(ContentAddress currentAddress, String user, String classId) {
        ContentFinderDao dao = dbi.onDemand(ContentFinderDao.class);
        return dao.findAlternatePathByPathIdAndUserInClass(currentAddress.getPathId(), user, classId);
    }

    @Override
    public List<AlternatePath> findChildPathsOfTypeBA(AlternatePath currentPath) {
        ContentFinderDao dao = dbi.onDemand(ContentFinderDao.class);
        return dao.findBASubPathsForGivenPath(currentPath.getId());
    }

    @Override
    public List<AlternatePath> findChildPathsOfTypeBackfill(AlternatePath currentPath) {
        ContentFinderDao dao = dbi.onDemand(ContentFinderDao.class);
        return dao.findBackfillsSubPathsForGivenPath(currentPath.getId());
    }

    @Override
    public List<AlternatePath> findChildPathsOfTypePostTest(ContentAddress currentAddress, String user,
        String classId) {
        ContentFinderDao dao = dbi.onDemand(ContentFinderDao.class);
        if (classId != null) {
            return dao
                .findPostTestAlternatePathsForCULAndUserInClass(currentAddress.getCourse(), currentAddress.getUnit(),
                    currentAddress.getLesson(), user, classId);
        } else {
            return dao.findPostTestAlternatePathsForCULAndUser(currentAddress.getCourse(), currentAddress.getUnit(),
                currentAddress.getLesson(), user);
        }
    }

    @Override
    public List<AlternatePath> findChildPathsOfTypePreTest(ContentAddress currentAddress, String user, String classId) {
        ContentFinderDao dao = dbi.onDemand(ContentFinderDao.class);
        if (classId != null) {
            return dao
                .findPreTestAlternatePathsForCULAndUserInClass(currentAddress.getCourse(), currentAddress.getUnit(),
                    currentAddress.getLesson(), user, classId);
        } else {
            return dao.findPreTestAlternatePathsForCULAndUser(currentAddress.getCourse(), currentAddress.getUnit(),
                currentAddress.getLesson(), user);
        }
    }

    @Override
    public boolean validateContentAddress(ContentAddress contentAddress) {
        if (contentAddress.getCourse() == null || contentAddress.getUnit() == null
            || contentAddress.getLesson() == null) {
            return false;
        }
        ContentFinderDao dao = dbi.onDemand(ContentFinderDao.class);
        long validRecordCount = 0;
        if (contentAddress.getCollection() == null) {
            validRecordCount =
                dao.validateCUL(contentAddress.getCourse(), contentAddress.getUnit(), contentAddress.getLesson());
        } else if (!contentAddress.isOnAlternatePath()) {
            validRecordCount =
                dao.validateCULC(contentAddress.getCourse(), contentAddress.getUnit(), contentAddress.getLesson(),
                    contentAddress.getCollection());
        } else if (contentAddress.isOnAlternatePath()) {
            validRecordCount =
                dao.validateCUL(contentAddress.getCourse(), contentAddress.getUnit(), contentAddress.getLesson());
            if (validRecordCount > 0) {
                validRecordCount = dao.validatePath(contentAddress.getPathId(), contentAddress.getCollection(),
                    contentAddress.getCollectionType().getName());
            }
        }
        return (validRecordCount > 0);
    }

    @Override
    public String findCourseVersion(UUID course) {
        finderDao = dbi.onDemand(ContentFinderDao.class);

        return finderDao.findCourseVersion(course.toString());
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
        return Collections.emptySet();
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
