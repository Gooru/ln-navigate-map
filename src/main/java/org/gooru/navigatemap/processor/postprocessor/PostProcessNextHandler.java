package org.gooru.navigatemap.processor.postprocessor;

import java.util.ArrayList;
import java.util.List;
import org.gooru.navigatemap.app.components.AppConfiguration;
import org.skife.jdbi.v2.DBI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import io.vertx.core.json.JsonObject;

/**
 * @author ashish on 24/7/18.
 */
class PostProcessNextHandler implements PostProcessorHandler {

  private final DBI dbi;
  private PostProcessorNextCommand command;
  private static final Logger LOGGER = LoggerFactory.getLogger(PostProcessNextHandler.class);
  private PostProcessorDao postProcessorDao;

  PostProcessNextHandler(DBI dbi) {
    this.dbi = dbi;
  }

  @Override
  public void handle(JsonObject requestData) {
    LOGGER.info("Handling the postprocessing for next output: '{}'", requestData);
    command = PostProcessorNextCommand.buildFromJson(requestData);
    // 1. if we are serving content on teacher path, then update serve count for that item
    handleTeacherPathItemServed();
    // 2. if we are serving content on system path, then update the serve count for that item
    handleSystemPathItemServed();
    // 3. if we are giving any suggestion, make an entry into suggestions_tracker
    handleSuggestionsProvided();
  }

  private void handleSuggestionsProvided() {
    if (command.getSuggestions() != null && !command.getSuggestions().isEmpty()) {
      List<SuggestionTrackerModel> suggestionTrackerModels =
          SuggestionTrackerModelsBuilder.buildForSystemSuggestion(command).build();
      trackAllSuggestions(suggestionTrackerModels);
    }
  }

  private void handleSystemPathItemServed() {
    if (command.getContext().onSystemPath()) {
      getPostProcessorDao().updatePathServeCount(command.getContext().getPathId());
    }
  }

  private void handleTeacherPathItemServed() {
    if (command.getContext().onTeacherPath()) {
      long currentCount =
          getPostProcessorDao().updatePathServeCount(command.getContext().getPathId());
      if (currentCount <= AppConfiguration.getInstance()
          .getNotificationTeacherSuggestionReadThreshold()) {
        NotificationCoordinator.buildForTeacherSuggestionRead(command).coordinateNotification();
      }
    }
  }

  private PostProcessorDao getPostProcessorDao() {
    if (postProcessorDao == null) {
      postProcessorDao = dbi.onDemand(PostProcessorDao.class);
    }
    return postProcessorDao;
  }

  private void trackAllSuggestions(List<SuggestionTrackerModel> suggestionTrackerModels) {
    List<SuggestionTrackerModel> suggestionsInClassAtCollection = new ArrayList<>();
    List<SuggestionTrackerModel> suggestionsInClassAtLesson = new ArrayList<>();
    List<SuggestionTrackerModel> suggestionsForILAtCollection = new ArrayList<>();
    List<SuggestionTrackerModel> suggestionsForILAtLesson = new ArrayList<>();
    segregateSuggestionsPerContext(suggestionTrackerModels, suggestionsInClassAtCollection,
        suggestionsInClassAtLesson, suggestionsForILAtCollection, suggestionsForILAtLesson);
    persistSegregatedSuggestions(suggestionsInClassAtCollection, suggestionsInClassAtLesson,
        suggestionsForILAtCollection, suggestionsForILAtLesson);
  }

  private void segregateSuggestionsPerContext(List<SuggestionTrackerModel> suggestionTrackerModels,
      List<SuggestionTrackerModel> suggestionsInClassAtCollection,
      List<SuggestionTrackerModel> suggestionsInClassAtLesson,
      List<SuggestionTrackerModel> suggestionsForILAtCollection,
      List<SuggestionTrackerModel> suggestionsForILAtLesson) {
    suggestionTrackerModels.forEach(suggestionTrackerModel -> {
      if (suggestionTrackerModel.getClassId() == null) {
        if (suggestionTrackerModel.getCollectionId() == null) {
          suggestionsForILAtLesson.add(suggestionTrackerModel);
        } else {
          suggestionsForILAtCollection.add(suggestionTrackerModel);
        }
      } else {
        if (suggestionTrackerModel.getCollectionId() == null) {
          suggestionsInClassAtLesson.add(suggestionTrackerModel);
        } else {
          suggestionsInClassAtCollection.add(suggestionTrackerModel);
        }
      }
    });
  }

  private void persistSegregatedSuggestions(List<SuggestionTrackerModel> suggestionsInClassAtCollection,
      List<SuggestionTrackerModel> suggestionsInClassAtLesson,
      List<SuggestionTrackerModel> suggestionsForILAtCollection,
      List<SuggestionTrackerModel> suggestionsForILAtLesson) {
    if (!suggestionsForILAtLesson.isEmpty()) {
      getPostProcessorDao().insertSuggestionsForILAtLesson(suggestionsForILAtLesson);
    }
    if (!suggestionsForILAtCollection.isEmpty()) {
      getPostProcessorDao().insertSuggestionsForILAtCollection(suggestionsForILAtCollection);
    }
    if (!suggestionsInClassAtLesson.isEmpty()) {
      getPostProcessorDao().insertSuggestionsInClassAtLesson(suggestionsInClassAtLesson);
    }
    if (!suggestionsInClassAtCollection.isEmpty()) {
      getPostProcessorDao().insertSuggestionsInClassAtCollection(suggestionsInClassAtCollection);
    }
  }

}
