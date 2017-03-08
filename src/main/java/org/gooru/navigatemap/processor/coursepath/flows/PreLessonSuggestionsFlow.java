package org.gooru.navigatemap.processor.coursepath.flows;

import org.gooru.navigatemap.app.components.AppConfiguration;
import org.gooru.navigatemap.processor.data.NavigateProcessorContext;
import org.gooru.navigatemap.responses.ExecutionResult;

/**
 * @author ashish on 6/3/17.
 */
final class PreLessonSuggestionsFlow implements Flow<NavigateProcessorContext> {

    private NavigateProcessorContext npc;

    @Override
    public ExecutionResult<NavigateProcessorContext> apply(ExecutionResult<NavigateProcessorContext> input) {

        npc = input.result();
        if (input.isCompleted() || !AppConfiguration.getInstance().suggestionsTurnedOn() || npc.navigateMessageContext()
            .isUserAnonymous()) {
            return input;
        }

        throw new AssertionError();
    }
}