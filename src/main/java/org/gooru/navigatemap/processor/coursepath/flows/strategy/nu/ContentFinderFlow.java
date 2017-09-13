package org.gooru.navigatemap.processor.coursepath.flows.strategy.nu;

import org.gooru.navigatemap.constants.HttpConstants;
import org.gooru.navigatemap.exceptions.HttpResponseWrapperException;
import org.gooru.navigatemap.processor.coursepath.flows.Flow;
import org.gooru.navigatemap.processor.coursepath.repositories.nu.ContentFinderRepository;
import org.gooru.navigatemap.processor.coursepath.repositories.nu.ContentRepositoryBuilder;
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

        LOGGER.debug("Applying content finder flow");
        if (this.executionResult.isCompleted()) {
            return executionResult;
        }
        npc = input.result();

        if (userExplicitlyAskedToStartHere()) {
            LOGGER.debug("Starting from location where user explicitly asked about");
            return explicitlyStartAtSpecifiedAddress();
        }

        if (needToStartCourse()) {
            LOGGER.debug("Need to start course. Starting it.");
            return startCourse();
        }

        if (npc.suggestionsTurnedOff()) {
            LOGGER.debug("Suggestions are off, hence finding next content on main path w/o skipping logic");
            return fetchNextItemFromCULWithoutSuggestions();
        }

        LOGGER.debug("Initiating the content finder flow with suggestions");
        return fetchNextItem();
    }

    private boolean needToStartCourse() {
        return npc.requestContext().getState() == State.Continue;
    }

    private ExecutionResult<NavigateProcessorContext> explicitlyStartAtSpecifiedAddress() {
        validateStartPointProvidedByUser();
        if (npc.requestContext().needToStartLesson()) {
            return fetchNextItem();
        } else {
            if (npc.currentItemIsResource()) {
                npc.setNextContextAddress(npc.getCurrentContentAddressQualified());
                npc.responseContext().setContentAddress(npc.getNextContentAddress());
            } else {
                npc.setNextContextAddress(npc.getCurrentContentAddress());
                npc.responseContext().setContentAddressWithItemFromCollection(npc.getNextContentAddress());
            }
            markAsDone(State.ContentServed);
            return executionResult;
        }
    }

    private void validateStartPointProvidedByUser() {
        ContentFinderRepository contentFinderRepository = ContentRepositoryBuilder.buildContentFinderRepository();
        if (!contentFinderRepository.validateContentAddress(npc.getCurrentContentAddressQualified())) {
            throw new HttpResponseWrapperException(HttpConstants.HttpStatus.BAD_REQUEST, "Invalid CUL info in context");
        }
    }

    private boolean userExplicitlyAskedToStartHere() {
        return npc.requestContext().getState() == State.Start;
    }

    private ExecutionResult<NavigateProcessorContext> startCourse() {
        ContentAddress contentAddress = ContentRepositoryBuilder.buildContentFinderRepository()
            .findFirstNotCompletedContentInCourse(npc.createFinderContext());
        if (contentAddress != null) {
            if (contentAddress.getCollection() != null) {
                // NOTE that we need to populate currentItem* fields in content address object from repo
                npc.setNextContextAddress(contentAddress);
                npc.responseContext().setContentAddress(contentAddress);
                markAsDone(State.ContentServed);
            } else {
                markAsDone(State.Done);
            }
        } else {
            throw new IllegalStateException("Not able to locate first valid content in course");
        }
        executionResult.setStatus(ExecutionResult.ExecutionStatus.SUCCESSFUL);
        return executionResult;
    }

    private ExecutionResult<NavigateProcessorContext> fetchNextItem() {
        final FinderContext finderContext = npc.createFinderContext();
        ContentAddress contentAddress =
            ContentRepositoryBuilder.buildContentFinderRepository().fetchNextItem(finderContext);
        if (contentAddress != null && contentAddress.getCollection() != null) {
            LOGGER.debug("Found next item");
            // NOTE that we need to populate currentItem* fields in content address object from repo
            npc.setNextContextAddress(contentAddress);
            npc.responseContext().setContentAddress(contentAddress);
            markAsDone(State.ContentServed);
            LOGGER.debug("Will serve content found right now");
        } else {
            LOGGER.warn("Not able to locate valid content in course");
            markAsDone(State.Done);
        }
        return executionResult;
    }

    private ExecutionResult<NavigateProcessorContext> fetchNextItemFromCULWithoutSuggestions() {
        ContentAddress contentAddress = ContentRepositoryBuilder.buildContentFinderRepository()
            .findNextContentFromCULWithoutSkipLogicAndAlternatePaths(npc.getCurrentContentAddress(),
                npc.createFinderContext());
        if (contentAddress != null) {
            if (contentAddress.getCollection() != null) {
                // NOTE that we need to populate currentItem* fields in content address object from repo
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
