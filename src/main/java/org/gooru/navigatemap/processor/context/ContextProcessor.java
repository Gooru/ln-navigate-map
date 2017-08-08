package org.gooru.navigatemap.processor.context;

import org.gooru.navigatemap.app.components.AppConfiguration;
import org.gooru.navigatemap.constants.Constants;
import org.gooru.navigatemap.processor.data.NavigateMessageContext;
import org.gooru.navigatemap.processor.data.NavigateProcessorContext;
import org.gooru.navigatemap.processor.data.RequestContext;
import org.gooru.navigatemap.routes.utils.DeliveryOptionsBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonObject;

/**
 * @author ashish on 26/2/17.
 */
public class ContextProcessor {

    private final Vertx vertx;
    private final int timeout;
    private NavigateMessageContext messageContext;
    private RequestContext context;
    private static final Logger LOGGER = LoggerFactory.getLogger(ContextProcessor.class);

    public ContextProcessor(Vertx vertx) {
        this.vertx = vertx;
        this.timeout = AppConfiguration.getInstance().getConfigAsInt("context.mbus.timeout.seconds");
    }

    public Future<NavigateProcessorContext> fetchContext(Message<JsonObject> message) {
        Future<NavigateProcessorContext> resultFuture = Future.future();
        try {
            messageContext = new NavigateMessageContext(message);
            context = RequestContext.builder(messageContext.getRequest());

            if (context.needsLastState() && !messageContext.isUserAnonymous()) {
                vertx.eventBus().<JsonObject>send(Constants.EventBus.MBEP_USER_CONTEXT, new JsonObject(),
                    DeliveryOptionsBuilder.createDeliveryOptionsForFetchUserContext(timeout, getUserContextKey()),
                    asyncResult -> {
                        if (asyncResult.succeeded()) {
                            JsonObject redisResult = asyncResult.result().body();
                            if (redisResult != null && !redisResult.isEmpty()) {
                                RequestContext rc = RequestContext.builder(asyncResult.result().body());
                                resultFuture.complete(new NavigateProcessorContext(rc, messageContext));
                            } else {
                                resultFuture.complete(new NavigateProcessorContext(context, messageContext));
                            }
                        } else {
                            resultFuture.fail(asyncResult.cause());
                        }
                    });
            } else {
                resultFuture.complete(new NavigateProcessorContext(context, messageContext));
            }
        } catch (Throwable e) {
            LOGGER.warn("Error while fetching context", e);
            resultFuture.fail(e);
        }
        return resultFuture;
    }

    private String getUserContextKey() {
        return ContextUtil
            .createUserContextKey(messageContext.getUserId(), context.getCourseId(), context.getClassId());
    }

}
