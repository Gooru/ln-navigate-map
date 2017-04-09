package org.gooru.navigatemap.processor.coursepath.flows;

import org.gooru.navigatemap.processor.coursepath.repositories.ContentRepositoryBuilder;
import org.gooru.navigatemap.processor.data.ContentAddress;
import org.gooru.navigatemap.processor.data.FinderContext;
import org.gooru.navigatemap.processor.data.NavigateProcessorContext;
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
            return explicitlyStartAtSpecifiedAddress();
        }

        if (needToStartCourse()) {
            return startCourse();
        }

        if (npc.suggestionsTurnedOff()) {
            return fetchNextItemFromCULWithoutSuggestions();
        }

        return fetchNextItem();
    }

    private boolean needToStartCourse() {
        return npc.requestContext().getState() == State.Continue;
    }

    private ExecutionResult<NavigateProcessorContext> explicitlyStartAtSpecifiedAddress() {
        validateStartPointProvidedByUser();
        npc.responseContext().setContentAddress(npc.getNextContentAddress());
        markAsDone(State.ContentServed);
        return executionResult;
    }

    private void validateStartPointProvidedByUser() {
        // TODO: Provide implementation
        LOGGER.warn("This method is not implemented");
    }

    private boolean userExplicitlyAskedToStartHere() {
        return npc.requestContext().getState() == State.Start;
    }

    private ExecutionResult<NavigateProcessorContext> startCourse() {
        ContentAddress contentAddress = ContentRepositoryBuilder.buildContentFinderRepository()
            .findFirstContentInCourse(npc.requestContext().getCourseId());
        if (contentAddress != null) {
            if (contentAddress.getCollection() != null) {
                npc.setNextContextAddress(contentAddress);
                if (npc.suggestionsTurnedOff()) {
                    npc.responseContext().setContentAddress(contentAddress);
                    markAsDone(State.ContentServed);
                }
            } else {
                executionResult.setStatus(ExecutionResult.ExecutionStatus.SUCCESSFUL);
                npc.responseContext().setState(State.Done);
            }
        } else {
            throw new IllegalStateException("Not able to locate first valid content in course");
        }
        executionResult.setStatus(ExecutionResult.ExecutionStatus.CONTINUE_PROCESSING);
        return executionResult;
    }

    private ExecutionResult<NavigateProcessorContext> fetchNextItem() {
        final FinderContext finderContext = createFinderContext();
        ContentAddress contentAddress = ContentRepositoryBuilder.buildNavigateService().navigateNext(finderContext);
        if (contentAddress != null) {
            npc.setNextContextAddress(contentAddress);
            if (finderContext.isStatusDone()) {
                npc.responseContext().setContentAddress(contentAddress);
                npc.responseContext()
                    .setCurrentItemAddress(finderContext.getCurrentItemId(), finderContext.getCurrentItemType(),
                        finderContext.getCurrentItemSubtype());
                markAsDone(State.ContentServed);
            }
        } else {
            LOGGER.warn("Not able to locate valid content in course");
            markAsDone(State.Done);
        }
        return executionResult;
    }

    private FinderContext createFinderContext() {
        return new FinderContext(npc.requestContext().getState(), npc.requestContext(), npc.getCurrentContentAddress(),
            npc.navigateMessageContext().getUserId());
    }

    private ExecutionResult<NavigateProcessorContext> fetchNextItemFromCULWithoutSuggestions() {
        ContentAddress contentAddress = ContentRepositoryBuilder.buildContentFinderRepository()
            .findNextContentFromCUL(npc.getCurrentContentAddress());
        if (contentAddress != null) {
            if (contentAddress.getCollection() != null) {
                npc.setNextContextAddress(contentAddress);
                npc.responseContext().setContentAddress(contentAddress);
                markAsDone(State.ContentServed);
            } else {
                markAsDone(State.Done);
            }
        } else {
            LOGGER.warn("Not able to locate valid content in course");
            markAsDone(State.Done);
        }
        return executionResult;
    }

    private void markAsDone(State contentServed) {
        executionResult.setStatus(ExecutionResult.ExecutionStatus.SUCCESSFUL);
        npc.responseContext().setState(contentServed);
    }

}
