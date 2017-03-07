package org.gooru.navigatemap.processor.coursepath.flows;

import org.gooru.navigatemap.app.components.AppConfiguration;
import org.gooru.navigatemap.processor.data.NavigateProcessorContext;
import org.gooru.navigatemap.responses.ExecutionResult;

/**
 * @author ashish on 6/3/17.
 */
final class PostContentSuggestionsFlow implements Flow<NavigateProcessorContext> {

    @Override
    public ExecutionResult<NavigateProcessorContext> apply(ExecutionResult<NavigateProcessorContext> input) {

        if (input.isCompleted() || !AppConfiguration.getInstance().suggestionsTurnedOn()) {
            return input;
        }

        throw new AssertionError();
    }
}