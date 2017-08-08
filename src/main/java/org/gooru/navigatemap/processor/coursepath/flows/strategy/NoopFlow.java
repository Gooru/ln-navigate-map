package org.gooru.navigatemap.processor.coursepath.flows.strategy;

import org.gooru.navigatemap.processor.coursepath.flows.Flow;
import org.gooru.navigatemap.processor.data.NavigateProcessorContext;
import org.gooru.navigatemap.responses.ExecutionResult;

/**
 * @author ashish on 5/5/17.
 */
public class NoopFlow implements Flow<NavigateProcessorContext> {
    @Override
    public ExecutionResult<NavigateProcessorContext> apply(ExecutionResult<NavigateProcessorContext> input) {
        return input;
    }
}
