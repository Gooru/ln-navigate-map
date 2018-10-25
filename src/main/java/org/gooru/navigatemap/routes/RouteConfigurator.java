package org.gooru.navigatemap.routes;

import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;

/**
 * @author ashish on 24/2/17.
 */
public interface RouteConfigurator {

  void configureRoutes(Vertx vertx, Router router, JsonObject config);
}
