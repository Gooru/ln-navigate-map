package org.gooru.navigatemap.routes.utils;

import java.util.List;
import java.util.Map;

import org.gooru.navigatemap.constants.Constants;

import io.netty.handler.codec.http.QueryStringDecoder;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;

/**
 * Created by ashish on 24/2/2017.
 */
public class RouteRequestUtility {

    /*
     * If the incoming request is POST or PUT, it is expected to have a payload
     * of JSON which is returned. In case of GET request, any query parameters
     * will be used to create a JSON body. Note that only query string is used
     * and not path matchers. In case of no query parameters send out empty Json
     * object, but don't send null
     */

    public static JsonObject getBodyForMessage(RoutingContext routingContext) {
        JsonObject httpBody, result = new JsonObject();
        if (routingContext.request().method().name().equals(HttpMethod.POST.name()) || routingContext.request().method()
            .name().equals(HttpMethod.PUT.name())) {
            httpBody = routingContext.getBodyAsJson();
        } else if (routingContext.request().method().name().equals(HttpMethod.GET.name())) {
            httpBody = new JsonObject();
            String uri = routingContext.request().query();
            if (uri != null) {
                QueryStringDecoder queryStringDecoder = new QueryStringDecoder(uri, false);
                Map<String, List<String>> prms = queryStringDecoder.parameters();
                if (!prms.isEmpty()) {
                    for (Map.Entry<String, List<String>> entry : prms.entrySet()) {
                        httpBody.put(entry.getKey(), entry.getValue());
                    }
                }
            }
        } else {
            httpBody = new JsonObject();
        }
        result.put(Constants.Message.MSG_HTTP_BODY, httpBody);
        result
            .put(Constants.Message.MSG_KEY_SESSION, (JsonObject) routingContext.get(Constants.Message.MSG_KEY_SESSION));
        result.put(Constants.Message.MSG_USER_ID, (String) routingContext.get(Constants.Message.MSG_USER_ID));
        result
            .put(Constants.Message.MSG_SESSION_TOKEN, (String) routingContext.get(Constants.Message.MSG_SESSION_TOKEN));
        return result;
    }
}