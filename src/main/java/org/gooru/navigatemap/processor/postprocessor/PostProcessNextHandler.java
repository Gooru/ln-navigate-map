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
      List<SuggestionTrackerModel> suggestionTrackerModelsToPersist = new ArrayList<>();
      suggestionTrackerModels.forEach(suggestionTrackerModel -> {
        Long id = null;
        if (suggestionTrackerModel.getClassId() == null) {
          id = fetchExistingSuggestionTrackedForIL(suggestionTrackerModel);
        } else {
          id = FetchExistingSuggestionTrackedInClass(suggestionTrackerModel);
        }
        if (id == null) {
          suggestionTrackerModelsToPersist.add(suggestionTrackerModel);
        } else {
          getPostProcessorDao().updateExistingSuggestionWithCurrentDateTime(id);
        }
      });
      if (!suggestionTrackerModelsToPersist.isEmpty()) {
        getPostProcessorDao().insertAllSuggestions(suggestionTrackerModelsToPersist);
      }
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

  private Long FetchExistingSuggestionTrackedInClass(
      SuggestionTrackerModel suggestionTrackerModel) {
    if (suggestionTrackerModel.getCollectionId() == null) {
      return getPostProcessorDao()
          .fetchExistingSuggestionTrackedInClassAtLesson(suggestionTrackerModel);
    } else {
      return getPostProcessorDao()
          .fetchExistingSuggestionTrackedInClassAtCollection(suggestionTrackerModel);
    }
  }

  private Long fetchExistingSuggestionTrackedForIL(SuggestionTrackerModel suggestionTrackerModel) {
    if (suggestionTrackerModel.getCollectionId() == null) {
      return getPostProcessorDao()
          .fetchExistingSuggestionTrackedForILAtLesson(suggestionTrackerModel);
    } else {
      return getPostProcessorDao()
          .fetchExistingSuggestionTrackedForILAtCollection(suggestionTrackerModel);
    }
  }
}
