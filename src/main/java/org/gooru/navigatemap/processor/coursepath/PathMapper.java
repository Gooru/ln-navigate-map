package org.gooru.navigatemap.processor.coursepath;

import org.gooru.navigatemap.processor.coursepath.state.StateStimulusMapper;
import org.gooru.navigatemap.processor.coursepath.state.Stimulus;
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
        try {
            this.navigateProcessorContext = npc;
            vertx.executeBlocking(pathMapperFuture -> {
                Future<NavigateProcessorContext> resultFuture = startPathMapping();
                resultFuture.setHandler(result -> pathMapperFuture.complete(result.result()));
            }, pathMapResult -> future.complete());
        } catch (Throwable throwable) {
            LOGGER.warn("Error while mapping path", throwable);
            future.fail(throwable);
        }
        return future;
    }

    private Future<NavigateProcessorContext> startPathMapping() {
        Future<NavigateProcessorContext> resultFuture = Future.future();
        Stimulus<NavigateProcessorContext> result =
            StateStimulusMapper.stimulate(new Stimulus<>(navigateProcessorContext));
        resultFuture.complete(result.getStimulusContent());
        return resultFuture;
    }
}
