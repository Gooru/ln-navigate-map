package org.gooru.navigatemap.bootstrap.verticles;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.http.HttpServer;
import io.vertx.ext.web.Router;

/**
 * @author ashish on 24/2/17.
 */
public class HttpVerticle extends AbstractVerticle {
    private static final Logger LOGGER = LoggerFactory.getLogger(HttpVerticle.class);

    @Override
    public void start(Future<Void> startFuture) throws Exception {
        LOGGER.info("Starting Http Verticle ...");
        final HttpServer httpServer = vertx.createHttpServer();

        final Router router = Router.router(vertx);
        configureRoutes(router);

        final int port = config().getInteger("http.port");
        LOGGER.info("Http Verticle starting on port: '{}'", port);
        httpServer.requestHandler(router::accept).listen(port, result -> {
            if (result.succeeded()) {
                LOGGER.info("Http Verticle started successfully");
                startFuture.complete();
            } else {
                LOGGER.error("Http Verticle failed to start", result.cause());
                startFuture.fail(result.cause());
            }
        });

    }

    @Override
    public void stop(Future<Void> stopFuture) throws Exception {
        // Currently a no op
        stopFuture.complete();
    }

    private void configureRoutes(final Router router) {
/*
        RouteConfiguration rc = new RouteConfiguration();
        for (RouteConfigurator configurator : rc) {
            configurator.configureRoutes(vertx, router, config());
        }
*/
    }

}
