package org.gooru.navigatemap.processor.coursepath.flows.strategy.nu;

import java.util.List;

import org.gooru.navigatemap.app.components.utilities.DbLookupUtility;
import org.gooru.navigatemap.processor.coursepath.flows.Flow;
import org.gooru.navigatemap.processor.coursepath.repositories.nu.ContentFinderRepository;
import org.gooru.navigatemap.processor.coursepath.repositories.nu.ContentRepositoryBuilder;
import org.gooru.navigatemap.processor.data.*;
import org.gooru.navigatemap.responses.ExecutionResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author ashish on 6/3/17.
 */
final class PostContentSuggestionsFlow implements Flow<NavigateProcessorContext> {

    private NavigateProcessorContext npc;
    private ExecutionResult<NavigateProcessorContext> result;
    private static final Logger LOGGER = LoggerFactory.getLogger(PostContentSuggestionsFlow.class);

    @Override
    public ExecutionResult<NavigateProcessorContext> apply(ExecutionResult<NavigateProcessorContext> input) {
        result = input;
        npc = result.result();

        LOGGER.debug("Will apply post content suggestions flow");
        if (input.isCompleted() || npc.suggestionsTurnedOff()) {
            LOGGER.debug("Returning without applying suggestions");
            return result;
        }
        if (npc.requestContext().needToStartLesson()) {
            LOGGER.debug("Starting lesson so skipping post content suggestions flow");
            return result;
        }
        // Right now we only serve resource if user did an assessment
        if (userDidAnAssessment()) {
            LOGGER.debug("User did an assessment. Trying to apply resource suggestions");
            applyResourceSuggestions();
        }
        return result;
    }

    private boolean userDidAnAssessment() {
        return npc.requestContext().getCurrentItemType() == CurrentItemType.Assessment
            && npc.requestContext().getState() == State.ContentServed;
    }

    private void applyResourceSuggestions() {
        final ContentFinderRepository contentFinderRepository = ContentRepositoryBuilder.buildContentFinderRepository();
        if (isEligibleForResourceSuggestion()) {
            LOGGER.debug("User is eligible for resource suggestions");
            String scoreRange = findScoreRange();
            FinderContext finderContext = npc.createFinderContext();
            finderContext.setScoreRange(scoreRange);
            List<SignatureResource> resourceSuggestions =
                contentFinderRepository.findResourceSuggestionsForAssessment(finderContext);
            if (resourceSuggestions != null && !resourceSuggestions.isEmpty()) {
                npc.getCtxSuggestions().addResources(resourceSuggestions);
                markAsDone();
            }
        } else if (isCompetencyCompleted()) {
            LOGGER.debug("User completed the competency. Will mark as completed if not already completed");
            contentFinderRepository.markCompetencyCompletedForUser(npc.createFinderContext());
        }

    }

    private String findScoreRange() {
        return DbLookupUtility.getInstance().preTestScoreRangeNameByScore(npc.requestContext().getScorePercent());
    }

    private void markAsDone() {
        result.setStatus(ExecutionResult.ExecutionStatus.SUCCESSFUL);
        npc.responseContext().setState(State.ContentEndSuggested);
    }

    private boolean isEligibleForResourceSuggestion() {
        return npc.requestContext().getScorePercent() != null
            && npc.requestContext().getScorePercent() < DbLookupUtility.getInstance()
            .thresholdForCompetencyCompletionBasedOnAssessment();
    }

    private boolean isCompetencyCompleted() {
        return npc.requestContext().getScorePercent() != null
            && npc.requestContext().getScorePercent() >= DbLookupUtility.getInstance()
            .thresholdForCompetencyCompletionBasedOnAssessment();
    }
}
