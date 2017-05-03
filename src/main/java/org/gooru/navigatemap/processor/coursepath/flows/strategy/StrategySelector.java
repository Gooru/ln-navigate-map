package org.gooru.navigatemap.processor.coursepath.flows.strategy;

import org.gooru.navigatemap.processor.coursepath.flows.FlowBuilder;
import org.gooru.navigatemap.processor.coursepath.flows.strategy.global.GlobalFlowBuilder;
import org.gooru.navigatemap.processor.coursepath.flows.strategy.nu.NuFlowBuilder;
import org.gooru.navigatemap.processor.data.NavigateProcessorContext;

/**
 * @author ashish on 3/5/17.
 */
public final class StrategySelector {

    private final NavigateProcessorContext npc;

    public StrategySelector(NavigateProcessorContext npc) {
        this.npc = npc;
    }

    public FlowBuilder findFlowBuilderBasedOnStrategy() {
        if (nuStrategyApplicable()) {
            return new NuFlowBuilder();
        } else {
            return new GlobalFlowBuilder();
        }
    }

    private boolean nuStrategyApplicable() {
        // TODO: implement this
        return false;
    }
}
