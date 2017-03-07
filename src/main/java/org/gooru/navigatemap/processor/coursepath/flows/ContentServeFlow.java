package org.gooru.navigatemap.processor.coursepath.flows;

import org.gooru.navigatemap.processor.data.NavigateProcessorContext;
import org.gooru.navigatemap.responses.ExecutionResult;

/**
 * @author ashish on 6/3/17.
 */
final class ContentServeFlow implements Flow<NavigateProcessorContext> {
    @Override
    public ExecutionResult<NavigateProcessorContext> apply(ExecutionResult<NavigateProcessorContext> input) {

        if (input.isCompleted()) {
            return input;
        }

        throw new AssertionError();
    }
}
