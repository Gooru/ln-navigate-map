package org.gooru.navigatemap.processor.coursepath.repositories.nu;

import java.util.List;

import org.gooru.navigatemap.processor.coursepath.repositories.AbstractContentRepository;
import org.gooru.navigatemap.processor.coursepath.repositories.ContentFinderNoSuggestionsDelegate;
import org.gooru.navigatemap.processor.coursepath.repositories.dao.ContentFinderDao;
import org.gooru.navigatemap.processor.data.ContentAddress;
import org.gooru.navigatemap.processor.data.FinderContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author ashish on 8/5/17.
 */
final class ContentFinderRepositoryImpl extends AbstractContentRepository implements ContentFinderRepository {
    private static final Logger LOGGER = LoggerFactory.getLogger(ContentFinderRepository.class);

    @Override
    public boolean validateContentAddress(ContentAddress currentContentAddress) {
        return false;
    }

    @Override
    public ContentAddress findFirstNotCompletedContentInCourse(FinderContext finderContext) {
        return null;
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
}
