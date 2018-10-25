package org.gooru.navigatemap.infra.data;

import java.util.HashMap;
import java.util.Map;

/**
 * @author ashish on 26/2/17.
 */
public enum State {

  Start("start"),
  Continue("continue"),
  ContentServed("content-served"),
  ContentEndSuggested("content-end-suggested"),
  Done("done");

  State(String name) {
    this.name = name;
  }

  private final String name;

  private static final Map<String, State> LOOKUP = new HashMap<>(values().length);

  static {
    for (State state : values()) {
      LOOKUP.put(state.name, state);
    }
  }

  public String getName() {
    return name;
  }

  public static State builder(String state) {
    State result = LOOKUP.get(state);
    if (result == null) {
      throw new IllegalArgumentException("Invalid state: " + state);
    }
    return result;
  }
}
