package org.gooru.navigatemap.processor.postprocessor;

import java.util.Objects;
import org.apache.kafka.clients.producer.Producer;
import org.gooru.navigatemap.app.components.AppConfiguration;
import org.gooru.navigatemap.app.components.KafkaProducerRegistry;
import org.gooru.navigatemap.infra.data.PathType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author ashish.
 */

class TeacherSuggestionReadNotificationCoordinator implements NotificationCoordinator {


  private static final String NOTIFICATION_TEACHER_SUGGESTION = "teacher.suggestion";
  private static final String ACTION_COMPLETE = "complete";
  private final PostProcessorNextCommand command;
  private static final Logger LOGGER = LoggerFactory
      .getLogger(TeacherSuggestionReadNotificationCoordinator.class);

  TeacherSuggestionReadNotificationCoordinator(PostProcessorNextCommand command) {
    this.command = command;
  }

  @Override
  public void coordinateNotification() {
    sendNotifications();
  }

  private void sendNotifications() {
    Producer<String, String> producer = KafkaProducerRegistry.getInstance()
        .getTeacherSuggestionKafkaProducer();
    NotificationSenderService senderService =
        NotificationSenderService
            .build(producer, AppConfiguration.getInstance().getNotificationTopic());
    NotificationsPayload payload = createNotificationPayload();
    if (payload != null) {
      senderService.sendNotification(payload);
    } else {
      LOGGER.warn("Not able to create payload. Won't send notification.");
    }
  }

  private NotificationsPayload createNotificationPayload() {
    if (command.getContext().onTeacherPath()) {
      NotificationsPayload model = new NotificationsPayload();
      model.setUserId(command.getUserId().toString());
      model.setClassId(Objects.toString(command.getContext().getClassId(), null));
      model.setCourseId(Objects.toString(command.getContext().getCourseId(), null));
      model.setUnitId(Objects.toString(command.getContext().getUnitId(), null));
      model.setLessonId(Objects.toString(command.getContext().getLessonId(), null));
      model.setCollectionId(Objects.toString(command.getContext().getCollectionId(), null));
      model.setCurrentItemId(Objects.toString(command.getContext().getCurrentItemId(), null));
      model.setCurrentItemType(command.getContext().getCurrentItemType().getName());
      model.setAction(ACTION_COMPLETE);
      model.setPathId(command.getContext().getPathId());
      model.setPathType(PathType.Teacher.getName());
      model.setNotificationType(NOTIFICATION_TEACHER_SUGGESTION);
      model.setContentSource(SuggestionArea.CourseMap.getName());
      return model;
    } else {
      LOGGER.warn("Not a teacher path for reset notification: " + command.getUserId().toString());
      return null;
    }
  }

}
