package org.gooru.navigatemap.processor.systemsuggestions;

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
 * @author ashish on 26/12/17.
 */
public class AddSystemSuggestionProcessor implements AsyncMessageProcessor {
    private final Message<JsonObject> message;
    private final Vertx vertx;
    private final Future<MessageResponse> result;
    private EventBusMessage eventBusMessage;
    private static final Logger LOGGER = LoggerFactory.getLogger(AddSystemSuggestionProcessor.class);
    private final AddSystemSuggestionService addSystemSuggestionService =
        new AddSystemSuggestionService(DBICreator.getDbiForDefaultDS());

    public AddSystemSuggestionProcessor(Vertx vertx, Message<JsonObject> message) {
        this.vertx = vertx;
        this.message = message;
        this.result = Future.future();
    }

    @Override
    public Future<MessageResponse> process() {
        try {
            this.eventBusMessage = EventBusMessage.eventBusMessageBuilder(message);
            AddSystemSuggestionCommand command = AddSystemSuggestionCommand.builder(eventBusMessage.getRequestBody());
            addSystemSuggestion(command);
        } catch (Throwable throwable) {
            LOGGER.warn("Encountered exception", throwable);
            result.fail(throwable);
        }
        return result;
    }

    private void addSystemSuggestion(AddSystemSuggestionCommand command) {
        vertx.executeBlocking(future -> {
            try {
                Long result = addSystemSuggestionService.addSystemSuggestion(command);
                future.complete(result);
            } catch (Throwable throwable) {
                LOGGER.warn("Encountered exception accepting suggestion", throwable);
                future.fail(throwable);
            }
        }, asyncResult -> {
            if (asyncResult.succeeded()) {
                vertx.eventBus().send(Constants.EventBus.MBEP_POST_PROCESS, eventBusMessage.getRequestBody(),
                    DeliveryOptionsBuilder
                        .createDeliveryOptionsWithMsgOp(Constants.Message.MSG_OP_POSTPROCESS_SYSTEM_SUGGESTION_ADD));
                result.complete(MessageResponseFactory.createCreatedResponse(asyncResult.result().toString()));
            } else {
                result.fail(asyncResult.cause());
            }
        });

    }
}
