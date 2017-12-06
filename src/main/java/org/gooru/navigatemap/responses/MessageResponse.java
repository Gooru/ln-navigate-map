package org.gooru.navigatemap.responses;

import org.gooru.navigatemap.app.constants.Constants;
import org.gooru.navigatemap.app.constants.HttpConstants;

import io.vertx.core.eventbus.DeliveryOptions;
import io.vertx.core.json.JsonObject;

/**
 * @author ashish on 24/2/2017.
 */
public final class MessageResponse {

    private final DeliveryOptions deliveryOptions;
    private final JsonObject reply;

    // Private constructor
    private MessageResponse(JsonObject response) {
        this.deliveryOptions = new DeliveryOptions();
        this.reply = response;
    }

    public DeliveryOptions deliveryOptions() {
        return this.deliveryOptions;
    }

    public JsonObject reply() {
        return this.reply;
    }

    public boolean isSuccessful() {
        return reply.getInteger(Constants.Message.MSG_HTTP_STATUS) == HttpConstants.HttpStatus.SUCCESS.getCode();
    }

    // Public builder with validations
    public static class Builder {
        private HttpConstants.HttpStatus httpStatus = null;
        private JsonObject responseBody = null;
        private JsonObject headers = null;

        public Builder() {
            this.headers = new JsonObject();
            // Default content type is JSON, unless caller overrides it later
            this.setContentTypeJson();
        }

        // Setters for http status code
        public Builder setStatusCreated() {
            this.httpStatus = HttpConstants.HttpStatus.CREATED;
            return this;
        }

        public Builder setStatusOkay() {
            this.httpStatus = HttpConstants.HttpStatus.SUCCESS;
            return this;
        }

        public Builder setStatusNoOutput() {
            this.httpStatus = HttpConstants.HttpStatus.NO_CONTENT;
            return this;
        }

        public Builder setStatusBadRequest() {
            this.httpStatus = HttpConstants.HttpStatus.BAD_REQUEST;
            return this;
        }

        public Builder setStatusForbidden() {
            this.httpStatus = HttpConstants.HttpStatus.FORBIDDEN;
            return this;
        }

        public Builder setStatusNotFound() {
            this.httpStatus = HttpConstants.HttpStatus.NOT_FOUND;
            return this;
        }

        public Builder setStatusInternalError() {
            this.httpStatus = HttpConstants.HttpStatus.ERROR;
            return this;
        }

        public Builder setStatusHttpCode(HttpConstants.HttpStatus httpStatus) {
            this.httpStatus = httpStatus;
            return this;
        }

        // Setters for headers
        public Builder setContentTypeJson() {
            this.headers.put(HttpConstants.HEADER_CONTENT_TYPE, HttpConstants.CONTENT_TYPE_JSON);
            return this;
        }

        public Builder setHeader(String key, String value) {
            if (key == null || value == null) {
                return this;
            }
            if (key.equalsIgnoreCase(HttpConstants.HEADER_CONTENT_LENGTH)) {
                // Do not allow content length to be setup, it should be handled
                // in gateway
                return this;
            }
            this.headers.put(key, value);
            return this;
        }

        // Setters for Response body, interpreted as per status of message
        public Builder setResponseBody(JsonObject responseBody) {
            this.responseBody = responseBody;
            return this;
        }

        public MessageResponse build() {
            JsonObject result = new JsonObject();
            result.put(Constants.Message.MSG_HTTP_STATUS, this.httpStatus.getCode())
                .put(Constants.Message.MSG_HTTP_HEADERS, this.headers)
                .put(Constants.Message.MSG_HTTP_BODY, this.responseBody == null ? new JsonObject() : this.responseBody);
            return new MessageResponse(result);
        }

    }
}
