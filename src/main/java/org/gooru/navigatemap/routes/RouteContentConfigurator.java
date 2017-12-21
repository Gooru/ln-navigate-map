package org.gooru.navigatemap.routes;

import org.gooru.navigatemap.app.constants.Constants;
import org.gooru.navigatemap.routes.utils.DeliveryOptionsBuilder;
import org.gooru.navigatemap.routes.utils.RouteRequestUtility;
import org.gooru.navigatemap.routes.utils.RouteResponseUtility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.vertx.core.Vertx;
import io.vertx.core.eventbus.DeliveryOptions;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;

/**
 * @author ashish on 24/2/17.
 */
public class RouteContentConfigurator implements RouteConfigurator {

    private static final Logger LOGGER = LoggerFactory.getLogger(RouteContentConfigurator.class);
    private EventBus eb = null;
    private long mbusTimeout;

    @Override
    public void configureRoutes(Vertx vertx, Router router, JsonObject config) {
        eb = vertx.eventBus();
        mbusTimeout = config.getLong(Constants.EventBus.MBUS_TIMEOUT, 30L) * 1000;
        router.post(Constants.Route.API_TEACHER_SUGGESTIONS_ADD).handler(this::addTeacherSuggestions);
        router.get(Constants.Route.API_SYSTEM_SUGGESTIONS_ADD).handler(this::addSystemSuggestions);
    }

    private void addTeacherSuggestions(RoutingContext routingContext) {
        DeliveryOptions options = DeliveryOptionsBuilder.buildWithApiVersion(routingContext).setSendTimeout(mbusTimeout)
            .addHeader(Constants.Message.MSG_OP, Constants.Message.MSG_OP_TEACHER_SUGGESTION_ADD);
        eb.<JsonObject>send(Constants.EventBus.MBEP_CONTENT, RouteRequestUtility.getBodyForMessage(routingContext),
            options, reply -> RouteResponseUtility.responseHandler(routingContext, reply, LOGGER));
    }

    private void addSystemSuggestions(RoutingContext routingContext) {
        DeliveryOptions options = DeliveryOptionsBuilder.buildWithApiVersion(routingContext).setSendTimeout(mbusTimeout)
            .addHeader(Constants.Message.MSG_OP, Constants.Message.MSG_OP_SYSTEM_SUGGESTION_ADD);
        eb.<JsonObject>send(Constants.EventBus.MBEP_CONTENT, RouteRequestUtility.getBodyForMessage(routingContext),
            options, reply -> RouteResponseUtility.responseHandler(routingContext, reply, LOGGER));
    }
}
