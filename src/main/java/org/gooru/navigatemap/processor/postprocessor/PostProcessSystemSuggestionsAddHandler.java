package org.gooru.navigatemap.processor.postprocessor;

import java.util.Objects;

import org.skife.jdbi.v2.DBI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.vertx.core.json.JsonObject;

/**
 * @author ashish on 24/7/18.
 */
class PostProcessSystemSuggestionsAddHandler implements PostProcessorHandler {
    private final DBI dbi;
    private static final Logger LOGGER = LoggerFactory.getLogger(PostProcessSystemSuggestionsAddHandler.class);

    PostProcessSystemSuggestionsAddHandler(DBI dbi) {
        this.dbi = dbi;
    }

    @Override
    public void handle(JsonObject requestData) {
        // TODO: Provide implementation
        LOGGER.info(Objects.toString(requestData));
    }
}
