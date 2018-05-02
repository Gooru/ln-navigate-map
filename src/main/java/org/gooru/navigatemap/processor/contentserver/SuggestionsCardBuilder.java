package org.gooru.navigatemap.processor.contentserver;

import java.util.ArrayList;
import java.util.List;

import org.gooru.navigatemap.app.components.AppConfiguration;
import org.gooru.navigatemap.processor.data.SuggestionCard;
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
    private List<String> suggestions;
    private final SuggestionCardService suggestionCardService;

    private static final Logger LOGGER = LoggerFactory.getLogger(SuggestionsCardBuilder.class);

    SuggestionsCardBuilder(SuggestionContext ctxSuggestions, SuggestionCardService suggestionCardService) {
        this.suggestionContext = ctxSuggestions;
        this.suggestionCardService = suggestionCardService;
    }

    JsonArray createSuggestionCards() {
        LOGGER.debug("Creating suggestion cards");
        if (!suggestionContext.hasSuggestions()) {
            LOGGER.warn("Suggestions Card builder invoked without any suggestions");
            return new JsonArray();
        }
        initializeSuggestionsList();
        applySuggestionsLimit();
        List<SuggestionCard> result = getSuggestionCards();
        return transformSuggestionsCardToJson(result);
    }

    private List<SuggestionCard> getSuggestionCards() {
        return suggestionCardService.suggestionCardForCollections(suggestions);
    }

    private void applySuggestionsLimit() {
        LOGGER.debug("Applying suggestions limit");
        Integer limit = AppConfiguration.getInstance().suggestionsLimit();
        if (limit == null) {
            limit = DEFAULT_LIMIT;
        }
        if (suggestions.size() > limit) {
            List<String> limitedCollectionsList = new ArrayList<>(limit);
            int currentSize = 0;
            for (String item : suggestions) {
                limitedCollectionsList.add(item);
                currentSize++;
                if (currentSize == limit) {
                    break;
                }
            }
            suggestions = limitedCollectionsList;
        }
    }

    private void initializeSuggestionsList() {
        suggestions = new ArrayList<>();
        if (suggestionContext.hasAssessmentsSuggested()) {
            LOGGER.debug("Will add assessments suggested to suggestions list");
            suggestions.addAll(suggestionContext.getAssessments());
        }
        if (suggestionContext.hasCollectionsSuggested()) {
            LOGGER.debug("Will add suggestions suggested to suggestions list");
            suggestions.addAll(suggestionContext.getCollections());
        }
    }

    private static JsonArray transformSuggestionsCardToJson(List<SuggestionCard> result) {
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

}
