package org.gooru.navigatemap.processor.postprocessor;

import io.vertx.core.json.JsonObject;
import java.util.Objects;
import java.util.UUID;
import org.apache.kafka.clients.producer.Producer;
import org.gooru.navigatemap.app.components.AppConfiguration;
import org.gooru.navigatemap.app.components.KafkaProducerRegistry;
import org.gooru.navigatemap.app.constants.Constants;
import org.gooru.navigatemap.infra.data.PathType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author ashish.
 */

class TeacherSuggestionAddedNotificationCoordinator implements NotificationCoordinator {


  private static final String NOTIFICATION_TEACHER_SUGGESTION = "teacher.suggestion";
  private static final String ACTION_INITIATE = "initiate";
  private final PostProcessTeacherSuggestionAddHandler.TeacherSuggestionPayload command;
  private final JsonObject requestData;
  private static final Logger LOGGER = LoggerFactory
      .getLogger(TeacherSuggestionAddedNotificationCoordinator.class);
  private JsonObject userPathMap;

  TeacherSuggestionAddedNotificationCoordinator(
      PostProcessTeacherSuggestionAddHandler.TeacherSuggestionPayload command,
      JsonObject requestData) {

    this.command = command;
    this.requestData = requestData;
  }

  @Override
  public void coordinateNotification() {
    userPathMap = requestData.getJsonObject(Constants.Message.MSG_USER_PATH_MAP);
    if (userPathMap == null || userPathMap.isEmpty()) {
      LOGGER.warn("User path map null or empty. Won't send notification");
      return;
    }
    if (userPathMap.size() > command.getUserIds().size()) {
      LOGGER.warn(
          "Size of user path map is greater than that of users specified. Won't send notifications");
      return;
    }
    sendNotifications();
  }

  private void sendNotifications() {
    Producer<String, String> producer = KafkaProducerRegistry.getInstance()
        .getTeacherSuggestionKafkaProducer();
    NotificationSenderService senderService =
        NotificationSenderService
            .build(producer, AppConfiguration.getInstance().getNotificationTopic());

    for (UUID userId : command.getUserIds()) {
      NotificationsPayload payload = createNotificationPayload(userId);
      if (payload != null) {
        senderService.sendNotification(payload);
      } else {
        LOGGER.warn("Not able to create payload. Won't send notification.");
      }
    }
  }

  private NotificationsPayload createNotificationPayload(UUID userId) {
    Integer pathId = userPathMap.getInteger(userId.toString());
    if (pathId != null) {
      NotificationsPayload model = new NotificationsPayload();
      model.setUserId(userId.toString());
      model.setClassId(Objects.toString(command.getClassId(), null));
      model.setCourseId(Objects.toString(command.getCourseId(), null));
      model.setUnitId(Objects.toString(command.getUnitId(), null));
      model.setLessonId(Objects.toString(command.getLessonId(), null));
      model.setCollectionId(Objects.toString(command.getCollectionId(), null));
      model.setCurrentItemId(Objects.toString(command.getSuggestedContentId(), null));
      model.setCurrentItemType(command.getSuggestedContentType());
      model.setAction(ACTION_INITIATE);
      model.setPathId(Long.valueOf(pathId));
      model.setPathType(PathType.Teacher.getName());
      model.setNotificationType(NOTIFICATION_TEACHER_SUGGESTION);
      model.setContentSource(SuggestionArea.CourseMap.getName());
      return model;
    } else {
      LOGGER.warn("Path id not found for user: " + userId.toString());
      return null;
    }
  }

}
