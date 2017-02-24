package org.gooru.navigatemap.routes.utils;

import org.gooru.navigatemap.responses.writers.ResponseWriterBuilder;
import org.slf4j.Logger;

import io.vertx.core.AsyncResult;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;

/**
 * Created by ashish on 24/2/17.
 */
public class RouteResponseUtility {

    public void responseHandler(final RoutingContext routingContext, final AsyncResult<Message<JsonObject>> reply,
        final Logger LOG) {
        if (reply.succeeded()) {
            ResponseWriterBuilder.build(routingContext, reply).writeResponse();
        } else {
            LOG.error("Not able to send message", reply.cause());
            routingContext.response().setStatusCode(500).end();
        }
    }
}
