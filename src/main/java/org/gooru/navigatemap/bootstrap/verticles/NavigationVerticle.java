package org.gooru.navigatemap.bootstrap.verticles;

import java.util.Objects;

import org.gooru.navigatemap.app.constants.Constants;
import org.gooru.navigatemap.app.exceptions.HttpResponseWrapperException;
import org.gooru.navigatemap.app.exceptions.MessageResponseWrapperException;
import org.gooru.navigatemap.infra.data.context.ContextAttributes;
import org.gooru.navigatemap.infra.data.context.ContextProcessor;
import org.gooru.navigatemap.infra.data.context.ContextUtil;
import org.gooru.navigatemap.processor.next.contentserver.ContentServer;
import org.gooru.navigatemap.processor.next.contentserver.RemoteAssessmentCollectionFetcher;
import org.gooru.navigatemap.processor.next.contentserver.RemoteUriLocator;
import org.gooru.navigatemap.processor.next.contentserver.ResponseParserForNextApi;
import org.gooru.navigatemap.processor.next.pathfinder.PathFinderProcessor;
import org.gooru.navigatemap.responses.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.eventbus.DeliveryOptions;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.eventbus.Message;
import io.vertx.core.http.HttpClient;
import io.vertx.core.http.HttpClientOptions;
import io.vertx.core.json.JsonObject;

/**
 * @author ashish on 26/2/17.
 */
public class NavigationVerticle extends AbstractVerticle {
    private static final Logger LOGGER = LoggerFactory.getLogger(NavigationVerticle.class);
    private HttpClient client;
    private RemoteUriLocator remoteUriLocator;

    @Override
    public void start(Future<Void> startFuture) {

        EventBus eb = vertx.eventBus();
        eb.localConsumer(Constants.EventBus.MBEP_NAVIGATE, this::processMessage).completionHandler(result -> {
            if (result.succeeded()) {
                LOGGER.info("Navigation end point ready to listen");
                startFuture.complete();
            } else {
                LOGGER.error("Error registering the Navigation handler. Halting the machinery");
                startFuture.fail(result.cause());
                Runtime.getRuntime().halt(1);
            }
        });

        initializeHttpClient();
    }

    private void initializeHttpClient() {

        final Integer timeout = config().getInteger("http.timeout");
        final Integer poolSize = config().getInteger("http.poolSize");
        Objects.requireNonNull(timeout);
        Objects.requireNonNull(poolSize);
        String assessmentUri = config().getString("assessment.fetch.uri");
        Objects.requireNonNull(assessmentUri);
        String collectionUri = config().getString("collection.fetch.uri");
        Objects.requireNonNull(collectionUri);
        String resourceUri = config().getString("resource.fetch.uri");
        Objects.requireNonNull(resourceUri);
        String assessmentExternalUri = config().getString("assessment-external.fetch.uri");
        Objects.requireNonNull(assessmentExternalUri);
        remoteUriLocator = new RemoteUriLocator(assessmentUri, collectionUri, assessmentExternalUri);

        client = vertx.createHttpClient(new HttpClientOptions().setConnectTimeout(timeout).setMaxPoolSize(poolSize));
    }

    private void processMessage(Message<JsonObject> message) {
        String op = message.headers().get(Constants.Message.MSG_OP);
        switch (op) {
        case Constants.Message.MSG_OP_NEXT:
            processNextCommand(message);
            break;
        default:
            LOGGER.warn("Invalid operation type");
            ResponseUtil.processFailure(message);
        }
    }

    private void processNextCommand(Message<JsonObject> message) {
        Future<JsonObject> future = Future.future();
        new ContextProcessor(vertx).fetchContext(message)
            .compose(navigateProcessorContext -> new PathFinderProcessor(vertx).findNext(navigateProcessorContext))
            .compose(
                ar -> new ContentServer(vertx, future, new RemoteAssessmentCollectionFetcher(client, remoteUriLocator))
                    .serveContent(ar), future);

        future.setHandler(event -> {
            if (event.succeeded()) {
                message.reply(event.result());
                ResponseParserForNextApi responseParserForNextApi = ResponseParserForNextApi.build(event.result());

                String user = message.body().getString(Constants.Message.MSG_USER_ID);
                persistNewContext(responseParserForNextApi, user);
                persistSuggestion(responseParserForNextApi);
            } else {
                LOGGER.warn("Failed to process next command", event.cause());
                if (event.cause() instanceof HttpResponseWrapperException) {
                    HttpResponseWrapperException exception = (HttpResponseWrapperException) event.cause();
                    message.reply(new JsonObject().put(Constants.Message.MSG_HTTP_STATUS, exception.getStatus())
                        .put(Constants.Message.MSG_HTTP_BODY, exception.getBody())
                        .put(Constants.Message.MSG_HTTP_HEADERS, new JsonObject()));
                } else if (event.cause() instanceof MessageResponseWrapperException) {
                    MessageResponseWrapperException exception = (MessageResponseWrapperException) event.cause();
                    message.reply(exception.getMessageResponse().reply(),
                        exception.getMessageResponse().deliveryOptions());
                } else {
                    message.reply(new JsonObject().put(Constants.Message.MSG_HTTP_STATUS, 500)
                        .put(Constants.Message.MSG_HTTP_BODY, new JsonObject())
                        .put(Constants.Message.MSG_HTTP_HEADERS, new JsonObject()));
                }
            }
        });
    }

    private void persistSuggestion(ResponseParserForNextApi responseParserForNextApi) {
        if (responseParserForNextApi.getSuggestions().isEmpty()) {
            return;
        }
        vertx.eventBus().send(Constants.EventBus.MBEP_POST_PROCESS, responseParserForNextApi.getResponse());
    }

    private void persistNewContext(ResponseParserForNextApi responseParserForNextApi, String user) {
        JsonObject newContext = responseParserForNextApi.getContext();
        if (newContext != null) {
            String contextKey = ContextUtil
                .createUserContextKey(user, newContext.getString(ContextAttributes.COURSE_ID),
                    newContext.getString(ContextAttributes.CLASS_ID));
            vertx.eventBus().send(Constants.EventBus.MBEP_USER_CONTEXT, newContext,
                new DeliveryOptions().addHeader(Constants.Message.MSG_OP, Constants.Message.MSG_OP_CONTEXT_SET)
                    .addHeader(Constants.Message.MSG_HDR_KEY_CONTEXT, contextKey));
        }
    }

    @Override
    public void stop(Future<Void> stopFuture) {
    }
}
