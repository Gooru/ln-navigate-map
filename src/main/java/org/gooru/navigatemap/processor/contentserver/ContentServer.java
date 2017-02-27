package org.gooru.navigatemap.processor.contentserver;

import org.gooru.navigatemap.constants.Constants;

import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;

/**
 * @author ashish on 26/2/17.
 */
public class ContentServer {
    private final Vertx vertx;

    public ContentServer(Vertx vertx) {
        this.vertx = vertx;
    }

    public JsonObject serveContent() {
        return serveDummyResponse();
    }

    private JsonObject serveDummyResponse() {
        return new JsonObject().put(Constants.Message.MSG_HTTP_STATUS, 200)
            .put(Constants.Message.MSG_HTTP_BODY, new JsonObject().put("status", "successful"))
            .put(Constants.Message.MSG_HTTP_HEADERS, new JsonObject());
    }
}
