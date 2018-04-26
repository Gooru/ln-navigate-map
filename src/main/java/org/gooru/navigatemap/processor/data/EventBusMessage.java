package org.gooru.navigatemap.processor.data;

import java.util.UUID;

import org.gooru.navigatemap.app.constants.Constants;

import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonObject;

/**
 * @author ashish on 23/11/17.
 */
public final class EventBusMessage {

    private final String sessionToken;
    private final JsonObject requestBody;
    private final UUID userId;
    private final JsonObject session;

    public String getSessionToken() {
        return sessionToken;
    }

    public JsonObject getRequestBody() {
        return requestBody;
    }

    public UUID getUserId() {
        return userId;
    }

    public JsonObject getSession() {
        return session;
    }

    private EventBusMessage(String sessionToken, JsonObject requestBody, UUID userId, JsonObject session) {
        this.sessionToken = sessionToken;
        this.requestBody = requestBody;
        this.userId = userId;
        this.session = session;
    }

    public static EventBusMessage eventBusMessageBuilder(Message<JsonObject> message) {
        String sessionToken = message.body().getString(Constants.Message.MSG_SESSION_TOKEN);
        String userId = message.body().getString(Constants.Message.MSG_USER_ID);
        JsonObject requestBody = message.body().getJsonObject(Constants.Message.MSG_HTTP_BODY);
        JsonObject session = message.body().getJsonObject(Constants.Message.MSG_KEY_SESSION);

        return new EventBusMessage(sessionToken, requestBody, UUID.fromString(userId), session);
    }
}
