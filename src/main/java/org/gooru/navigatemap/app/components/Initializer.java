package org.gooru.navigatemap.app.components;

import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;

/**
 * @author ashish on 24/2/17.
 */
public interface Initializer {

  void initializeComponent(Vertx vertx, JsonObject config);

}
