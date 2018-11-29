package org.gooru.navigatemap.processor.postprocessor;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author ashish.
 */

class DefaultNotificationSenderService implements NotificationSenderService {

  private final Producer<String, String> producer;
  private final String topic;
  private static final Logger LOGGER = LoggerFactory
      .getLogger(DefaultNotificationSenderService.class);

  DefaultNotificationSenderService(Producer<String, String> producer, String topic) {
    this.producer = producer;
    this.topic = topic;
  }

  @Override
  public void sendNotification(NotificationsPayload payload) {
    try {
      String message = new ObjectMapper().writeValueAsString(payload);
      ProducerRecord<String, String> producerRecord = new ProducerRecord<>(topic, message);
      if (producer != null) {
        producer.send(producerRecord, (metadata, exception) -> {
          if (exception == null) {
            LOGGER.debug(
                "Message Delivered, got metadata: Offset : " + metadata.offset() + " : Topic : " +
                    metadata.topic() + " : Partition : " + metadata.partition());
          } else {
            LOGGER.warn("Failed to send message.", exception);
          }
        });
      } else {
        LOGGER.warn("Not able to obtain producer instance. Notification not sent.");
      }
    } catch (JsonProcessingException e) {
      LOGGER.warn("Not able to convert payload to Json String. Won't send notification.", e);
    }
  }
}
