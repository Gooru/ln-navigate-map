package org.gooru.navigatemap.processor.contentserver;

import org.gooru.navigatemap.app.constants.Constants;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

/**
 * @author ashish on 22/9/17.
 */
public final class ResponseParser {

    private JsonObject response;

    private ResponseParser(JsonObject response) {
        this.response = response;
    }

    public JsonObject getResponse() {
        return response.copy();
    }

    public int getStatus() {
        return response.getInteger(Constants.Message.MSG_HTTP_STATUS);
    }

    public JsonObject getContext() {
        return response.getJsonObject(Constants.Message.MSG_HTTP_BODY).getJsonObject(Constants.Response.RESP_CONTEXT);
    }

    public JsonArray getSuggestions() {
        return response.getJsonObject(Constants.Message.MSG_HTTP_BODY).getJsonArray(Constants.Response.RESP_SUGGESTIONS);
    }

    public JsonObject getContent() {
        return response.getJsonObject(Constants.Message.MSG_HTTP_BODY).getJsonObject(Constants.Response.RESP_CONTENT);
    }

    public static ResponseParser build(JsonObject responseJson) {
        if (responseJson == null || responseJson.isEmpty()) {
            throw new IllegalStateException("No response to parse");
        }
        return new ResponseParser(responseJson.copy());
    }
}
