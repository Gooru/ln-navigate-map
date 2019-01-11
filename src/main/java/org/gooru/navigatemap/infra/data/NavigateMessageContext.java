package org.gooru.navigatemap.infra.data;

import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import org.gooru.navigatemap.app.constants.Constants;

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

  /*
   This is how the session will look like
   {"standard_preference":{"K12.SC":"NGSS","K12.MA":"CCSS","K12.SS":"C3","K12.ELA":"CCSS","K12.CS":"CSTA"}, "language_preference" : [1, 2]}
   Note that lang ids are Ints
   */
  public Integer getPrimaryLanguage() {
    // NOTE: Only returns primary language, which is first item in JsonArray
    JsonArray languageArray = session.getJsonArray("language_preference");
    if (languageArray != null && !languageArray.isEmpty()) {
      return languageArray.getInteger(0);
    }
    return null;
  }

  public boolean isUserAnonymous() {
    return this.userId.equalsIgnoreCase(Constants.Message.MSG_USER_ANONYMOUS);
  }
}
