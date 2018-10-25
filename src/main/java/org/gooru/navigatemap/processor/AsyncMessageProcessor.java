package org.gooru.navigatemap.processor;

import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonObject;
import org.gooru.navigatemap.processor.systemsuggestions.AddSystemSuggestionProcessor;
import org.gooru.navigatemap.processor.teachersuggestions.AddTeacherSuggestionsProcessor;
import org.gooru.navigatemap.responses.MessageResponse;

/**
 * @author ashish on 17/11/17.
 */
public interface AsyncMessageProcessor {

  Future<MessageResponse> process();

  static AsyncMessageProcessor buildAddTeacherSuggestionsProcessor(Vertx vertx,
      Message<JsonObject> message) {
    return new AddTeacherSuggestionsProcessor(vertx, message);
  }

  static AsyncMessageProcessor buildAddSystemSuggestionsProcessor(Vertx vertx,
      Message<JsonObject> message) {
    return new AddSystemSuggestionProcessor(vertx, message);
  }

  static AsyncMessageProcessor buildPlaceHolderSuccessProcessor(Vertx vertx,
      Message<JsonObject> message) {
    return () -> {
      Future<MessageResponse> future = Future.future();
      future.complete(MessageResponse.Builder.buildPlaceHolderResponse());
      return future;
    };
  }

  static AsyncMessageProcessor buildPlaceHolderExceptionProcessor(Vertx vertx,
      Message<JsonObject> message) {
    return () -> {
      Future<MessageResponse> future = Future.future();
      future.fail(new IllegalStateException("Illegal State for processing command"));
      return future;
    };
  }

}
