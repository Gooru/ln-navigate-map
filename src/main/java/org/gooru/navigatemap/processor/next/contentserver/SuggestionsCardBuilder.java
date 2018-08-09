package org.gooru.navigatemap.processor.next.contentserver;

import java.util.ArrayList;
import java.util.List;

import org.gooru.navigatemap.app.components.AppConfiguration;
import org.gooru.navigatemap.infra.data.SuggestionCard;
import org.gooru.navigatemap.infra.data.SuggestionContext;
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
    private List<SuggestionCard> suggestionCards;

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
        fetchSuggestionCards();
        applySuggestionsLimit();
        return transformSuggestionsCardToJson();
    }

    private void fetchSuggestionCards() {
        suggestionCards = suggestionCardService.suggestionCardForCollections(suggestions);
        for (SuggestionCard suggestionCard : suggestionCards) {
            suggestionCard.setSuggestedContentSubType(suggestionContext.getSuggestedContentSubType());
        }
    }

    private void applySuggestionsLimit() {
        LOGGER.debug("Applying suggestions limit");
        Integer limit = AppConfiguration.getInstance().suggestionsLimit();
        if (limit == null) {
            limit = DEFAULT_LIMIT;
        }
        if (suggestionCards.size() > limit) {
            suggestionCards.subList(limit, suggestionCards.size()).clear();
        }
        if (suggestionCards.isEmpty()) {
            LOGGER.warn("Suggestions are empty after querying the db.");
            LOGGER.warn("Original ids fetched were:");
            for (String suggestion : suggestions) {
                LOGGER.warn("Vanished Id: '{}'", suggestion);
            }
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

    private JsonArray transformSuggestionsCardToJson() {
        if (suggestionCards != null && !suggestionCards.isEmpty()) {
            try {
                String resultString = new ObjectMapper().writeValueAsString(suggestionCards);
                return new JsonArray(resultString);
            } catch (JsonProcessingException e) {
                LOGGER.error("Not able to convert suggestions card to Json Array", e);
            }
        } else {
            LOGGER.warn("Suggestions cards are empty");
        }
        return new JsonArray();
    }

}
