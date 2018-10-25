package org.gooru.navigatemap.responses.auth;

import io.vertx.core.json.JsonObject;

/**
 * @author ashish on 24/2/17.
 */
public interface AuthSessionResponseHolder extends AuthResponseHolder {

  JsonObject getSession();
}
