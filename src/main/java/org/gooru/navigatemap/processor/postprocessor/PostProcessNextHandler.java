package org.gooru.navigatemap.processor.postprocessor;

import java.util.List;
import java.util.Objects;

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
        LOGGER.info("Handling the postprocessing for next output: '{}'", Objects.toString(requestData));
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
            getPostProcessorDao().insertAllSuggestions(suggestionTrackerModels);
        }
    }

    private void handleSystemPathItemServed() {
        if (command.getContext().onSystemPath()) {
            getPostProcessorDao().updatePathServeCount(command.getContext().getPathId());
        }
    }

    private void handleTeacherPathItemServed() {
        if (command.getContext().onTeacherPath()) {
            long currentCount = getPostProcessorDao().updatePathServeCount(command.getContext().getPathId());
            if (currentCount <= AppConfiguration.getInstance().getNotificationTeacherSuggestionReadThreshold()) {
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

}
