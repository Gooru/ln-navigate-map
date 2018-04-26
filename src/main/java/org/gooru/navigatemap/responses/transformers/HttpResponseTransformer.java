package org.gooru.navigatemap.responses.transformers;

import java.util.HashMap;
import java.util.Map;

import org.gooru.navigatemap.app.constants.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonObject;

/**
 * @author ashish on 24/2/2017.
 */
class HttpResponseTransformer implements ResponseTransformer {

    private static final Logger LOGGER = LoggerFactory.getLogger(ResponseTransformer.class);
    private final Message<JsonObject> message;
    private boolean transformed = false;
    private Map<String, String> headers;
    private int httpStatus;
    private JsonObject httpBody;

    public HttpResponseTransformer(Message<JsonObject> message) {
        this.message = message;
        if (message == null) {
            LOGGER.error("Invalid or null Message<JsonObject> for initialization");
            throw new IllegalArgumentException("Invalid or null Message<Object> for initialization");
        }
    }

    @Override
    public void transform() {
        if (!this.transformed) {
            processTransformation();
            this.transformed = true;
        }
    }

    @Override
    public JsonObject transformedBody() {
        transform();
        return this.httpBody;
    }

    @Override
    public Map<String, String> transformedHeaders() {
        transform();
        return this.headers;
    }

    @Override
    public int transformedStatus() {
        transform();
        return this.httpStatus;
    }

    private void processTransformation() {
        JsonObject messageBody = message.body();

        // First initialize the http status
        this.httpStatus = messageBody.getInteger(Constants.Message.MSG_HTTP_STATUS);

        // Then initialize the headers
        processHeaders(messageBody);

        this.httpBody = messageBody.getJsonObject(Constants.Message.MSG_HTTP_BODY);
        this.transformed = true;
    }

    private void processHeaders(JsonObject jsonObject) {
        JsonObject jsonHeaders = jsonObject.getJsonObject(Constants.Message.MSG_HTTP_HEADERS);
        this.headers = new HashMap<>();
        if (jsonHeaders != null && !jsonHeaders.isEmpty()) {
            Map<String, Object> headerMap = jsonHeaders.getMap();
            for (Map.Entry<String, Object> entry : headerMap.entrySet()) {
                this.headers.put(entry.getKey(), entry.getValue().toString());
            }
        }
    }

}
