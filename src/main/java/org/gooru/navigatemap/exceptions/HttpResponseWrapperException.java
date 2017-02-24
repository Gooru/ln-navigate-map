package org.gooru.navigatemap.exceptions;

import org.gooru.navigatemap.constants.Constants;
import org.gooru.navigatemap.constants.HttpConstants;

import io.vertx.core.json.JsonObject;

/**
 * @author ashish on 24/2/2017.
 */
public final class HttpResponseWrapperException extends RuntimeException {
    private final HttpConstants.HttpStatus status;
    private final JsonObject payload;

    public HttpResponseWrapperException(HttpConstants.HttpStatus status, JsonObject payload) {
        this.status = status;
        this.payload = payload;
    }

    public HttpResponseWrapperException(HttpConstants.HttpStatus status, String message) {
        this.status = status;
        this.payload = new JsonObject().put(Constants.Message.MSG_MESSAGE, message);
    }

    public int getStatus() {
        return this.status.getCode();
    }

    public JsonObject getBody() {
        return this.payload;
    }
}
