package org.gooru.navigatemap.bootstrap.verticles;

import java.io.IOException;

import org.gooru.navigatemap.constants.Constants;
import org.gooru.navigatemap.processor.contentserver.ResponseParser;
import org.gooru.navigatemap.processor.data.CurrentItemType;
import org.gooru.navigatemap.processor.data.SuggestionCard;
import org.gooru.navigatemap.processor.postprocessor.repositories.PostProcessorRepository;
import org.gooru.navigatemap.processor.postprocessor.repositories.PostProcessorRespositoryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;

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
    public void start(Future<Void> startFuture) throws Exception {
        EventBus eb = vertx.eventBus();

        eb.<JsonObject>localConsumer(Constants.EventBus.MBEP_POST_PROCESS, message -> {
            ResponseParser requestData = ResponseParser.build(message.body());
            process(requestData);
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

    private void process(ResponseParser requestData) {
        vertx.<Void>executeBlocking(future -> {
            requestData.getSuggestions().forEach(suggestionObj -> {
                if (suggestionObj instanceof JsonObject) {
                    try {
                        SuggestionCard suggestion =
                            new ObjectMapper().readValue(suggestionObj.toString(), SuggestionCard.class);
                        PostProcessorRepository repository = PostProcessorRespositoryBuilder.build();
                        if (CurrentItemType.Resource.getName().equalsIgnoreCase(suggestion.getFormat())) {
                            repository.incrementResourceSuggestedCount(suggestion.getId());
                        }
                        future.complete();
                    } catch (IOException e) {
                        LOGGER.warn("Suggestion Json failed to convert to suggestion card, will skip count update. "
                            + "Value : {}", suggestionObj.toString());
                        future.fail(e);

                    } catch (Throwable t) {
                        LOGGER.warn("Suggestion count update failed", t);
                        future.fail(t);
                    }
                } else {
                    LOGGER.warn("Got non Json object as suggestion: {}", suggestionObj.toString());
                    future.fail(new IllegalArgumentException("Non json object as suggestion"));
                }
            });
        }, asyncResult -> {
            if (asyncResult.succeeded()) {
                LOGGER.info("Done updating the suggested count for resource suggestion");
            } else {
                LOGGER.warn("Not able to update the resource suggestion count");
            }
        });
    }

    @Override
    public void stop(Future<Void> stopFuture) throws Exception {
        stopFuture.complete();
    }

}
