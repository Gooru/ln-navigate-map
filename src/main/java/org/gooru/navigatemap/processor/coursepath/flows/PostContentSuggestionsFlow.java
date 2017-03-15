package org.gooru.navigatemap.processor.coursepath.flows;

import java.util.List;

import org.gooru.navigatemap.app.components.AppConfiguration;
import org.gooru.navigatemap.app.components.utilities.DbLookupUtility;
import org.gooru.navigatemap.processor.coursepath.repositories.ContentFinderRepository;
import org.gooru.navigatemap.processor.coursepath.repositories.ContentRepositoryBuilder;
import org.gooru.navigatemap.processor.data.CollectionSubtype;
import org.gooru.navigatemap.processor.data.CollectionType;
import org.gooru.navigatemap.processor.data.NavigateProcessorContext;
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
        if (input.isCompleted() || !AppConfiguration.getInstance().suggestionsTurnedOn() || npc.navigateMessageContext()
            .isUserAnonymous()) {
            return input;
        }
        // Right now we only serve benchmark if user did a post test successfully
        if (npc.requestContext().getCurrentItemType() != CollectionType.Assessment
            || npc.requestContext().getCurrentItemSubtype() != CollectionSubtype.PostTest) {
            return input;
        }

        applyBASuggestions();
        return result;
    }

    private void applyBASuggestions() {
        if (!isEligibleForBA()) {
            return;
        }

        final ContentFinderRepository contentFinderRepository = ContentRepositoryBuilder.buildContentFinderService();
        // Find competencies for this post test
        List<String> competencies =
            contentFinderRepository.findCompetenciesForPostTest(npc.requestContext().getCurrentItemId());
        // Find benchmark associated with standards for post test
        List<String> benchmarks = contentFinderRepository.findBenchmarkAssessments(competencies);
        // Filter it based on if user has already taken them or user has already added them to map
        List<String> benchmarksNotAddedByUser = ContentRepositoryBuilder.buildContentFilterService()
            .filterBAForNotAddedByUser(benchmarks, npc.navigateMessageContext().getUserId());

        // If any suggestions remains, we present it
        if (benchmarksNotAddedByUser != null && !benchmarksNotAddedByUser.isEmpty()) {
            benchmarksNotAddedByUser.forEach(benchmark -> {
                npc.getCtxSuggestions().addAssessment(benchmark);
            });
            result.setStatus(ExecutionResult.ExecutionStatus.SUCCESSFUL);
        }
    }

    private boolean isEligibleForBA() {
        return npc.requestContext().getScorePercent() != null
            && npc.requestContext().getScorePercent() >= DbLookupUtility.getInstance().postTestThresholdForBA();
    }
}