package org.gooru.navigatemap.processor.coursepath.flows;

import org.gooru.navigatemap.processor.data.NavigateProcessorContext;
import org.gooru.navigatemap.responses.ExecutionResult;

/**
 * @author ashish on 6/3/17.
 */
public final class Workflow {
    private Workflow() {
        throw new AssertionError();
    }

    public static void submit(NavigateProcessorContext npc) {
        ExecutionResult<NavigateProcessorContext> result =
            new ExecutionResult<>(npc, ExecutionResult.ExecutionStatus.CONTINUE_PROCESSING);

        FlowBuilder.buildPostContentSuggestionsFlow().apply(result);
        FlowBuilder.buildContentFinderFlow().apply(result);
        FlowBuilder.buildPostLessonSuggestionsFlow().apply(result);
        FlowBuilder.buildPreLessonSuggestionsFlow().apply(result);
        FlowBuilder.buildContentServeFlow().apply(result);

    }
}
