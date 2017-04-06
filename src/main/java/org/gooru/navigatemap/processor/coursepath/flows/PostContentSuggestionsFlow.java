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

/**
 * @author ashish on 6/3/17.
 */
final class PostContentSuggestionsFlow implements Flow<NavigateProcessorContext> {

    private NavigateProcessorContext npc;
    private ExecutionResult<NavigateProcessorContext> result;

    @Override
    public ExecutionResult<NavigateProcessorContext> apply(ExecutionResult<NavigateProcessorContext> input) {
        result = input;
        npc = result.result();
        if (input.isCompleted() || npc.suggestionsTurnedOff()) {
            return result;
        }
        // Right now we only serve benchmark if user did a post test successfully or backfills if user did pre test
        if (userDidAPostTest()) {
            applyBASuggestions();
        } else if (userDidAPreTest()) {
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
            benchmarksNotAddedByUser.forEach(benchmark -> npc.getCtxSuggestions().addAssessment(benchmark));
            markAsDone();
        }
    }

    private void applyBackfillsSuggestions() {
        if (npc.requestContext().getScorePercent() == null) {
            return;
        }
        String rangeName =
            DbLookupUtility.getInstance().preTestScoreRangeNameByScore(npc.requestContext().getScorePercent());
        if (rangeName == null || rangeName.isEmpty()) {
            return;
        }

        final ContentFinderRepository contentFinderRepository = ContentRepositoryBuilder.buildContentFinderRepository();
        List<String> backfills = contentFinderRepository
            .findBackfillsForPreTestAndScoreRange(npc.requestContext().getCurrentItemId(), rangeName);

        if (backfills != null && !backfills.isEmpty()) {
            backfills.forEach(backfill -> npc.getCtxSuggestions().addCollection(backfill));
            markAsDone();
        }

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