package org.gooru.navigatemap.processor.next.contentserver;

import org.gooru.navigatemap.app.constants.Constants;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

/**
 * @author ashish on 22/9/17.
 */
public final class PostProcessorPayloadCreator {

    private final JsonObject response;

    private PostProcessorPayloadCreator(JsonObject response) {
        this.response = response;
    }

    public JsonObject getResponse() {
        return response.copy();
    }

    public JsonObject getContext() {
        return response.getJsonObject(Constants.Response.RESP_CONTEXT);
    }

    public JsonArray getSuggestions() {
        return response.getJsonArray(Constants.Response.RESP_SUGGESTIONS);
    }

    /**
     * Remove the data specific to content section as it is huge and create another JsonObject to do postprocessing
     *
     *
     * @param user
     * @param responseJson The response sent as part of Next API to caller
     * @return A new object which does not have dependency on content section of response
     */
    public static PostProcessorPayloadCreator buildFromNextApiResponse(String user, JsonObject responseJson) {
        if (responseJson == null || responseJson.isEmpty()) {
            throw new IllegalStateException("No response to parse");
        }
        JsonObject postProcessorResponse = new JsonObject().put(Constants.Response.RESP_CONTEXT,
            responseJson.getJsonObject(Constants.Message.MSG_HTTP_BODY).getJsonObject(Constants.Response.RESP_CONTEXT))
                .put(Constants.Response.RESP_SUGGESTIONS, responseJson.getJsonObject(Constants.Message.MSG_HTTP_BODY)
                    .getJsonArray(Constants.Response.RESP_SUGGESTIONS)).put(Constants.Message.MSG_USER_ID, user);
        return new PostProcessorPayloadCreator(postProcessorResponse.copy());
    }
}
