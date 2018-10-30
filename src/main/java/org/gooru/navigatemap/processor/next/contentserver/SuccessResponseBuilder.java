package org.gooru.navigatemap.processor.next.contentserver;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import org.gooru.navigatemap.app.constants.Constants;
import org.gooru.navigatemap.app.constants.HttpConstants;
import org.gooru.navigatemap.infra.data.ResponseContext;

/**
 * @author ashish on 6/3/17.
 */
final class SuccessResponseBuilder implements ResponseBuilder {

  private final JsonObject content;
  private final ResponseContext context;
  private final JsonArray suggestions;

  SuccessResponseBuilder(ResponseContext context, JsonObject content) {
    this.context = context;
    this.content = content;
    this.suggestions = new JsonArray();
  }

  SuccessResponseBuilder(ResponseContext context, JsonArray suggestions) {
    this.context = context;
    this.suggestions = suggestions;
    this.content = new JsonObject();
  }

  SuccessResponseBuilder(ResponseContext context) {
    this.context = context;
    this.suggestions = new JsonArray();
    this.content = new JsonObject();
  }

  @Override
  public JsonObject buildResponse() {
    JsonObject body = createHttpBodyResponse();

    return new JsonObject()
        .put(Constants.Message.MSG_HTTP_STATUS, HttpConstants.HttpStatus.SUCCESS.getCode())
        .put(Constants.Message.MSG_HTTP_BODY, body)
        .put(Constants.Message.MSG_HTTP_HEADERS, new JsonObject());

  }

  private JsonObject createHttpBodyResponse() {
    return new JsonObject().put(Constants.Response.RESP_CONTEXT, context.toJson())
        .put(Constants.Response.RESP_CONTENT, content)
        .put(Constants.Response.RESP_SUGGESTIONS, suggestions);
  }
}
