package org.gooru.navigatemap.processor.postprocessor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author ashish on 25/7/18.
 */
class SuggestionsTrackerModelsBuilderForTeacherSuggestion
    implements SuggestionTrackerModelsBuilder {

  private final PostProcessTeacherSuggestionAddHandler.TeacherSuggestionPayload command;
  private static final Logger LOGGER =
      LoggerFactory.getLogger(SuggestionsTrackerModelsBuilderForTeacherSuggestion.class);

  SuggestionsTrackerModelsBuilderForTeacherSuggestion(
      PostProcessTeacherSuggestionAddHandler.TeacherSuggestionPayload command) {
    this.command = command;
  }

  @Override
  public List<SuggestionTrackerModel> build() {
    List<SuggestionTrackerModel> models = new ArrayList<>(command.getUserIds().size());
    Map<String, Integer> userPathMap = command.getUserPathMap();
    if (userPathMap == null || userPathMap.isEmpty()) {
      LOGGER.warn("User path map null or empty. Nothing to track");
      return models;
    }
    for (UUID userId : command.getUserIds()) {
      if (userPathMap.containsKey(userId.toString())) {
        Long pathId = Long.valueOf(userPathMap.get(userId.toString()));
        SuggestionTrackerModel model = buildSuggestionTrackerModel(userId, pathId);
        models.add(model);
      }
    }
    return models;
  }

  private SuggestionTrackerModel buildSuggestionTrackerModel(UUID userId, Long pathId) {
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
    model.setSuggestedContentType(SuggestionTrackerTypes.SuggestedContentType
        .builder(command.getSuggestedContentType()).getName());
    model.setSuggestedTo(SuggestionTrackerTypes.SuggestedTo.Student.getName());
    model.setAccepted(false);
    model.setAcceptedAt(null);
    model.setPathId(pathId);

    return model;
  }

}
