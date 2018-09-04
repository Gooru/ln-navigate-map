package org.gooru.navigatemap.app.components.helpers;

import io.vertx.core.json.JsonObject;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.MockProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Map;

/**
 * Build Kafka producers.
 *
 * Currently only producers it can create are of String key and String record type. If there is a need to create a
 * different type of producer, the class needs to be modified to handle that.
 * @author ashish.
 */

public final class KafkaProducerBuilder {

    private KafkaProducerBuilder() {
        throw new AssertionError();
    }

    public static Producer<String, String> buildKafkaProducer(JsonObject producerConfig) {
        Map<String, Object> props = producerConfig.getMap();
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        return new KafkaProducer<>(props);
    }

    public static Producer<String, String> buildMockProducer() {
        return new MockProducer<>(true, new StringSerializer(), new StringSerializer());
    }
}
