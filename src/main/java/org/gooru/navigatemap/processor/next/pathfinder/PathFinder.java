package org.gooru.navigatemap.processor.next.pathfinder;

import org.gooru.navigatemap.infra.data.ContentAddress;
import org.gooru.navigatemap.infra.data.NavigateProcessorContext;
import org.gooru.navigatemap.infra.data.State;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.vertx.core.Future;
import io.vertx.core.Vertx;

/**
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
            ContentAddress targetAddress = PathFinderFactory.buildStraightPathFinderService()
                .findNextContentFromCUL(npc.getCurrentContentAddress(), npc.requestContext().getClassId());
            if (targetAddress != null) {
                if (targetAddress.getCollection() != null) {
                    npc.serveContent(targetAddress);
                } else {
                    npc.responseContext().setState(State.Done);
                }
            } else {
                LOGGER.warn("Not able to locate valid content in course");
                npc.responseContext().setState(State.Done);
            }
        } else {
            // TODO: Provide implementation
            // Delegate it to Suggestion oriented path finder
            npc.responseContext().setState(State.Done);

        }
        resultFuture.complete(npc);
        return resultFuture;
    }

}
