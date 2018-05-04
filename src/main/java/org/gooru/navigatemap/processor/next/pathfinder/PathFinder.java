package org.gooru.navigatemap.processor.next.pathfinder;

import org.gooru.navigatemap.infra.data.ContentAddress;
import org.gooru.navigatemap.infra.data.NavigateProcessorContext;
import org.gooru.navigatemap.infra.data.State;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.vertx.core.Future;
import io.vertx.core.Vertx;

/**
 * This is the main workhorse to find the content on path. This class triggers different kind of flows e.g. straight
 * path flow, explicit start flow etc by calling and delegating different services. Based on the outcome of service,
 * this class decides what needs to be done with respect to API response by modifying {@link NavigateProcessorContext}
 *
 * @author ashish on 26/2/17.
 */
public class PathFinder {
    private final Vertx vertx;
    private NavigateProcessorContext npc;
    private static final Logger LOGGER = LoggerFactory.getLogger(PathFinder.class);

    public PathFinder(Vertx vertx) {
        this.vertx = vertx;
    }

    public Future<NavigateProcessorContext> findNext(NavigateProcessorContext navigateProcessorContext) {
        Future<NavigateProcessorContext> future = Future.future();
        this.npc = navigateProcessorContext;
        vertx.<NavigateProcessorContext>executeBlocking(pathMapperFuture -> {
            try {
                Future<NavigateProcessorContext> resultFuture = doPathMapping();
                resultFuture.setHandler(result -> pathMapperFuture.complete(result.result()));
            } catch (Throwable throwable) {
                LOGGER.warn("Error while mapping path", throwable);
                future.fail(throwable);
            }
        }, pathMapResult -> future.complete(pathMapResult.result()));
        return future;
    }

    private Future<NavigateProcessorContext> doPathMapping() {
        Future<NavigateProcessorContext> resultFuture = Future.future();
        if (!npc.suggestionsApplicable()) {
            handleNoSuggestionsRoute();
        } else {
            // Delegate it to Suggestion oriented path finder
            handleSuggestionsOrientedRoute();
        }
        resultFuture.complete(npc);
        return resultFuture;
    }

    private void handleSuggestionsOrientedRoute() {
        if (npc.userExplicitlyAskedToStartHere()) {
            ContentAddress targetAddress = PathFinderFactory.buildExplicitStartPathFinderService()
                .explicitlyStartItem(PathFinderContext.buildContext(npc));
            serveTheContent(targetAddress);
        } else {
            // TODO: Provide implementation
            throw new AssertionError();
        }
    }

    private void handleNoSuggestionsRoute() {
        ContentAddress targetAddress = PathFinderFactory.buildStraightPathFinderService()
            .findNextContentFromCourse(PathFinderContext.buildContext(npc));
        serveTheContent(targetAddress);
    }

    private void serveTheContent(ContentAddress contentToServe) {
        if (contentToServe != null) {
            if (contentToServe.getCollection() != null) {
                npc.serveContent(contentToServe);
            } else {
                npc.responseContext().setState(State.Done);
            }
        } else {
            LOGGER.warn("Not able to locate valid content in course");
            npc.responseContext().setState(State.Done);
        }
    }

}
