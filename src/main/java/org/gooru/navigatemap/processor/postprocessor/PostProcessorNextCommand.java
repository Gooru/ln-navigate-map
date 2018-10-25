package org.gooru.navigatemap.processor.postprocessor;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import org.gooru.navigatemap.app.constants.Constants;
import org.gooru.navigatemap.infra.data.RequestContext;
import org.gooru.navigatemap.infra.data.SuggestionCard;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author ashish on 23/7/18.
 */
class PostProcessorNextCommand {

  private final UUID userId;
  private final List<SuggestionCard> suggestions;
  private final RequestContext context;
  private static final Logger LOGGER = LoggerFactory.getLogger(PostProcessorNextCommand.class);

  private PostProcessorNextCommand(RequestContext requestContext,
      List<SuggestionCard> suggestionCards, UUID userId) {
    context = requestContext;
    suggestions = suggestionCards;
    this.userId = userId;
  }

  static PostProcessorNextCommand buildFromJson(JsonObject request) {
    JsonObject ctx = request.getJsonObject(Constants.Response.RESP_CONTEXT);
    JsonArray suggestions = request.getJsonArray(Constants.Response.RESP_SUGGESTIONS);
    String userString = request.getString(Constants.Message.MSG_USER_ID);

    UUID userId = UUID.fromString(userString);

    RequestContext requestContext = RequestContext.builder(ctx);
    List<SuggestionCard> suggestionCards;

    if (suggestions != null && !suggestions.isEmpty()) {
      ObjectMapper mapper = new ObjectMapper();
      try {
        suggestionCards = mapper
            .readValue(suggestions.toString(), new TypeReference<List<SuggestionCard>>() {
            });
      } catch (IOException e) {
        LOGGER.warn("Not able to serialize suggestions to cards");
        throw new IllegalArgumentException("Invalid JSON for suggestion", e);
      }
    } else {
      suggestionCards = Collections.emptyList();
    }
    return new PostProcessorNextCommand(requestContext, suggestionCards, userId);
  }

  List<SuggestionCard> getSuggestions() {
    return suggestions;
  }

  RequestContext getContext() {
    return context;
  }

  public UUID getUserId() {
    return userId;
  }
}
