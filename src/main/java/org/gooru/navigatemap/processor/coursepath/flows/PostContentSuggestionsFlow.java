package org.gooru.navigatemap.processor.coursepath.flows;

import java.util.List;

import org.gooru.navigatemap.app.components.utilities.DbLookupUtility;
import org.gooru.navigatemap.processor.coursepath.repositories.ContentFinderRepository;
import org.gooru.navigatemap.processor.coursepath.repositories.ContentRepositoryBuilder;
import org.gooru.navigatemap.processor.data.CollectionSubtype;
import org.gooru.navigatemap.processor.data.CollectionType;
import org.gooru.navigatemap.processor.data.NavigateProcessorContext;
import org.gooru.navigatemap.processor.data.State;
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
        // Right now we only serve benchmark if user did a post test successfully or backfills if user did pre test
        if (userDidAPostTest()) {
            LOGGER.debug("User did a post test. Trying to apply BA suggestions");
            applyBASuggestions();
        } else if (userDidAPreTest()) {
            LOGGER.debug("User did a pre test. Trying to apply back fills suggestions");
            applyBackfillsSuggestions();
        }
        return result;
    }

    private boolean userDidAPreTest() {
        return npc.requestContext().getCurrentItemType() == CollectionType.Assessment
            && npc.requestContext().getCurrentItemSubtype() == CollectionSubtype.PreTest
            && npc.requestContext().getState() == State.ContentServed;
    }

    private boolean userDidAPostTest() {
        return npc.requestContext().getCurrentItemType() == CollectionType.Assessment
            && npc.requestContext().getCurrentItemSubtype() == CollectionSubtype.PostTest
            && npc.requestContext().getState() == State.ContentServed;
    }

    private void applyBASuggestions() {
        if (!isEligibleForBA()) {
            LOGGER.debug("User is not eligible for BA suggestions");
            return;
        }

        final ContentFinderRepository contentFinderRepository = ContentRepositoryBuilder.buildContentFinderRepository();
        // Find competencies for this post test
        List<String> competencies =
            contentFinderRepository.findCompetenciesForPostTest(npc.requestContext().getCurrentItemId());
        // Find benchmark associated with standards for post test
        List<String> benchmarks = contentFinderRepository.findBenchmarkAssessments(competencies);
        // Filter it based on if user has already taken them or user has already added them to map
        List<String> benchmarksNotAddedByUser = ContentRepositoryBuilder.buildContentFilterRepository()
            .filterBAForNotAddedByUser(benchmarks, npc.navigateMessageContext().getUserId());

        // If any suggestions remains, we present it
        if (benchmarksNotAddedByUser != null && !benchmarksNotAddedByUser.isEmpty()) {
            LOGGER.debug("Found BA which are not added by user");
            benchmarksNotAddedByUser.forEach(benchmark -> npc.getCtxSuggestions().addAssessment(benchmark));
            markAsDone();
        }
    }

    private void applyBackfillsSuggestions() {
        if (npc.requestContext().getScorePercent() == null) {
            LOGGER.debug("Null score. Won't apply backfills.");
            return;
        }
        String rangeName =
            DbLookupUtility.getInstance().preTestScoreRangeNameByScore(npc.requestContext().getScorePercent());
        if (rangeName == null || rangeName.isEmpty()) {
            LOGGER.debug("Score range name not found. Won't apply backfills");
            return;
        }

        final ContentFinderRepository contentFinderRepository = ContentRepositoryBuilder.buildContentFinderRepository();
        List<String> backfills = contentFinderRepository
            .findBackfillsForPreTestAndScoreRange(npc.requestContext().getCurrentItemId(), rangeName);

        if (backfills != null && !backfills.isEmpty()) {
            LOGGER.debug("Found backfills. Willl apply");
            backfills.forEach(backfill -> npc.getCtxSuggestions().addCollection(backfill));
            markAsDone();
        }
        LOGGER.debug("No backfills found");

    }

    private void markAsDone() {
        result.setStatus(ExecutionResult.ExecutionStatus.SUCCESSFUL);
        npc.responseContext().setState(State.ContentEndSuggested);
    }

    private boolean isEligibleForBA() {
        return npc.requestContext().getScorePercent() != null
            && npc.requestContext().getScorePercent() >= DbLookupUtility.getInstance().postTestThresholdForBA();
    }
}