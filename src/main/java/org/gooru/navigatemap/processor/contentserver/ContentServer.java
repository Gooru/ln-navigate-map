package org.gooru.navigatemap.processor.contentserver;

import java.util.Objects;

import org.gooru.navigatemap.app.components.AppConfiguration;
import org.gooru.navigatemap.app.constants.Constants;
import org.gooru.navigatemap.processor.data.CurrentItemType;
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
        LOGGER.debug("Starting ContentServer flow");

        if (npc.getCtxSuggestions().hasSuggestions()) {
            LOGGER.debug("Will serve suggestions");
            completionFuture.complete(serveSuggestions());
        } else if (npc.responseContext().getState() == State.Done) {
            LOGGER.debug("Status done. Won't serve anything");
            JsonObject result = ResponseBuilder
                .createSuccessResponseBuilder(navigateProcessorContext.responseContext(), new JsonObject())
                .buildResponse();
            completionFuture.complete(result);
        } else {
            Objects.requireNonNull(npc.responseContext().getCurrentItemId());
            Objects.requireNonNull(npc.responseContext().getCurrentItemType());

            LOGGER.debug("Will serve content (assessment/collection)");

            if (npc.responseContext().getCurrentItemType() == CurrentItemType.Collection) {
                serveCollection();
            } else if (npc.responseContext().getCurrentItemType() == CurrentItemType.Assessment) {
                serveAssessment();
            } else if (npc.responseContext().getCurrentItemType() == CurrentItemType.AssessmentExternal) {
                serveAssessmentExternal();
            } else {
                // TODO: What to do??
                LOGGER.warn("Invalid content to serve, not sure what to do");
                LOGGER.debug(npc.responseContext().toJson().toString());
                completionFuture.complete(new JsonObject());
            }
        }
    }

    private void serveResources() {
        if (AppConfiguration.getInstance().serveContentDetails()) {
            LOGGER.debug("Will fetch content details from remote servers");

            fetcher.fetch(navigateProcessorContext.responseContext(),
                navigateProcessorContext.navigateMessageContext().getSessionToken())
                .setHandler(ar -> completionFuture.complete(ar.result()));
        } else {
            LOGGER.debug("Will serve content without details");
            JsonObject result = ResponseBuilder.createSuccessResponseBuilder(navigateProcessorContext.responseContext(),
                new JsonObject()
                    .put("id", Objects.toString(navigateProcessorContext.responseContext().getCurrentItemId(), null))
                    .put("type", Objects
                        .toString(navigateProcessorContext.responseContext().getCurrentItemType().getName(), null))
                    .put("subtype",
                        Objects.toString(navigateProcessorContext.responseContext().getCurrentItemSubtype(), null)))
                .buildResponse();
            completionFuture.complete(result);
        }
    }

    private void serveAssessment() {
        serveAssessmentCollection();
    }

    private void serveAssessmentExternal() {
        serveAssessmentCollection();
    }

    private void serveCollection() {
        serveAssessmentCollection();
    }

    private void serveAssessmentCollection() {
        if (AppConfiguration.getInstance().serveContentDetails()) {
            LOGGER.debug("Will fetch content details from remote servers");

            fetcher.fetch(navigateProcessorContext.responseContext(),
                navigateProcessorContext.navigateMessageContext().getSessionToken())
                .setHandler(ar -> completionFuture.complete(ar.result()));
        } else {
            LOGGER.debug("Will serve content without details");
            JsonObject result = ResponseBuilder.createSuccessResponseBuilder(navigateProcessorContext.responseContext(),
                new JsonObject()
                    .put("id", Objects.toString(navigateProcessorContext.responseContext().getCurrentItemId(), null))
                    .put("type", Objects
                        .toString(navigateProcessorContext.responseContext().getCurrentItemType().getName(), null))
                    .put("subtype",
                        Objects.toString(navigateProcessorContext.responseContext().getCurrentItemSubtype(), null)))
                .buildResponse();
            completionFuture.complete(result);
        }
    }

    private JsonObject serveSuggestions() {
        // TODO : Provide complete implementation
        JsonArray suggestions =
            new SuggestionsCardBuilder(navigateProcessorContext.getCtxSuggestions()).createSuggestionCards();
        return new SuccessResponseBuilder(navigateProcessorContext.responseContext(), suggestions).buildResponse();
    }

    private void serveDummyResponse() {
        completionFuture.complete(new JsonObject().put(Constants.Message.MSG_HTTP_STATUS, 200)
            .put(Constants.Message.MSG_HTTP_BODY, new JsonObject().put("status", "successful"))
            .put(Constants.Message.MSG_HTTP_HEADERS, new JsonObject()));
    }
}
