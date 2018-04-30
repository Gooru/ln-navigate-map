package org.gooru.navigatemap.processor.coursepath.flows.strategy;

import java.util.Objects;
import java.util.UUID;

import org.gooru.navigatemap.processor.coursepath.flows.FlowBuilder;
import org.gooru.navigatemap.processor.coursepath.flows.strategy.global.GlobalFlowBuilder;
import org.gooru.navigatemap.processor.coursepath.flows.strategy.nu.NuFlowBuilder;
import org.gooru.navigatemap.processor.coursepath.repositories.global.ContentRepositoryBuilder;
import org.gooru.navigatemap.processor.data.NavigateProcessorContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author ashish on 3/5/17.
 */
public final class StrategySelector {

    private final NavigateProcessorContext npc;
    private static final String NU_VERSION = "3.0-nu";
    private static final Logger LOGGER = LoggerFactory.getLogger(StrategySelector.class);

    public StrategySelector(NavigateProcessorContext npc) {
        this.npc = npc;
    }

    public FlowBuilder findFlowBuilderBasedOnStrategy() {
        if (nuStrategyApplicable()) {
            LOGGER.debug("Selecting NU strategy");
            return new NuFlowBuilder();
        } else {
            LOGGER.debug("Selecting Global strategy");
            return new GlobalFlowBuilder();
        }
    }

    private boolean nuStrategyApplicable() {
        // NU strategy is deprecated. We always apply global one now
        return false;
    }
}
