package org.gooru.navigatemap.processor.coursepath.repositories.nu;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.gooru.navigatemap.processor.coursepath.repositories.AbstractContentRepository;
import org.gooru.navigatemap.processor.coursepath.repositories.ContentFinderNoSuggestionsDelegate;
import org.gooru.navigatemap.processor.coursepath.repositories.dao.AlternatePathNUStrategyDao;
import org.gooru.navigatemap.processor.coursepath.repositories.dao.ContentFinderDao;
import org.gooru.navigatemap.processor.data.CollectionType;
import org.gooru.navigatemap.processor.data.ContentAddress;
import org.gooru.navigatemap.processor.data.FinderContext;
import org.gooru.navigatemap.processor.utilities.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.vertx.core.json.DecodeException;
import io.vertx.core.json.JsonObject;

/**
 * @author ashish on 8/5/17.
 */
final class ContentFinderRepositoryImpl extends AbstractContentRepository implements ContentFinderRepository {
    private static final Logger LOGGER = LoggerFactory.getLogger(ContentFinderRepository.class);

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
                AlternatePathNUStrategyDao alternatePathNUStrategyDao = dbi.onDemand(AlternatePathNUStrategyDao.class);
                validRecordCount = alternatePathNUStrategyDao
                    .validatePath(contentAddress.getPathId(), contentAddress.getCurrentItem());
            }
        }
        return (validRecordCount > 0);
    }

    @Override
    public ContentAddress findFirstNotCompletedContentInCourse(FinderContext finderContext) {
        List<String> lessons;
        List<ContentAddress> contentAddresses;
        ContentFinderDao finderDao = dbi.onDemand(ContentFinderDao.class);

        String course = finderContext.getCurrentAddress().getCourse();
        List<String> units = finderDao.findUnitsInCourse(course);

        for (String unit : units) {
            lessons = finderDao.findLessonsInCU(course, unit);
            for (String lesson : lessons) {
                contentAddresses = finderDao.findCollectionsInCUL(course, unit, lesson);
                if (contentAddresses != null && !contentAddresses.isEmpty()) {
                    for (ContentAddress contentAddress : contentAddresses) {
                        if (contentAddress.getCollectionType() != CollectionType.Assessment) {
                            contentAddress.populateCurrentItemsFromCollections();
                            return contentAddress;
                        } else {
                            String competencies = finderDao
                                .findCompetenciesForCollection(course, unit, lesson, contentAddress.getCollection());
                            List<String> competencyList = parseCollectionTaxonomy(contentAddress, competencies);
                            if (competencyList == null || competencyList.isEmpty()) {
                                contentAddress.populateCurrentItemsFromCollections();
                                return contentAddress;
                            } else {
                                AlternatePathNUStrategyDao alternatePathNUStrategyDao =
                                    dbi.onDemand(AlternatePathNUStrategyDao.class);
                                List<String> completedCompetenciesByUser = alternatePathNUStrategyDao
                                    .findCompletedCompetenciesForUserInGivenList(finderContext.getUser(),
                                        CollectionUtils.convertToSqlArrayOfString(competencyList));
                                competencyList.removeAll(completedCompetenciesByUser);
                                if (!competencyList.isEmpty()) {
                                    contentAddress.populateCurrentItemsFromCollections();
                                    return contentAddress;
                                }
                            }
                        }
                    }
                }
            }
        }
        return new ContentAddress();
    }

    @Override
    public ContentAddress fetchNextItem(FinderContext finderContext) {
        return null;
    }

    @Override
    public ContentAddress findNextContentFromCULWithoutSkipLogicAndAlternatePaths(
        ContentAddress currentContentAddress) {
        ContentFinderDao finderDao = dbi.onDemand(ContentFinderDao.class);

        ContentAddress address = new ContentFinderNoSuggestionsDelegate(finderDao)
            .findNextContentFromCULWithoutAlternatePaths(currentContentAddress);

        if (address != null && address.getCollection() != null) {
            address.populateCurrentItemsFromCollections();
        }
        return address;
    }

    @Override
    public List<String> findResourceSuggestionsForAssessment(FinderContext finderContext) {
        return null;
    }

    @Override
    public void markCompetencyCompletedForUser(FinderContext finderContext) {

    }

    private static List<String> parseCollectionTaxonomy(ContentAddress contentAddress, String collectionTaxonomy) {
        if (collectionTaxonomy != null && !collectionTaxonomy.isEmpty()) {
            // Lesson taxonomy is supposed to be a JsonObject with keys as competencies' internal code
            // Note that they are not GDT aware but FW specific
            try {
                JsonObject taxonomy = new JsonObject(collectionTaxonomy);
                return new ArrayList<>(taxonomy.fieldNames());
            } catch (DecodeException ex) {
                LOGGER.warn("Invalid taxonomy string for address: Course='{}', Unit='{}', Lesson='{}', Collection='{}'",
                    contentAddress.getCourse(), contentAddress.getUnit(), contentAddress.getLesson(),
                    contentAddress.getCollection());
            }
        }
        return Collections.emptyList();
    }

}
