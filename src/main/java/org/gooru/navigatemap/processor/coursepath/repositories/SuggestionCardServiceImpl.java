package org.gooru.navigatemap.processor.coursepath.repositories;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.gooru.navigatemap.processor.coursepath.repositories.dao.SuggestionCardDao;
import org.gooru.navigatemap.processor.data.SuggestionCard;
import org.gooru.navigatemap.processor.utilities.CollectionUtils;

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

        return dao.createSuggestionsCardForCollections(CollectionUtils.convertToSqlArrayOfUUID(collectionsList));
    }

    private List<SuggestionCard> createSuggestionsCardForResources(Set<String> resources) {
        SuggestionCardDao dao = dbi.onDemand(SuggestionCardDao.class);
        List<String> resourcesList = new ArrayList<>(resources);

        return dao.createSuggestionsCardForResources(CollectionUtils.convertToSqlArrayOfUUID(resourcesList));
    }
}
