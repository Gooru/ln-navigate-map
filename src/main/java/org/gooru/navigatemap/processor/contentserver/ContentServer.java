package org.gooru.navigatemap.processor.contentserver;

import java.util.Objects;

import org.gooru.navigatemap.constants.Constants;
import org.gooru.navigatemap.processor.coursepath.repositories.ContentRepositoryBuilder;
import org.gooru.navigatemap.processor.data.CollectionType;
import org.gooru.navigatemap.processor.data.NavigateProcessorContext;
import org.gooru.navigatemap.processor.data.State;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

/**
 * @author ashish on 26/2/17.
 */
public class ContentServer {
    private final Vertx vertx;
    private final RemoteAssessmentCollectionFetcher fetcher;
    private final Future<JsonObject> completionFuture;
    private NavigateProcessorContext navigateProcessorContext;

    private static final Logger LOGGER = LoggerFactory.getLogger(ContentServer.class);

    public ContentServer(Vertx vertx, Future<JsonObject> future, RemoteAssessmentCollectionFetcher fetcher) {
        this.vertx = vertx;
        this.completionFuture = future;
        this.fetcher = fetcher;
    }

    public void serveContent(NavigateProcessorContext npc) {

        this.navigateProcessorContext = npc;

        if (npc.getCtxSuggestions().hasSuggestions()) {
            completionFuture.complete(serveSuggestions());
        } else if (npc.responseContext().getState() == State.Done) {
            completionFuture.complete(new JsonObject());
        } else {
            Objects.requireNonNull(npc.responseContext().getCurrentItemId());
            Objects.requireNonNull(npc.responseContext().getCurrentItemType());

            if (npc.responseContext().getCurrentItemType() == CollectionType.Collection) {
                serveCollection();
            } else if (npc.responseContext().getCurrentItemType() == CollectionType.Assessment) {
                serveAssessment();
            } else {
                // TODO: What to do??
                LOGGER.warn("Invalid content to serve, not sure what to do");
                LOGGER.debug(npc.responseContext().toJson().toString());
                completionFuture.complete(new JsonObject());
            }
        }
    }

    private void serveAssessment() {
        fetcher.fetch(navigateProcessorContext.responseContext(),
            navigateProcessorContext.navigateMessageContext().getSessionToken())
            .setHandler(ar -> completionFuture.complete(ar.result()));
    }

    private void serveCollection() {
        fetcher.fetch(navigateProcessorContext.responseContext(),
            navigateProcessorContext.navigateMessageContext().getSessionToken())
            .setHandler(ar -> completionFuture.complete(ar.result()));
    }

    private JsonObject serveSuggestions() {
        JsonArray suggestions = new SuggestionsCardBuilder(navigateProcessorContext.getCtxSuggestions(),
            ContentRepositoryBuilder.buildContentSuggestionsService()).createSuggestionCards();
        return new SuccessResponseBuilder(navigateProcessorContext.responseContext(), suggestions).buildResponse();
    }

    private void serveDummyResponse() {
        completionFuture.complete(new JsonObject().put(Constants.Message.MSG_HTTP_STATUS, 200)
            .put(Constants.Message.MSG_HTTP_BODY, new JsonObject().put("status", "successful"))
            .put(Constants.Message.MSG_HTTP_HEADERS, new JsonObject()));
    }
}
