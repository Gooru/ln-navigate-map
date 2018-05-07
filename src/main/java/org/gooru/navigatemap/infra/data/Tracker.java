package org.gooru.navigatemap.infra.data;

import io.vertx.core.json.JsonObject;

/**
 * @author ashish on 7/5/18.
 */
public class Tracker {
    private String name;
    private JsonObject payload;

    public String getName() {
        return name;
    }

    public JsonObject getPayload() {
        return payload;
    }

    public Tracker(String name, JsonObject payload) {
        this.name = name;
        this.payload = payload;
    }
}
