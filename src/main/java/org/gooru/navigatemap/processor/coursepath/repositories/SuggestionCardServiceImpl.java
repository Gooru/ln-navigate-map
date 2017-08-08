package org.gooru.navigatemap.processor.coursepath.repositories;

import java.util.*;

import org.gooru.navigatemap.processor.coursepath.repositories.dao.SuggestionCardDao;
import org.gooru.navigatemap.processor.data.CollectionRQCount;
import org.gooru.navigatemap.processor.data.SuggestionCard;
import org.gooru.navigatemap.processor.utilities.CollectionUtils;
import org.gooru.navigatemap.processor.utilities.jdbi.PGArray;

/**
 * @author ashish on 7/5/17.
 */
class SuggestionCardServiceImpl extends AbstractContentRepository implements SuggestionCardService {
    @Override
    public List<SuggestionCard> suggestionCardForCollections(Set<String> collections) {
        if (collections != null && !collections.isEmpty()) {
            return createSuggestionsCardForCollections(collections);
        }
        return Collections.emptyList();
    }

    @Override
    public List<SuggestionCard> suggestionCardForResources(Set<String> resources) {
        if (resources != null && !resources.isEmpty()) {
            return createSuggestionsCardForResources(resources);
        }
        return Collections.emptyList();
    }

    private List<SuggestionCard> createSuggestionsCardForCollections(Set<String> collections) {
        SuggestionCardDao dao = dbi.onDemand(SuggestionCardDao.class);
        List<String> collectionsList = new ArrayList<>(collections);

        final PGArray<UUID> collectionsPGArray = CollectionUtils.convertToSqlArrayOfUUID(collectionsList);

        List<SuggestionCard> suggestionCards =
            dao.createSuggestionsCardForCollectionsWithoutRQCount(collectionsPGArray);

        List<CollectionRQCount> countRQForCollection = dao.fetchRQCountForCollections(collectionsPGArray);

        for (SuggestionCard suggestionCardWithoutRQCount : suggestionCards) {
            String collectionId = suggestionCardWithoutRQCount.getId();
            for (CollectionRQCount collectionRQCount : countRQForCollection) {
                if (Objects.equals(collectionId, collectionRQCount.getCollectionId())) {
                    suggestionCardWithoutRQCount.setQuestionCount(collectionRQCount.getQuestionCount());
                    suggestionCardWithoutRQCount.setResourceCount(collectionRQCount.getResourceCount());
                    break;
                }
            }
        }

        return suggestionCards;
    }

    private List<SuggestionCard> createSuggestionsCardForResources(Set<String> resources) {
        SuggestionCardDao dao = dbi.onDemand(SuggestionCardDao.class);
        List<String> resourcesList = new ArrayList<>(resources);

        return dao.createSuggestionsCardForResources(CollectionUtils.convertToSqlArrayOfUUID(resourcesList));
    }
}
