package org.gooru.navigatemap.app.components;

import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import org.apache.kafka.clients.producer.Producer;
import org.gooru.navigatemap.app.components.helpers.KafkaProducerBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author ashish on 24/2/17.
 */
public final class KafkaProducerRegistry implements Initializer, Finalizer {

    private static final String CONFIG_KAFKA_PRODUCERS_KEY = "kafka.producers";
    private static final String PRODUCER_TEACHER_SUGGESTION = "producer.teacher.suggestion";
    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaProducerRegistry.class);
    private static final String KAFKA_PRODUCERS_ENABLED = "kafka.producers.enabled";
    private static final String MOCK_PRODUCER_NAME = "mock.producer";

    private final Map<String, Producer> registry = new HashMap<>();
    private volatile boolean initialized = false;
    private Vertx vertx;
    private JsonObject globalConfig;
    private boolean isKafkaEnabled;

    private KafkaProducerRegistry() {
        // TODO Auto-generated constructor stub
    }

    public static KafkaProducerRegistry getInstance() {
        return Holder.INSTANCE;
    }

    @Override
    public void initializeComponent(Vertx vertx, JsonObject globalConfig) {
        this.vertx = vertx;
        this.globalConfig = globalConfig;
        // Skip if we are already initialized
        LOGGER.debug("Initialization called upon.");
        if (!initialized) {
            LOGGER.debug("May have to do initialization");
            synchronized (Holder.INSTANCE) {
                LOGGER.debug("Will initialize after double checking");
                if (!initialized) {
                    LOGGER.debug("Initializing now");
                    initialize();
                }
            }
        }
    }

    private void initialize() {
        isKafkaEnabled = globalConfig.getBoolean(KAFKA_PRODUCERS_ENABLED, false);
        if (isKafkaEnabled) {
            LOGGER.info("Kafka producers enabled, will create real producers.");
            JsonObject config = globalConfig.getJsonObject(CONFIG_KAFKA_PRODUCERS_KEY);
            if (config == null || config.isEmpty()) {
                throw new IllegalStateException("No configuration for producers found");
            }
            for (String producerName : config.fieldNames()) {
                JsonObject producerConfig = config.getJsonObject(producerName);
                if (producerConfig != null) {
                    Producer producer = KafkaProducerBuilder.buildKafkaProducer(producerConfig);
                    registry.put(producerName, producer);
                }
            }
        } else {
            LOGGER.warn("Kafka producers disabled, will create mock producers.");
            registry.put(MOCK_PRODUCER_NAME, KafkaProducerBuilder.buildMockProducer());
        }
        initialized = true;
    }

    public Producer getDefaultKafkaProducer() {
        if (isKafkaEnabled) {
            return registry.get(PRODUCER_TEACHER_SUGGESTION);
        } else {
            return registry.get(MOCK_PRODUCER_NAME);
        }
    }

    public Producer getTeacherSuggestionKafkaProducer() {
        return getKafkaProducerByName(PRODUCER_TEACHER_SUGGESTION);
    }

    public Producer getKafkaProducerByName(String name) {
        if (isKafkaEnabled) {
            if (name != null) {
                return registry.get(name);
            }
            return null;
        } else {
            return registry.get(MOCK_PRODUCER_NAME);
        }
    }

    @Override
    public void finalizeComponent() {
        for (Map.Entry<String, Producer> producerEntry : registry.entrySet()) {
            if (producerEntry.getValue() != null) {
                producerEntry.getValue().flush();
                producerEntry.getValue().close(5, TimeUnit.SECONDS);
            }
        }
    }

    private static final class Holder {
        private static final KafkaProducerRegistry INSTANCE = new KafkaProducerRegistry();
    }

}
