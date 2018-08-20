package org.gooru.navigatemap.app.components.helpers;

import io.vertx.core.json.JsonObject;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.MockProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.common.serialization.StringSerializer;

/**
 * @author ashish.
 */

public final class KafkaProducerBuilder {

    private KafkaProducerBuilder() {
        throw new AssertionError();
    }

    public static Producer buildKafkaProducer(JsonObject producerConfig) {
        return new KafkaProducer(producerConfig.getMap());
    }

    public static Producer buildMockProducer() {
        return new MockProducer<>(true, new StringSerializer(), new StringSerializer());
    }
}
