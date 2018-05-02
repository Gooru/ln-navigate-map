package org.gooru.navigatemap.processor.contentserver;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import org.gooru.navigatemap.processor.data.CollectionRQCount;
import org.gooru.navigatemap.processor.data.SuggestionCard;
import org.gooru.navigatemap.processor.utilities.CollectionUtils;
import org.gooru.navigatemap.processor.utilities.jdbi.PGArray;
import org.skife.jdbi.v2.DBI;

/**
 * @author ashish on 7/5/17.
 */
class SuggestionCardServiceImpl implements SuggestionCardService {

    private final DBI dbi;

    SuggestionCardServiceImpl(DBI dbi) {
        this.dbi = dbi;
    }

    @Override
    public List<SuggestionCard> suggestionCardForCollections(List<String> collections) {
        if (collections != null && !collections.isEmpty()) {
            return createSuggestionsCardForCollections(collections);
        }
        return Collections.emptyList();
    }

    private List<SuggestionCard> createSuggestionsCardForCollections(List<String> collections) {
        SuggestionCardDao dao = dbi.onDemand(SuggestionCardDao.class);

        final PGArray<UUID> collectionsPGArray = CollectionUtils.convertToSqlArrayOfUUID(collections);

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
}
