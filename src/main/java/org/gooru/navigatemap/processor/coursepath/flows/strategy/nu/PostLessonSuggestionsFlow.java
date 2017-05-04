package org.gooru.navigatemap.processor.coursepath.flows.strategy.nu;

import org.gooru.navigatemap.processor.coursepath.flows.Flow;
import org.gooru.navigatemap.processor.data.NavigateProcessorContext;
import org.gooru.navigatemap.responses.ExecutionResult;

/**
 * @author ashish on 6/3/17.
 */
final class PostLessonSuggestionsFlow implements Flow<NavigateProcessorContext> {

    @Override
    public ExecutionResult<NavigateProcessorContext> apply(ExecutionResult<NavigateProcessorContext> input) {
        // Short circuit, not required for this strategy
        return input;
    }

}
