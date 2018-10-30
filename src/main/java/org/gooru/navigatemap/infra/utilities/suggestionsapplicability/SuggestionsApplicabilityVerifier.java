package org.gooru.navigatemap.infra.utilities.suggestionsapplicability;

import org.gooru.navigatemap.app.components.AppConfiguration;
import org.gooru.navigatemap.infra.data.NavigateProcessorContext;
import org.gooru.navigatemap.infra.utilities.jdbi.DBICreator;

/**
 * This class verifies that
 * <li>if the user is anonymous OR</li>
 * <li>if the suggestions are turned off at config level</li>
 * Then advise not to apply suggestion. If both of above conditions are false, then look up in DB to
 * find out course version for course in question.
 *
 * @author ashish on 3/5/17.
 */
public final class SuggestionsApplicabilityVerifier {

  private final NavigateProcessorContext npc;

  public SuggestionsApplicabilityVerifier(NavigateProcessorContext npc) {
    this.npc = npc;
  }

  public boolean areSuggestionApplicable() {
    if (npc.navigateMessageContext().isUserAnonymous() || !AppConfiguration.getInstance()
        .suggestionsTurnedOn()) {
      return false;
    }
    return decideSuggestionsApplicability();
  }

  private boolean decideSuggestionsApplicability() {
    return new SuggestionsApplicabilityService(DBICreator.getDbiForDefaultDS())
        .areSuggestionsApplicable(npc.requestContext().getClassId(),
            npc.requestContext().getCourseId());
  }
}
