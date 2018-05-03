package org.gooru.navigatemap.infra.utilities.suggestionsapplicability;

import java.util.UUID;

import org.gooru.navigatemap.app.components.AppConfiguration;
import org.gooru.navigatemap.infra.data.NavigateProcessorContext;
import org.gooru.navigatemap.infra.utilities.jdbi.DBICreator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author ashish on 3/5/17.
 */
public final class SuggestionsApplicabilityVerifier {

    private final NavigateProcessorContext npc;
    private static final Logger LOGGER = LoggerFactory.getLogger(SuggestionsApplicabilityVerifier.class);

    public SuggestionsApplicabilityVerifier(NavigateProcessorContext npc) {
        this.npc = npc;
    }

    public boolean areSuggestionApplicable() {
        if (npc.navigateMessageContext().isUserAnonymous() || !AppConfiguration.getInstance().suggestionsTurnedOn()) {
            return false;
        }
        return decideSuggestionsApplicability();
    }

    public boolean arePreLessonSuggestionsOff() {
        return AppConfiguration.getInstance().suggestionsPreLessonOff();
    }

    public boolean arePostLessonSuggestionsOff() {
        return AppConfiguration.getInstance().suggestionsPostLessonOff();
    }

    private boolean decideSuggestionsApplicability() {
        // We need to know what kind of applicability is to be used and apply it here
        return decideSuggestionsApplicabilityBasedOnCourseVersion();
    }

    private boolean decideSuggestionsApplicabilityBasedOnCourseVersion() {
        UUID courseId = npc.requestContext().getCourseId();
        String version = new CourseVersionService(DBICreator.getDbiForDefaultDS()).findCourseVersion(courseId);
        return (version != null);
    }
}
