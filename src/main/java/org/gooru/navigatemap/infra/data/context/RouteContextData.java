package org.gooru.navigatemap.infra.data.context;

import io.vertx.core.json.DecodeException;
import io.vertx.core.json.JsonObject;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * This class encapsulates the data object in context for Next API. The context data key would be
 * used for state which is pass through for caller but is needed on the backend. The data state will
 * evolve and this class will provide a type safe method to dereference the object. In a nutshell
 * encoded string will denote a JSON object which is converted to string and then encoded using
 * base64.
 *
 * @author ashish on 18/7/18.
 */
public class RouteContextData {

  private final JsonObject inputJson;

  public RouteContextData(String input) {
    if (input != null) {
      try {
        byte[] asBytes = Base64.getDecoder().decode(input);
        String base64Decoded = new String(asBytes, StandardCharsets.UTF_8);
        inputJson = new JsonObject(base64Decoded);
      } catch (DecodeException | IllegalArgumentException e) {
        throw new IllegalArgumentException("Invalid contextData", e);
      }
    } else {
      inputJson = new JsonObject();
    }
  }

  public boolean isRepeatAssessmentPostSignatureCollectionOn() {
    Boolean result = inputJson.getBoolean(ContextDataAttributes.REPEAT_POST_SIGNATURE_COLLECTION);
    return result == Boolean.TRUE;
  }

  public void turnOnRepeatAssessmentPostSignatureCollection() {
    inputJson.put(ContextDataAttributes.REPEAT_POST_SIGNATURE_COLLECTION, true);
  }

  public void turnOffRepeatAssessmentPostSignatureCollection() {
    inputJson.remove(ContextDataAttributes.REPEAT_POST_SIGNATURE_COLLECTION);
  }

  public String encode() {
    byte[] bytes = inputJson.toString().getBytes(StandardCharsets.UTF_8);
    return Base64.getEncoder().encodeToString(bytes);
  }

  private static class ContextDataAttributes {

    private ContextDataAttributes() {
      throw new AssertionError();
    }

    static final String REPEAT_POST_SIGNATURE_COLLECTION = "rpsc";
  }
}
