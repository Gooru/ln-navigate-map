package org.gooru.navigatemap.routes;

import org.gooru.navigatemap.constants.Constants;
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
public class RouteNavigateConfigurator implements RouteConfigurator {

    private static final Logger LOGGER = LoggerFactory.getLogger(RouteNavigateConfigurator.class);
    private EventBus eb = null;
    private long mbusTimeout;

    @Override
    public void configureRoutes(Vertx vertx, Router router, JsonObject config) {
        eb = vertx.eventBus();
        mbusTimeout = config.getLong(Constants.EventBus.MBUS_TIMEOUT, 30L) * 1000;
        router.post(Constants.Route.API_NAVIGATE_NEXT).handler(this::navigateNext);
    }

    private void navigateNext(RoutingContext routingContext) {
        DeliveryOptions options = DeliveryOptionsBuilder.buildWithApiVersion(routingContext).setSendTimeout(mbusTimeout)
            .addHeader(Constants.Message.MSG_OP, Constants.Message.MSG_OP_NEXT);
        eb.<JsonObject>send(Constants.EventBus.MBEP_NAVIGATE,
            RouteRequestUtility.getBodyForMessage(routingContext), options,
            reply -> RouteResponseUtility.responseHandler(routingContext, reply, LOGGER));
    }
}
