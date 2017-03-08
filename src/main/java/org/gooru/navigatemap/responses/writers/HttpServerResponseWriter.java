package org.gooru.navigatemap.responses.writers;

import java.nio.charset.StandardCharsets;
import java.util.Map;

import org.gooru.navigatemap.constants.HttpConstants;
import org.gooru.navigatemap.responses.transformers.ResponseTransformer;
import org.gooru.navigatemap.responses.transformers.ResponseTransformerBuilder;
import io.vertx.core.AsyncResult;
import io.vertx.core.eventbus.Message;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;

/**
 * @author ashish on 3/1/2017.
 */
class HttpServerResponseWriter implements ResponseWriter {

    private final RoutingContext routingContext;
    private final ResponseTransformer transformer;

    public HttpServerResponseWriter(RoutingContext routingContext, AsyncResult<Message<JsonObject>> message) {
        this.routingContext = routingContext;
        transformer = ResponseTransformerBuilder.build(message.result());
    }

    public HttpServerResponseWriter(RoutingContext routingContext, ResponseTransformer transformer) {
        this.routingContext = routingContext;
        this.transformer = transformer;
    }

    @Override
    public void writeResponse() {
        final HttpServerResponse response = routingContext.response();
        // First set the status code
        writeHttpStatus(response);
        // Then set the headers
        writeHttpHeaders(response);
        // Then it is turn of the body to be set and ending the response
        writeHttpBody(response);
    }

    private void writeHttpBody(HttpServerResponse response) {
        if (transformer.transformedStatus() != HttpConstants.HttpStatus.NO_CONTENT.getCode()) {
            final String responseBody =
                ((transformer.transformedBody() != null) && (!transformer.transformedBody().isEmpty())) ?
                    transformer.transformedBody().toString() : null;
            if (responseBody != null) {
                // As of today, we always serve JSON
                response.putHeader(HttpConstants.HEADER_CONTENT_TYPE, HttpConstants.CONTENT_TYPE_JSON);
                response.putHeader(HttpConstants.HEADER_CONTENT_LENGTH,
                    Integer.toString(responseBody.getBytes(StandardCharsets.UTF_8).length));
                response.end(responseBody);
            } else {
                response.end();
            }
        } else {
            response.end();
        }
    }

    private void writeHttpHeaders(HttpServerResponse response) {
        Map<String, String> headers = transformer.transformedHeaders();
        if (headers != null && !headers.isEmpty()) {
            // Never accept content-length from others, we do that
            headers.keySet().stream()
                .filter(headerName -> !headerName.equalsIgnoreCase(HttpConstants.HEADER_CONTENT_LENGTH))
                .forEach(headerName -> response.putHeader(headerName, headers.get(headerName)));
        }
    }

    private void writeHttpStatus(HttpServerResponse response) {
        response.setStatusCode(transformer.transformedStatus());
    }
}
