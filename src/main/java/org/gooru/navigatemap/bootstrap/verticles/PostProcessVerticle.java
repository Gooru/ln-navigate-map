package org.gooru.navigatemap.bootstrap.verticles;

import java.util.Objects;

import org.gooru.navigatemap.app.constants.Constants;
import org.gooru.navigatemap.processor.postprocessor.PostProcessorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.json.JsonObject;

/**
 * @author ashish on 25/2/17.
 */
public class PostProcessVerticle extends AbstractVerticle {

    private static final Logger LOGGER = LoggerFactory.getLogger(PostProcessVerticle.class);

    @Override
    public void start(Future<Void> startFuture) {
        EventBus eb = vertx.eventBus();

        eb.<JsonObject>localConsumer(Constants.EventBus.MBEP_POST_PROCESS, message -> {
            process(message.headers().get(Constants.Message.MSG_OP), message.body());
        }).completionHandler(result -> {
            if (result.succeeded()) {
                LOGGER.info("Post processor end point ready to listen");
                startFuture.complete();
            } else {
                LOGGER.error("Error registering the Post processor handler. Halting the machinery");
                startFuture.fail(result.cause());
                Runtime.getRuntime().halt(1);
            }
        });
    }

    private void process(String op, JsonObject requestData) {
        vertx.<Void>executeBlocking(future -> {
            try {
                PostProcessorService.build().process(op, requestData);
                future.complete();
            } catch (Throwable throwable) {
                LOGGER.warn("Not able to do post processing.", throwable);
                LOGGER.warn(Objects.toString(requestData));
                future.fail(throwable);
            }

        }, asyncResult -> {
            if (asyncResult.succeeded()) {
                LOGGER.info("Done doing the post processing");
            } else {
                LOGGER.warn("Not able to do the post processing", asyncResult.cause());
            }
        });
    }

    @Override
    public void stop(Future<Void> stopFuture) {
        stopFuture.complete();
    }

}
