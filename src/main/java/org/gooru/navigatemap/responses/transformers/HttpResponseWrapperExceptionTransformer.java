package org.gooru.navigatemap.responses.transformers;

import java.util.Collections;
import java.util.Map;

import org.gooru.navigatemap.app.exceptions.HttpResponseWrapperException;

import io.vertx.core.json.JsonObject;

/**
 * @author ashish on 24/2/2017.
 */
public final class HttpResponseWrapperExceptionTransformer implements ResponseTransformer {

    private final HttpResponseWrapperException ex;

    HttpResponseWrapperExceptionTransformer(HttpResponseWrapperException ex) {
        this.ex = ex;
    }

    @Override
    public void transform() {
        // no op
    }

    @Override
    public JsonObject transformedBody() {
        return ex.getBody();
    }

    @Override
    public Map<String, String> transformedHeaders() {
        return Collections.emptyMap();
    }

    @Override
    public int transformedStatus() {
        return ex.getStatus();
    }
}
