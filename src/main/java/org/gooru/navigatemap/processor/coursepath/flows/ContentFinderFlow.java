package org.gooru.navigatemap.processor.coursepath.flows;

import org.gooru.navigatemap.processor.data.NavigateProcessorContext;
import org.gooru.navigatemap.processor.data.State;
import org.gooru.navigatemap.responses.ExecutionResult;

/**
 * @author ashish on 6/3/17.
 */
final class ContentFinderFlow implements Flow<NavigateProcessorContext> {

    private NavigateProcessorContext npc;

    @Override
    public ExecutionResult<NavigateProcessorContext> apply(ExecutionResult<NavigateProcessorContext> input) {

        if (input.isCompleted()) {
            return input;
        }
        npc = input.result();

        if (npc.requestContext().getState() == State.Continue) {
            // We should have the content here already as this may be start of course
            npc.responseContext().setContentAddress(npc.getNextContentAddress());
            input.setStatus(ExecutionResult.ExecutionStatus.SUCCESSFUL);
            npc.responseContext().setState(State.ContentServed);
            return input;
        }

        throw new AssertionError();
    }
}
