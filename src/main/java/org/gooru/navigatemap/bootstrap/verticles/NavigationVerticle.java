package org.gooru.navigatemap.bootstrap.verticles;

import java.util.Objects;

import org.gooru.navigatemap.constants.Constants;
import org.gooru.navigatemap.processor.contentserver.ContentServer;
import org.gooru.navigatemap.processor.contentserver.RemoteAssessmentCollectionFetcher;
import org.gooru.navigatemap.processor.context.ContextProcessor;
import org.gooru.navigatemap.processor.coursepath.PathMapper;
import org.gooru.navigatemap.responses.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
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
    private String assessmentUri;
    private String collectionUri;

    @Override
    public void start(Future<Void> startFuture) throws Exception {

        EventBus eb = vertx.eventBus();
        eb.consumer(Constants.EventBus.MBEP_NAVIGATE, this::processMessage);

        initializeHttpClient();
    }

    private void initializeHttpClient() {

        final Integer timeout = config().getInteger("http.timeout");
        final Integer poolSize = config().getInteger("http.poolSize");
        Objects.requireNonNull(timeout);
        Objects.requireNonNull(poolSize);
        assessmentUri = config().getString("assessment.fetch.uri");
        Objects.requireNonNull(assessmentUri);
        collectionUri = config().getString("collection.fetch.uri");
        Objects.requireNonNull(collectionUri);

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
            .compose(navigateProcessorContext -> new PathMapper(vertx).mapPath(navigateProcessorContext))
            .compose(ar -> {
                new ContentServer(vertx, future,
                    new RemoteAssessmentCollectionFetcher(client, assessmentUri, collectionUri)).serveContent(ar);
            }, future);

        future.setHandler(event -> {
            if (event.succeeded()) {
                message.reply(event.result());
            } else {
                // TODO: Implement this
                LOGGER.warn("Failed to process next command", event.cause());
                message.reply(new JsonObject().put(Constants.Message.MSG_HTTP_STATUS, 500)
                    .put(Constants.Message.MSG_HTTP_BODY, new JsonObject())
                    .put(Constants.Message.MSG_HTTP_HEADERS, new JsonObject()));
            }
        });
    }

    @Override
    public void stop(Future<Void> stopFuture) throws Exception {
    }
}
