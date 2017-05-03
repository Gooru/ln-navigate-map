package org.gooru.navigatemap.processor.coursepath.flows.strategy.nu;

import java.util.Objects;

import org.gooru.navigatemap.processor.coursepath.flows.Flow;
import org.gooru.navigatemap.processor.coursepath.repositories.ContentRepositoryBuilder;
import org.gooru.navigatemap.processor.data.NavigateProcessorContext;
import org.gooru.navigatemap.processor.data.State;
import org.gooru.navigatemap.processor.data.SuggestionContext;
import org.gooru.navigatemap.responses.ExecutionResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author ashish on 6/3/17.
 */
final class PostLessonSuggestionsFlow implements Flow<NavigateProcessorContext> {

    private NavigateProcessorContext npc;
    private ExecutionResult<NavigateProcessorContext> output;
    private static final Logger LOGGER = LoggerFactory.getLogger(PostLessonSuggestionsFlow.class);

    @Override
    public ExecutionResult<NavigateProcessorContext> apply(ExecutionResult<NavigateProcessorContext> input) {
        /*
         * Keep the older context if there is suggestion for content address. Don't copy the new one.
         */
        npc = input.result();
        output = input;
        LOGGER.debug("Applying post lesson suggestions flow");
        if (input.isCompleted() || npc.suggestionsTurnedOff()) {
            return input;
        }
        /*
         * If there are any post test suggestions, we should mark the processing done as pre test need not be calculated
         * If there are no, then we need to mark it done as this is the last step.
         */

        if (postLessonSuggestionsApplicable()) {
            LOGGER.debug("Post lesson suggestions flow applicable.");
            setupPostLessonSuggestions();
        }
        return output;
    }

    private void setupPostLessonSuggestions() {
        SuggestionContext suggestions = ContentRepositoryBuilder.buildContentSuggestionsService()
            .findPostLessonSuggestions(npc.getCurrentContentAddress(), npc.navigateMessageContext().getUserId());
        if (suggestions.hasSuggestions()) {
            LOGGER.debug("Post lesson suggestions: found suggestions");
            terminateFlowWithPostLessonSuggestions(suggestions);
        }
    }

    private void terminateFlowWithPostLessonSuggestions(SuggestionContext suggestions) {
        npc.getCtxSuggestions().addAssessments(suggestions.getAssessments());
        npc.getCtxSuggestions().addCollections(suggestions.getCollections());
        npc.responseContext().setContentAddress(npc.getCurrentContentAddress());
        markAsDone(State.LessonEndSuggested);
    }

    private void markAsDone(State contentServed) {
        output.setStatus(ExecutionResult.ExecutionStatus.SUCCESSFUL);
        npc.responseContext().setState(contentServed);
    }

    private boolean postLessonSuggestionsApplicable() {
        // If we are starting the course or user is explicitly asking to start from a particular location, then no
        if (npc.requestContext().getState() == State.Start || npc.requestContext().getState() == State.Continue) {
            return false;
        }
        if (npc.getCurrentContentAddress().isOnAlternatePath()) {
            return false;
        }
        // Do not care about next lesson to be null or not. We just check if current content's lesson is
        // different from current one and we have not suggested already.
        // NOTE: When we bring in the lesson as back fill, this may be changed
        return (npc.getCurrentContentAddress().getLesson() != null && !Objects
            .equals(npc.getNextContentAddress().getLesson(), npc.getCurrentContentAddress().getLesson())) && (
            npc.requestContext().getState() != State.LessonEndSuggested);
    }
}
