package org.gooru.navigatemap.processor.postprocessor;

import io.vertx.core.json.JsonObject;

/**
 * @author ashish.
 */

interface NotificationCoordinator {

    void coordinateNotification();

    static NotificationCoordinator buildForTeacherSuggestionAdded(
        PostProcessTeacherSuggestionAddHandler.TeacherSuggestionPayload command, JsonObject requestData) {
        return new TeacherSuggestionAddedNotificationCoordinator(command, requestData);
    }

    static NotificationCoordinator buildForTeacherSuggestionRead(PostProcessorNextCommand command) {
        return new TeacherSuggestionReadNotificationCoordinator(command);
    }

}
