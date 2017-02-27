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

    public <U> Future<U> mapPath(NavigateProcessorContext npc) {
        Future<U> future = Future.future();
        try {
            this.navigateProcessorContext = npc;
            future.complete();
        } catch (Throwable throwable) {
            LOGGER.warn("Error while mapping path", throwable);
            future.fail(throwable);
        }
        return future;
    }
}
