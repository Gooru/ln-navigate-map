package org.gooru.navigatemap.processor.teachersuggestions;

import org.gooru.navigatemap.app.constants.Constants;
import org.gooru.navigatemap.infra.data.EventBusMessage;
import org.gooru.navigatemap.infra.utilities.jdbi.DBICreator;
import org.gooru.navigatemap.processor.AsyncMessageProcessor;
import org.gooru.navigatemap.responses.MessageResponse;
import org.gooru.navigatemap.responses.MessageResponseFactory;
import org.gooru.navigatemap.routes.utils.DeliveryOptionsBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonObject;

/**
 * @author ashish on 17/11/17.
 */
public class AddTeacherSuggestionsProcessor implements AsyncMessageProcessor {

    private final Message<JsonObject> message;
    private final Vertx vertx;
    private final Future<MessageResponse> result;
    private EventBusMessage eventBusMessage;
    private static final Logger LOGGER = LoggerFactory.getLogger(AddTeacherSuggestionsProcessor.class);
    private final AddTeacherSuggestionsService addTeacherSuggestionsService =
        new AddTeacherSuggestionsService(DBICreator.getDbiForDefaultDS());

    public AddTeacherSuggestionsProcessor(Vertx vertx, Message<JsonObject> message) {
        this.vertx = vertx;
        this.message = message;
        this.result = Future.future();
    }

    @Override
    public Future<MessageResponse> process() {
        try {
            this.eventBusMessage = EventBusMessage.eventBusMessageBuilder(message);
            AddTeacherSuggestionsCommand command =
                AddTeacherSuggestionsCommand.builder(eventBusMessage.getRequestBody());
            addTeacherSuggestion(command);
        } catch (Throwable throwable) {
            LOGGER.warn("Encountered exception", throwable);
            result.fail(throwable);
        }
        return result;
    }

    private void addTeacherSuggestion(AddTeacherSuggestionsCommand command) {
        vertx.executeBlocking(future -> {
            try {
                addTeacherSuggestionsService.addTeacherSuggestion(command);
                future.complete();
            } catch (Throwable throwable) {
                LOGGER.warn("Encountered exception accepting suggestion", throwable);
                future.fail(throwable);
            }
        }, asyncResult -> {
            if (asyncResult.succeeded()) {
                vertx.eventBus().send(Constants.EventBus.MBEP_POST_PROCESS, JsonObject.mapFrom(command),
                    DeliveryOptionsBuilder
                        .createDeliveryOptionsWithMsgOp(Constants.Message.MSG_OP_POSTPROCESS_SYSTEM_SUGGESTION_ADD));
                result.complete(MessageResponseFactory.createOkayResponse(new JsonObject()));
            } else {
                result.fail(asyncResult.cause());
            }
        });

    }
}
