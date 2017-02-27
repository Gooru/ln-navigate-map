package org.gooru.navigatemap.routes.utils;

import org.gooru.navigatemap.constants.Constants;

import io.vertx.core.eventbus.DeliveryOptions;
import io.vertx.ext.web.RoutingContext;

/**
 * @author ashish on 24/2/17.
 */
public final class DeliveryOptionsBuilder {
    private DeliveryOptionsBuilder() {
        throw new AssertionError();
    }

    public static DeliveryOptions buildWithApiVersion(RoutingContext context) {
        final String apiVersion = context.request().getParam("version");
        VersionValidatorUtility.validateVersion(apiVersion);
        return new DeliveryOptions().addHeader(Constants.Message.MSG_API_VERSION, apiVersion);
    }

    public static DeliveryOptions createDeliveryOptionsForFetchUserContext(long timeout, String contextKey) {
        return new DeliveryOptions().setSendTimeout(timeout)
            .addHeader(Constants.Message.MSG_OP, Constants.Message.MSG_OP_CONTEXT_GET)
            .addHeader(Constants.Message.MSG_HDR_KEY_CONTEXT, contextKey);
    }
}
