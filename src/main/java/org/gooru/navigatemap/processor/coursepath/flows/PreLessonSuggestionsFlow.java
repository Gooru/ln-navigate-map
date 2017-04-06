package org.gooru.navigatemap.processor.coursepath.flows;

import java.util.Objects;

import org.gooru.navigatemap.processor.coursepath.repositories.ContentRepositoryBuilder;
import org.gooru.navigatemap.processor.data.NavigateProcessorContext;
import org.gooru.navigatemap.processor.data.State;
import org.gooru.navigatemap.processor.data.SuggestionContext;
import org.gooru.navigatemap.responses.ExecutionResult;

/**
 * @author ashish on 6/3/17.
 */
final class PreLessonSuggestionsFlow implements Flow<NavigateProcessorContext> {

    private NavigateProcessorContext npc;
    private ExecutionResult<NavigateProcessorContext> output;

    @Override
    public ExecutionResult<NavigateProcessorContext> apply(ExecutionResult<NavigateProcessorContext> input) {
        /*
         * If there are suggestions, then populate new CUL but not CA info from next address. If there are no
         * suggestions, populate all info.
         */

        npc = input.result();
        output = input;
        if (input.isCompleted() || npc.suggestionsTurnedOff()) {
            return input;
        }

        if (preLessonSuggestionsApplicable()) {
            setupPreLessonSuggestions();
        }

        return output;
    }

    private void setupPreLessonSuggestions() {
        SuggestionContext suggestions = ContentRepositoryBuilder.buildContentSuggestionsService()
            .findPreLessonSuggestions(npc.getNextContentAddress(), npc.navigateMessageContext().getUserId());
        if (suggestions.hasSuggestions()) {
            terminateFlowWithPreLessonSuggestions(suggestions);
        } else {
            Workflow.terminateFlowWithContent(output, npc);
        }
    }

    private void terminateFlowWithPreLessonSuggestions(SuggestionContext suggestions) {
        // Setup suggestions
        npc.getCtxSuggestions().addAssessments(suggestions.getAssessments());
        npc.getCtxSuggestions().addCollections(suggestions.getCollections());
        npc.responseContext().setContentAddress(npc.getNextContentAddress());
        markAsDone(State.LessonStartSuggested);
    }

    private void markAsDone(State contentServed) {
        output.setStatus(ExecutionResult.ExecutionStatus.SUCCESSFUL);
        npc.responseContext().setState(contentServed);
    }

    private boolean preLessonSuggestionsApplicable() {
        // If user is explicitly asking to start from a particular location, then no
        if (npc.requestContext().getState() == State.Start) {
            return false;
        }
        // Do not care about previous or current lesson to be null or not. We just check if next content's lesson is
        // different from current one.
        // NOTE: When we bring in the lesson as back fill, this needs to be changed
        return npc.getNextContentAddress().getLesson() != null && Objects
            .equals(npc.getNextContentAddress().getLesson(), npc.getCurrentContentAddress().getLesson());
    }

}