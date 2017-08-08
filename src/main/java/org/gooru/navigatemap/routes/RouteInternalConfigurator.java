package org.gooru.navigatemap.routes;

import org.gooru.navigatemap.constants.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.dropwizard.MetricsService;
import io.vertx.ext.web.Router;

/**
 * @author ashish on 24/2/17.
 */
class RouteInternalConfigurator implements RouteConfigurator {

    private static final Logger LOGGER = LoggerFactory.getLogger(RouteInternalConfigurator.class);

    @Override
    public void configureRoutes(Vertx vertx, Router router, JsonObject config) {
        LOGGER.debug("Configuring routes for internal route");
        router.route(Constants.Route.API_INTERNAL_BANNER).handler(routingContext -> {
            JsonObject result =
                new JsonObject().put("Organisation", "gooru.org").put("Product", "navigator-map").put("purpose", "api")
                    .put("mission", "Honor the human right to education");
            routingContext.response().end(result.toString());
        });

        final MetricsService metricsService = MetricsService.create(vertx);
        router.route(Constants.Route.API_INTERNAL_METRICS).handler(routingContext -> {
            JsonObject ebMetrics = metricsService.getMetricsSnapshot(vertx);
            routingContext.response().end(ebMetrics.toString());
        });

    }
}
