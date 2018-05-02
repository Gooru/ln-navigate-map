package org.gooru.navigatemap.processor.coursepath;

import org.gooru.navigatemap.processor.data.NavigateProcessorContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.vertx.core.Future;
import io.vertx.core.Vertx;

/**
 * @author ashish on 26/2/17.
 */
public class PathMapper {
    private final Vertx vertx;
    private NavigateProcessorContext navigateProcessorContext;
    private static final Logger LOGGER = LoggerFactory.getLogger(PathMapper.class);

    public PathMapper(Vertx vertx) {
        this.vertx = vertx;
    }

    public Future<NavigateProcessorContext> mapPath(NavigateProcessorContext npc) {
        Future<NavigateProcessorContext> future = Future.future();
        this.navigateProcessorContext = npc;
        vertx.<NavigateProcessorContext>executeBlocking(pathMapperFuture -> {
            try {
                Future<NavigateProcessorContext> resultFuture = startPathMapping();
                resultFuture.setHandler(result -> pathMapperFuture.complete(result.result()));
            } catch (Throwable throwable) {
                LOGGER.warn("Error while mapping path", throwable);
                future.fail(throwable);
            }
        }, pathMapResult -> future.complete(pathMapResult.result()));
        return future;
    }

    private Future<NavigateProcessorContext> startPathMapping() {
        Future<NavigateProcessorContext> resultFuture = Future.future();
        // TODO: Provide implementation

        resultFuture.complete(navigateProcessorContext);
        return resultFuture;
    }
}
