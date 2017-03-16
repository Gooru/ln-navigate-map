package org.gooru.navigatemap.processor.coursepath.flows;

import org.gooru.navigatemap.processor.coursepath.repositories.ContentRepositoryBuilder;
import org.gooru.navigatemap.processor.data.ContentAddress;
import org.gooru.navigatemap.processor.data.NavigateProcessorContext;
import org.gooru.navigatemap.processor.data.ResponseContext;
import org.gooru.navigatemap.processor.data.State;
import org.gooru.navigatemap.responses.ExecutionResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author ashish on 6/3/17.
 */
final class ContentFinderFlow implements Flow<NavigateProcessorContext> {

    private NavigateProcessorContext npc;
    private ExecutionResult<NavigateProcessorContext> executionResult;
    private static final Logger LOGGER = LoggerFactory.getLogger(ContentFinderFlow.class);

    @Override
    public ExecutionResult<NavigateProcessorContext> apply(ExecutionResult<NavigateProcessorContext> input) {

        this.executionResult = input;

        if (this.executionResult.isCompleted()) {
            return executionResult;
        }
        npc = input.result();

        if (userExplicitlyAskedToStartHere()) {
            // We should have the content here already as this may be start of course
            npc.responseContext().setContentAddress(npc.getNextContentAddress());
            markAsDone(State.ContentServed);
        }

        if (npc.suggestionsTurnedOff()) {
            fetchNextItemFromCULWithoutSuggestions();
        }
        return executionResult;
    }

    private boolean userExplicitlyAskedToStartHere() {
        return npc.requestContext().getState() == State.Continue;
    }

    private void fetchNextItemFromCULWithoutSuggestions() {
        ResponseContext responseContext = npc.responseContext();
        ContentAddress contentAddress =
            ContentRepositoryBuilder.buildContentFinderService().findNextContentFromCUL(npc.getCurrentContentAddress());
        if (contentAddress != null) {
            if (contentAddress.getCollection() != null) {
                npc.setNextContextAddress(contentAddress);
                responseContext.setContentAddress(contentAddress);
                markAsDone(State.ContentServed);
            } else {
                markAsDone(State.Done);
            }
        } else {
            LOGGER.warn("Not able to locate valid content in course");
            markAsDone(State.Done);
        }

    }

    private void markAsDone(State contentServed) {
        executionResult.setStatus(ExecutionResult.ExecutionStatus.SUCCESSFUL);
        npc.responseContext().setState(contentServed);
    }

}
