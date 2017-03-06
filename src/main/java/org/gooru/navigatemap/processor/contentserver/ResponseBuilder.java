package org.gooru.navigatemap.processor.contentserver;

import org.gooru.navigatemap.processor.data.ResponseContext;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

/**
 * @author ashish on 6/3/17.
 */
public interface ResponseBuilder {
    JsonObject buildResponse();

    static ResponseBuilder createSuccessResponseBuilder(ResponseContext context, JsonObject content) {
        return new SuccessResponseBuilder(context, content);
    }

    static ResponseBuilder createSuccessResponseBuilder(ResponseContext context, JsonArray suggestions) {
        return new SuccessResponseBuilder(context, suggestions);
    }

    static ResponseBuilder createExceptionResponseBuilder(int status, JsonObject body) {
        return new ExceptionResponseBuilder(status, body);
    }
}
