package org.gooru.navigatemap.processor.coursepath.flows;

import java.util.Objects;

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
final class PreLessonSuggestionsFlow implements Flow<NavigateProcessorContext> {

    private NavigateProcessorContext npc;
    private ExecutionResult<NavigateProcessorContext> output;
    private static final Logger LOGGER = LoggerFactory.getLogger(PreLessonSuggestionsFlow.class);

    @Override
    public ExecutionResult<NavigateProcessorContext> apply(ExecutionResult<NavigateProcessorContext> input) {
        /*
         * If there are suggestions, then populate new CUL but not CA info from next address. If there are no
         * suggestions, populate all info.
         */

        npc = input.result();
        output = input;
        LOGGER.debug("Applying pre lesson suggestions flow");
        if (input.isCompleted() || npc.suggestionsTurnedOff()) {
            LOGGER.debug("Returning w/o applying pre lesson suggestions");
            return input;
        }

        if (preLessonSuggestionsApplicable()) {
            LOGGER.debug("Pre lesson suggestions are applicable");
            setupPreLessonSuggestions();
        } else {
            LOGGER.debug("Pre lesson suggestions are not applicable");
            Workflow.terminateFlowWithContent(output, npc);
        }

        return output;
    }

    private void setupPreLessonSuggestions() {
        LOGGER.debug("Setting up pre lesson suggestions");
        SuggestionContext suggestions = ContentRepositoryBuilder.buildContentSuggestionsService()
            .findPreLessonSuggestions(npc.getNextContentAddress(), npc.navigateMessageContext().getUserId());
        if (suggestions.hasSuggestions()) {
            LOGGER.debug("Found pre lesson suggestions");
            terminateFlowWithPreLessonSuggestions(suggestions);
        } else {
            LOGGER.debug("Did not find pre lesson suggestions to apply");
            Workflow.terminateFlowWithContent(output, npc);
        }
    }

    private void terminateFlowWithPreLessonSuggestions(SuggestionContext suggestions) {
        // Setup suggestions
        npc.getCtxSuggestions().addAssessments(suggestions.getAssessments());
        npc.getCtxSuggestions().addCollections(suggestions.getCollections());
        npc.responseContext().setContentAddressWithoutItem(npc.getNextContentAddress());
        markAsDone(State.LessonStartSuggested);
    }

    private void markAsDone(State contentServed) {
        output.setStatus(ExecutionResult.ExecutionStatus.SUCCESSFUL);
        npc.responseContext().setState(contentServed);
    }

    private boolean preLessonSuggestionsApplicable() {
        // If user is starting a lesson, then yes
        if (npc.requestContext().needToStartLesson()) {
            return true;
        }
        // If user is explicitly asking to start from a particular location, then no
        if (npc.requestContext().getState() == State.Start) {
            return false;
        }
        // Else infer. Do not care about previous or current lesson to be null or not. We just check if next content's
        // lesson is different from current one.
        // NOTE: When we bring in the lesson as back fill, this needs to be changed
        return npc.getNextContentAddress().getLesson() != null && !Objects
            .equals(npc.getNextContentAddress().getLesson(), npc.getCurrentContentAddress().getLesson());
    }

}