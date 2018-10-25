package org.gooru.navigatemap.processor.next.contentserver;

import io.vertx.core.json.JsonObject;
import org.gooru.navigatemap.app.constants.Constants;

/**
 * @author ashish on 6/3/17.
 */
final class ExceptionResponseBuilder implements ResponseBuilder {

  private final JsonObject body;
  private final int status;

  ExceptionResponseBuilder(int status, JsonObject body) {
    this.status = status;
    this.body = body != null ? body : new JsonObject();
  }

  @Override
  public JsonObject buildResponse() {
    return new JsonObject().put(Constants.Message.MSG_HTTP_STATUS, status)
        .put(Constants.Message.MSG_HTTP_BODY, body)
        .put(Constants.Message.MSG_HTTP_HEADERS, new JsonObject());

  }
}
