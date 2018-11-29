package org.gooru.navigatemap.processor.next.contentserver;

import io.vertx.core.Future;
import io.vertx.core.http.HttpClient;
import io.vertx.core.http.HttpClientRequest;
import io.vertx.core.json.JsonObject;
import org.gooru.navigatemap.app.constants.HttpConstants;
import org.gooru.navigatemap.infra.data.ResponseContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author ashish on 6/3/17.
 */
public final class RemoteAssessmentCollectionFetcher {

  private static final Logger LOGGER = LoggerFactory
      .getLogger(RemoteAssessmentCollectionFetcher.class);
  private static final String HEADER_AUTH_PREFIX = "Token ";

  private final HttpClient client;
  private final Future<JsonObject> completionFuture;
  private final RemoteUriLocator remoteUriLocator;

  public RemoteAssessmentCollectionFetcher(HttpClient client, RemoteUriLocator remoteUriLocator) {
    this.client = client;
    this.completionFuture = Future.future();
    this.remoteUriLocator = remoteUriLocator;
  }

  Future<JsonObject> fetch(ResponseContext context, String auth) {
    String uri = remoteUriLocator.getUriForItemType(context.getCurrentItemType());
    if (uri != null) {
      uri += context.getCurrentItemId().toString();
      fetchFromRemote(context, uri, auth);
    } else {
      LOGGER.warn("Unsupported CollectionType: '{}'", context.getCurrentItemType());
    }
    return completionFuture;
  }

  private void fetchFromRemote(ResponseContext context, String uri, String auth) {
    LOGGER.debug("Need to fetch data from remote server");
    HttpClientRequest request = client.getAbs(uri, response -> response.bodyHandler(buffer -> {
      JsonObject result;
      String fromBuffer = buffer.toString();
      JsonObject body =
          (fromBuffer == null || fromBuffer.isEmpty()) ? new JsonObject()
              : new JsonObject(fromBuffer);
      if (response.statusCode() != HttpConstants.HttpStatus.SUCCESS.getCode()) {
        LOGGER.warn("Remote fetch failed, status code: '{}'", response.statusCode());
        result = ResponseBuilder.createExceptionResponseBuilder(response.statusCode(), body)
            .buildResponse();
      } else {
        LOGGER.debug("Communication with remote successful");
        result = ResponseBuilder.createSuccessResponseBuilder(context, body).buildResponse();
      }
      LOGGER.debug("Response from remote <{}>", result);
      completionFuture.complete(result);
    })).exceptionHandler(ex -> {
      LOGGER.warn("Error while communicating with remote server: ", ex);
      completionFuture.complete(ResponseBuilder
          .createExceptionResponseBuilder(HttpConstants.HttpStatus.UPSTREAM_ERROR.getCode(),
              new JsonObject())
          .buildResponse());
    });

    request.putHeader(HttpConstants.HEADER_AUTH, HEADER_AUTH_PREFIX + auth)
        .putHeader(HttpConstants.HEADER_CONTENT_TYPE, HttpConstants.CONTENT_TYPE_JSON).end();

  }
}
