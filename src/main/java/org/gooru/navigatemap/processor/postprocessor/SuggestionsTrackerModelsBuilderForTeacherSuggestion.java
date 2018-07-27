package org.gooru.navigatemap.processor.postprocessor;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @author ashish on 25/7/18.
 */
class SuggestionsTrackerModelsBuilderForTeacherSuggestion implements SuggestionTrackerModelsBuilder {

    private final PostProcessTeacherSuggestionAddHandler.TeacherSuggestionPayload command;

    SuggestionsTrackerModelsBuilderForTeacherSuggestion(
        PostProcessTeacherSuggestionAddHandler.TeacherSuggestionPayload command) {
        this.command = command;
    }

    @Override
    public List<SuggestionTrackerModel> build() {
        List<SuggestionTrackerModel> models = new ArrayList<>(command.getUserIds().size());

        for (UUID userId : command.getUserIds()) {
            SuggestionTrackerModel model = buildSuggestionTrackerModel(userId);
            models.add(model);
        }
        return models;
    }

    private SuggestionTrackerModel buildSuggestionTrackerModel(UUID userId) {
        SuggestionTrackerModel model = new SuggestionTrackerModel();
        model.setUserId(userId);
        model.setCourseId(command.getCourseId());
        model.setUnitId(command.getUnitId());
        model.setLessonId(command.getLessonId());
        model.setClassId(command.getClassId());
        model.setCollectionId(command.getCollectionId());
        model.setSuggestedContentId(command.getSuggestedContentId());
        model.setSuggestionOrigin(SuggestionTrackerTypes.SuggestionOrigin.Teacher.getName());
        model.setSuggestionOriginatorId(command.getTeacherId().toString());
        model.setSuggestionCriteria(SuggestionTrackerTypes.SuggestionCriteria.Performance.getName());
        model.setSuggestedContentType(
            SuggestionTrackerTypes.SuggestedContentType.builder(command.getSuggestedContentType()).getName());
        model.setSuggestedTo(SuggestionTrackerTypes.SuggestedTo.Student.getName());
        model.setAccepted(false);
        model.setAcceptedAt(null);

        return model;
    }

}
