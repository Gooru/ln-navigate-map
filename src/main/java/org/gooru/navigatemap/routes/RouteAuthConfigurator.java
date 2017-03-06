package org.gooru.navigatemap.routes;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.gooru.navigatemap.constants.Constants;
import org.gooru.navigatemap.constants.HttpConstants;
import org.gooru.navigatemap.responses.auth.AuthSessionResponseHolder;
import org.gooru.navigatemap.responses.auth.AuthSessionResponseHolderBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.vertx.core.Vertx;
import io.vertx.core.eventbus.DeliveryOptions;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;

/**
 * @author ashish on 24/2/17.
 */
class RouteAuthConfigurator implements RouteConfigurator {

    private static final Logger LOG = LoggerFactory.getLogger(RouteAuthConfigurator.class);
    private static final String HEADER_AUTH_PREFIX = "Token";
    private static final Pattern AUTH_PATTERN = Pattern.compile(
        '^' + HEADER_AUTH_PREFIX + "[\\s]+((?:[A-Za-z0-9+/]{4})*(?:[A-Za-z0-9+/]{2}==|[A-Za-z0-9+/]{3}=)?)\\s*$");

    @Override
    public void configureRoutes(Vertx vertx, Router router, JsonObject config) {

        EventBus eBus = vertx.eventBus();
        final long mbusTimeout = config.getLong(Constants.EventBus.MBUS_TIMEOUT, 30L);

        router.route(Constants.Route.API_AUTH_ROUTE).handler(routingContext -> {
            long authProcessingStart = System.currentTimeMillis();
            String sessionToken = extractSessionToken(routingContext.request().getHeader(HttpConstants.HEADER_AUTH));
            // If the session token is null or absent, we send an error to client
            if (sessionToken == null || sessionToken.isEmpty()) {
                routingContext.response().setStatusCode(HttpConstants.HttpStatus.UNAUTHORIZED.getCode())
                    .setStatusMessage(HttpConstants.HttpStatus.UNAUTHORIZED.getMessage()).end();
            } else {
                // If the session token is present, we send it to Message Bus for validation. We stash it on to
                // routing context for good measure. We could have done that later in success callback but we want to
                // avoid closure from callback for success to this local context, hence it is here
                routingContext.put(Constants.Message.MSG_SESSION_TOKEN, sessionToken);
                DeliveryOptions options = new DeliveryOptions().setSendTimeout(mbusTimeout * 1000)
                    .addHeader(Constants.Message.MSG_OP, Constants.Message.MSG_OP_AUTH)
                    .addHeader(Constants.Message.MSG_SESSION_TOKEN, sessionToken);
                eBus.<JsonObject>send(Constants.EventBus.MBEP_AUTH, null, options, reply -> {
                    if (reply.succeeded()) {
                        AuthSessionResponseHolder responseHolder =
                            AuthSessionResponseHolderBuilder.build(reply.result());
                        // Message header would indicate whether the auth was successful or not. In addition,
                        // successful auth may have been for anonymous user. We allow only GET request for anonymous
                        // user (since we do not support head, trace, options etc so far)
                        if (responseHolder.isAuthorized()) {
                            JsonObject session = responseHolder.getSession();
                            routingContext.put(Constants.Message.MSG_KEY_SESSION, session);
                            routingContext.put(Constants.Message.MSG_USER_ID, responseHolder.getUser());
                            routingContext.put(Constants.Message.PROCESSING_AUTH_TIME,
                                (System.currentTimeMillis() - authProcessingStart));
                            routingContext
                                .put(Constants.Message.PROCESSING_HANDLER_START_TIME, System.currentTimeMillis());
                            routingContext.next();
                        } else {
                            routingContext.response().setStatusCode(HttpConstants.HttpStatus.UNAUTHORIZED.getCode())
                                .setStatusMessage(HttpConstants.HttpStatus.UNAUTHORIZED.getMessage()).end();
                        }
                    } else {
                        LOG.error("Not able to send message", reply.cause());
                        routingContext.response().setStatusCode(HttpConstants.HttpStatus.ERROR.getCode()).end();
                    }
                });
            }
        });

    }

    private static String extractSessionToken(String authHeader) {
        if (authHeader == null || authHeader.isEmpty()) {
            LOG.debug("Session token is null or empty");
            return null;
        }
        Matcher authMatcher = AUTH_PATTERN.matcher(authHeader);
        if (authMatcher.matches()) {
            return authMatcher.group(1);
        }
        LOG.debug("Incorrect format of session token '{}'", authHeader);
        return null;
    }

}
