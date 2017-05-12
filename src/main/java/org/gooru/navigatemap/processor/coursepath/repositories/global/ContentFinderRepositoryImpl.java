package org.gooru.navigatemap.processor.coursepath.repositories.global;

import java.util.*;

import org.gooru.navigatemap.processor.coursepath.repositories.AbstractContentRepository;
import org.gooru.navigatemap.processor.coursepath.repositories.ContentFinderNoSuggestionsDelegate;
import org.gooru.navigatemap.processor.coursepath.repositories.dao.AlternatePathGlobalStrategyDao;
import org.gooru.navigatemap.processor.coursepath.repositories.dao.ContentFinderDao;
import org.gooru.navigatemap.processor.coursepath.repositories.helpers.TaxonomyParserHelper;
import org.gooru.navigatemap.processor.data.AlternatePath;
import org.gooru.navigatemap.processor.data.ContentAddress;
import org.gooru.navigatemap.processor.utilities.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
        AlternatePathGlobalStrategyDao dao = dbi.onDemand(AlternatePathGlobalStrategyDao.class);
        return dao.findBenchmarksForCompetencyList(CollectionUtils.convertToSqlArrayOfString(competencies));
    }

    @Override
    public Set<String> findPreTestsAssessments(Set<String> competencies) {
        List<String> competencyList = new ArrayList<>(competencies);
        AlternatePathGlobalStrategyDao dao = dbi.onDemand(AlternatePathGlobalStrategyDao.class);
        List<String> result =
            dao.findPreTestsForCompetencyList(CollectionUtils.convertToSqlArrayOfString(competencyList));
        return new HashSet<>(result);
    }

    @Override
    public Set<String> findPostTestsAssessments(Set<String> competencies) {
        AlternatePathGlobalStrategyDao dao = dbi.onDemand(AlternatePathGlobalStrategyDao.class);
        List<String> competencyList = new ArrayList<>(competencies);
        List<String> result =
            dao.findPostTestsForCompetencyList(CollectionUtils.convertToSqlArrayOfString(competencyList));
        return new HashSet<>(result);
    }

    @Override
    public List<String> findCompetenciesForPostTest(UUID postTestId) {
        AlternatePathGlobalStrategyDao dao = dbi.onDemand(AlternatePathGlobalStrategyDao.class);
        return dao.findCompetenciesForPostTest(postTestId.toString());
    }

    @Override
    public Set<String> findCompetenciesForLesson(ContentAddress contentAddress) {
        ContentFinderDao dao = dbi.onDemand(ContentFinderDao.class);
        String lessonTaxonomy = dao.findCompetenciesForLesson(contentAddress.getCourse(), contentAddress.getUnit(),
            contentAddress.getLesson());
        return TaxonomyParserHelper.parseLessonTaxonomy(contentAddress, lessonTaxonomy);
    }

    @Override
    public List<String> findBackfillsForPreTestAndScoreRange(UUID preTestId, String scoreRangeName) {
        AlternatePathGlobalStrategyDao dao = dbi.onDemand(AlternatePathGlobalStrategyDao.class);
        return dao.findBackfillsForPreTestAndScoreRange(preTestId, scoreRangeName);
    }

    @Override
    public AlternatePath findAlternatePathForUser(ContentAddress currentAddress, String user) {
        AlternatePathGlobalStrategyDao dao = dbi.onDemand(AlternatePathGlobalStrategyDao.class);
        return dao.findAlternatePathByPathIdAndUser(currentAddress.getPathId(), user);
    }

    @Override
    public AlternatePath findAlternatePathForUserInClass(ContentAddress currentAddress, String user, String classId) {
        AlternatePathGlobalStrategyDao dao = dbi.onDemand(AlternatePathGlobalStrategyDao.class);
        return dao.findAlternatePathByPathIdAndUserInClass(currentAddress.getPathId(), user, classId);
    }

    @Override
    public List<AlternatePath> findChildPathsOfTypeBA(AlternatePath currentPath) {
        AlternatePathGlobalStrategyDao dao = dbi.onDemand(AlternatePathGlobalStrategyDao.class);
        return dao.findBASubPathsForGivenPath(currentPath.getId());
    }

    @Override
    public List<AlternatePath> findChildPathsOfTypeBackfill(AlternatePath currentPath) {
        AlternatePathGlobalStrategyDao dao = dbi.onDemand(AlternatePathGlobalStrategyDao.class);
        return dao.findBackfillsSubPathsForGivenPath(currentPath.getId());
    }

    @Override
    public List<AlternatePath> findChildPathsOfTypePostTest(ContentAddress currentAddress, String user,
        String classId) {
        AlternatePathGlobalStrategyDao dao = dbi.onDemand(AlternatePathGlobalStrategyDao.class);
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
        AlternatePathGlobalStrategyDao dao = dbi.onDemand(AlternatePathGlobalStrategyDao.class);
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
        long validRecordCount = 0;
        if (contentAddress.getCollection() == null) {
            ContentFinderDao dao = dbi.onDemand(ContentFinderDao.class);
            validRecordCount =
                dao.validateCUL(contentAddress.getCourse(), contentAddress.getUnit(), contentAddress.getLesson());
        } else if (!contentAddress.isOnAlternatePath()) {
            ContentFinderDao dao = dbi.onDemand(ContentFinderDao.class);
            validRecordCount =
                dao.validateCULC(contentAddress.getCourse(), contentAddress.getUnit(), contentAddress.getLesson(),
                    contentAddress.getCollection());
        } else if (contentAddress.isOnAlternatePath()) {
            ContentFinderDao dao = dbi.onDemand(ContentFinderDao.class);
            validRecordCount =
                dao.validateCUL(contentAddress.getCourse(), contentAddress.getUnit(), contentAddress.getLesson());
            if (validRecordCount > 0) {
                AlternatePathGlobalStrategyDao alternatePathGlobalStrategyDao =
                    dbi.onDemand(AlternatePathGlobalStrategyDao.class);
                validRecordCount = alternatePathGlobalStrategyDao
                    .validatePath(contentAddress.getPathId(), contentAddress.getCollection(),
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
