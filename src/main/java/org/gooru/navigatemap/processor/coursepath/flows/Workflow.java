package org.gooru.navigatemap.processor.coursepath.flows;

import org.gooru.navigatemap.processor.coursepath.flows.strategy.StrategySelector;
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

        FlowBuilder flowBuilder = new StrategySelector(npc).findFlowBuilderBasedOnStrategy();

        npc.responseContext().setVersion(flowBuilder.version().flowVersion());

        result = flowBuilder.buildPostContentSuggestionsFlow().apply(result);
        result = flowBuilder.buildContentFinderFlow().apply(result);
        result = flowBuilder.buildPostLessonSuggestionsFlow().apply(result);
        result = flowBuilder.buildPreLessonSuggestionsFlow().apply(result);

        if (!result.isCompleted()) {
            terminateFlowWithContent(result.result());
        }
    }

    private static void terminateFlowWithContent(NavigateProcessorContext npc) {
        // Currently you can't set up a separate item on alternate path as address
        if (npc.getNextContentAddress().isValidAddress()) {
            npc.responseContext().setContentAddressWithItemFromCollection(npc.getNextContentAddress());
            npc.responseContext().setState(State.ContentServed);
        } else {
            LOGGER.warn("Workflow not completed, putting in done");
            npc.responseContext().setState(State.Done);
        }
    }

    public static void terminateFlowWithContent(ExecutionResult<NavigateProcessorContext> result,
            NavigateProcessorContext npc) {
        // Currently you can't set up a separate item on alternate path as address
        if (npc.getNextContentAddress().isValidAddress()) {
            npc.responseContext().setContentAddressWithItemFromCollection(npc.getNextContentAddress());
            npc.responseContext().setState(State.ContentServed);
            result.setStatus(ExecutionResult.ExecutionStatus.SUCCESSFUL);
        } else {
            npc.responseContext().setState(State.Done);
        }
    }

}
