package org.gooru.navigatemap.processor.postprocessor;

import org.gooru.navigatemap.app.constants.Constants;
import org.skife.jdbi.v2.DBI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.vertx.core.json.JsonObject;

/**
 * Dispatcher class to dispatch the request to respective handlers based on op provided.
 *
 * @author ashish on 23/7/18.
 */
class PostProcessorServiceImpl implements PostProcessorService {

    private final DBI dbi;
    private static final Logger LOGGER = LoggerFactory.getLogger(PostProcessorService.class);

    PostProcessorServiceImpl(DBI dbi) {
        this.dbi = dbi;
    }

    @Override
    public void process(String op, JsonObject requestData) {
        switch (op) {
        case Constants.Message.MSG_OP_POSTPROCESS_NEXT:
            PostProcessorHandler.buildForNextCommand(dbi).handle(requestData);
            break;
        case Constants.Message.MSG_OP_POSTPROCESS_SYSTEM_SUGGESTION_ADD:
            PostProcessorHandler.buildForSystemSuggestionsAddCommand(dbi).handle(requestData);
            break;
        case Constants.Message.MSG_OP_POSTPROCESS_TEACHER_SUGGESTION_ADD:
            PostProcessorHandler.buildForTeacherSuggestionsAddCommand(dbi).handle(requestData);
            break;
        default:
            LOGGER.warn("Invalid op: '{}'", op);
        }
    }

}
