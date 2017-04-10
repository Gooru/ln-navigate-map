package org.gooru.navigatemap.processor.contentserver;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.gooru.navigatemap.app.components.AppConfiguration;
import org.gooru.navigatemap.processor.coursepath.repositories.ContentSuggestionsService;
import org.gooru.navigatemap.processor.data.SuggestionCard4Collection;
import org.gooru.navigatemap.processor.data.SuggestionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.vertx.core.json.JsonArray;

/**
 * @author ashish on 4/4/17.
 */
class SuggestionsCardBuilder {
    private static final Integer DEFAULT_LIMIT = 1;
    private final SuggestionContext suggestionContext;
    private final ContentSuggestionsService suggestionRepository;
    private Set<String> collections;

    private static final Logger LOGGER = LoggerFactory.getLogger(SuggestionsCardBuilder.class);

    SuggestionsCardBuilder(SuggestionContext ctxSuggestions, ContentSuggestionsService contentSuggestionsService) {
        suggestionContext = ctxSuggestions;
        this.suggestionRepository = contentSuggestionsService;
    }

    JsonArray createSuggestionCards() {
        LOGGER.debug("Creating suggestion cards");
        if (!suggestionContext.hasSuggestions()) {
            LOGGER.warn("Suggestions Card builder invoked without any suggestions");
            return new JsonArray();
        }
        initializeSuggestedCollectionsList();
        applySuggestionsLimit();
        List<SuggestionCard4Collection> result = suggestionRepository.suggestionCardForCollections(collections);
        return transformSuggestionsCardToJson(result);
    }

    private static JsonArray transformSuggestionsCardToJson(List<SuggestionCard4Collection> result) {
        if (result != null && !result.isEmpty()) {
            try {
                String resultString = new ObjectMapper().writeValueAsString(result);
                return new JsonArray(resultString);
            } catch (JsonProcessingException e) {
                LOGGER.error("Not able to convert suggestions card to Json Array", e);
            }
        }
        return new JsonArray();
    }

    private void applySuggestionsLimit() {
        LOGGER.debug("Applying suggestions limit");
        Integer limit = AppConfiguration.getInstance().suggestionsLimit();
        if (limit == null) {
            limit = DEFAULT_LIMIT;
        }
        if (collections.size() > limit) {
            Set<String> limitedCollectionsSet = new HashSet<>(limit);
            int currentSize = 0;
            for (String item : collections) {
                limitedCollectionsSet.add(item);
                currentSize++;
                if (currentSize == limit) {
                    break;
                }
            }
            collections = limitedCollectionsSet;
        }
    }

    private void initializeSuggestedCollectionsList() {
        collections = new HashSet<>();
        if (suggestionContext.hasAssessmentsSuggested()) {
            LOGGER.debug("Will add assessments suggested to suggestions list");
            collections.addAll(suggestionContext.getAssessments());
        }
        if (suggestionContext.hasCollectionsSuggested()) {
            LOGGER.debug("Will add collections suggested to suggestions list");
            collections.addAll(suggestionContext.getCollections());
        }

    }
}
