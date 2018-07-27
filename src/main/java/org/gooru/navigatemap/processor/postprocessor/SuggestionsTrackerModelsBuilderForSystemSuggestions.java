package org.gooru.navigatemap.processor.postprocessor;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.gooru.navigatemap.infra.data.SuggestionCard;

/**
 * @author ashish on 25/7/18.
 */
class SuggestionsTrackerModelsBuilderForSystemSuggestions implements SuggestionTrackerModelsBuilder {

    private final PostProcessorNextCommand command;

    SuggestionsTrackerModelsBuilderForSystemSuggestions(PostProcessorNextCommand command) {
        this.command = command;
    }

    @Override
    public List<SuggestionTrackerModel> build() {
        if (command.getSuggestions() == null || command.getSuggestions().isEmpty()) {
            throw new IllegalArgumentException("Suggestions are null or empty");
        }
        List<SuggestionTrackerModel> models = new ArrayList<>(command.getSuggestions().size());

        for (SuggestionCard suggestionCard : command.getSuggestions()) {
            SuggestionTrackerModel model = buildSuggestionTrackerModel(suggestionCard);
            models.add(model);
        }
        return models;
    }

    private SuggestionTrackerModel buildSuggestionTrackerModel(SuggestionCard suggestionCard) {
        SuggestionTrackerModel model = new SuggestionTrackerModel();
        model.setUserId(command.getUserId());
        model.setCourseId(command.getContext().getCourseId());
        model.setUnitId(command.getContext().getUnitId());
        model.setLessonId(command.getContext().getLessonId());
        model.setClassId(command.getContext().getClassId());
        model.setCollectionId(command.getContext().getCollectionId());
        model.setSuggestedContentId(UUID.fromString(suggestionCard.getId()));
        model.setSuggestionOrigin(SuggestionTrackerTypes.SuggestionOrigin.System.getName());
        model.setSuggestionOriginatorId(null);
        model.setSuggestionCriteria(SuggestionTrackerTypes.SuggestionCriteria.Performance.getName());
        model.setSuggestedContentType(
            SuggestionTrackerTypes.SuggestedContentType.builder(suggestionCard.getFormat()).getName());
        model.setSuggestedTo(SuggestionTrackerTypes.SuggestedTo.Student.getName());
        model.setAccepted(false);
        model.setAcceptedAt(null);

        return model;
    }

}
