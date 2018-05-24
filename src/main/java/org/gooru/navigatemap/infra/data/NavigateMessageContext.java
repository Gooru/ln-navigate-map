package org.gooru.navigatemap.infra.data;

import org.gooru.navigatemap.app.constants.Constants;

import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonObject;

/**
 * @author ashish on 27/2/17.
 */
public final class NavigateMessageContext {

    private final Message<JsonObject> message;
    private final String sessionToken;
    private final String userId;
    private final JsonObject session;
    private final JsonObject request;

    public NavigateMessageContext(Message<JsonObject> message) {
        this.message = message;
        JsonObject messageBody = message.body();
        this.sessionToken = messageBody.getString(Constants.Message.MSG_SESSION_TOKEN);
        this.userId = messageBody.getString(Constants.Message.MSG_USER_ID);
        this.session = messageBody.getJsonObject(Constants.Message.MSG_KEY_SESSION).copy();
        this.request = messageBody.getJsonObject(Constants.Message.MSG_HTTP_BODY);
    }

    public String getSessionToken() {
        return sessionToken;
    }

    public String getUserId() {
        return userId;
    }

    public JsonObject getRequest() {
        return request;
    }

    public JsonObject getSession() {
        return session;
    }

    public boolean isUserAnonymous() {
        return this.userId.equalsIgnoreCase(Constants.Message.MSG_USER_ANONYMOUS);
    }
}
