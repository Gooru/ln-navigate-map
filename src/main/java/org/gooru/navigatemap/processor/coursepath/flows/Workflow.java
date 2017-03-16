package org.gooru.navigatemap.processor.coursepath.flows;

import org.gooru.navigatemap.processor.data.NavigateProcessorContext;
import org.gooru.navigatemap.processor.data.State;
import org.gooru.navigatemap.responses.ExecutionResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author ashish on 6/3/17.
 */
public final class Workflow {
    private Workflow() {
        throw new AssertionError();
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(Workflow.class);

    public static void submit(NavigateProcessorContext npc) {
        ExecutionResult<NavigateProcessorContext> result =
            new ExecutionResult<>(npc, ExecutionResult.ExecutionStatus.CONTINUE_PROCESSING);

        result = FlowBuilder.buildPostContentSuggestionsFlow().apply(result);
        result = FlowBuilder.buildContentFinderFlow().apply(result);
        result = FlowBuilder.buildPostLessonSuggestionsFlow().apply(result);
        result = FlowBuilder.buildPreLessonSuggestionsFlow().apply(result);

        if (!result.isCompleted()) {
            LOGGER.warn("Workflow not completed, putting in done");
            npc.responseContext().setState(State.Done);
        }
    }
}
