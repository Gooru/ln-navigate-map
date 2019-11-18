package org.gooru.navigatemap.app.constants;

/**
 * @author ashish on 24/2/17.
 */
public final class Constants {

  public static final class EventBus {

    public static final String MBEP_AUTH = "org.gooru.navigate-map.eventbus.auth";
    public static final String MBEP_NAVIGATE = "org.gooru.navigate-map.eventbus.navigate";
    public static final String MBEP_USER_CONTEXT = "org.gooru.navigate-map.eventbus.user.context";
    public static final String MBEP_POST_PROCESS = "org.gooru.navigate-map.eventbus.post.process";
    public static final String MBEP_CONTENT = "org.gooru.navigate-map.eventbus.content";

    public static final String MBUS_TIMEOUT = "event.bus.send.timeout.seconds";

    private EventBus() {
      throw new AssertionError();
    }
  }

  public static final class Message {

    public static final String MSG_OP = "mb.operation";
    public static final String MSG_API_VERSION = "api.version";
    public static final String MSG_SESSION_TOKEN = "session.token";
    public static final String MSG_OP_AUTH = "auth";
    public static final String MSG_KEY_SESSION = "session";
    public static final String MSG_OP_STATUS = "mb.op.status";
    public static final String MSG_OP_STATUS_SUCCESS = "mb.op.status.success";
    public static final String MSG_OP_STATUS_FAIL = "mb.op.status.fail";
    public static final String MSG_HDR_KEY_CONTEXT = "user.context";
    public static final String MSG_USER_ANONYMOUS = "anonymous";
    public static final String MSG_USER_ID = "user_id";
    public static final String MSG_HTTP_STATUS = "http.status";
    public static final String MSG_HTTP_BODY = "http.body";
    public static final String MSG_HTTP_HEADERS = "http.headers";
    public static final String MSG_USER_PATH_MAP = "user_path_map";
    public static final String MSG_PATH_ID = "path_id";

    public static final String MSG_OP_NEXT = "navigate.next";
    public static final String MSG_OP_CONTEXT_GET = "context.get";
    public static final String MSG_OP_CONTEXT_SET = "context.set";
    public static final String MSG_OP_POSTPROCESS_NEXT = "postprocess.next";
    public static final String MSG_OP_POSTPROCESS_TEACHER_SUGGESTION_ADD = "postprocess.suggestion.add.teacher";
    public static final String MSG_OP_POSTPROCESS_SYSTEM_SUGGESTION_ADD = "postprocess.suggestion.add.system";
    public static final String MSG_OP_TEACHER_SUGGESTION_ADD = "teacher.suggestion.add";
    public static final String MSG_OP_SYSTEM_SUGGESTION_ADD = "system.suggestion.add";

    public static final String MSG_MESSAGE = "message";
    public static final String PROCESSING_AUTH_TIME = "auth.processing.time";
    public static final String PROCESSING_HANDLER_START_TIME = "handler.start.time";
    public static final String ACCESS_TOKEN_VALIDITY = "access_token_validity";

    private Message() {
      throw new AssertionError();
    }
  }

  public static final class Response {

    public static final String RESP_CONTENT = "content";
    public static final String RESP_SUGGESTIONS = "suggestions";
    public static final String RESP_CONTEXT = "context";

    private Response() {
      throw new AssertionError();
    }
  }

  public static final class Params {

    public static final String PARAM_COURSE_ID = "course_id";
    public static final String PARAM_CLASS_ID = "class_id";

    private Params() {
      throw new AssertionError();
    }
  }

  public static final class Route {

    public static final String API_AUTH_ROUTE = "/api/navigate-map/*";
    private static final String API_BASE_ROUTE = "/api/navigate-map/:version/";
    public static final String API_NAVIGATE_NEXT = API_BASE_ROUTE + "next";
    public static final String API_NAVIGATE_CONTEXT = API_BASE_ROUTE + "context";
    public static final String API_TEACHER_SUGGESTIONS_ADD = API_BASE_ROUTE + "teacher/suggestions";
    public static final String API_SYSTEM_SUGGESTIONS_ADD = API_BASE_ROUTE + "system/suggestions";
    public static final String API_INTERNAL_BANNER = "/api/internal/banner";
    public static final String API_INTERNAL_METRICS = "/api/internal/metrics";

    private Route() {
      throw new AssertionError();
    }
  }

  private Constants() {
    throw new AssertionError();
  }
}
