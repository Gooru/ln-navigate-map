package org.gooru.navigatemap.processor.coursepath.flows.strategy;

import java.util.UUID;

import org.gooru.navigatemap.processor.coursepath.flows.FlowBuilder;
import org.gooru.navigatemap.processor.coursepath.flows.strategy.global.GlobalFlowBuilder;
import org.gooru.navigatemap.processor.coursepath.flows.strategy.nu.NuFlowBuilder;
import org.gooru.navigatemap.processor.coursepath.repositories.ContentRepositoryBuilder;
import org.gooru.navigatemap.processor.data.NavigateProcessorContext;

/**
 * @author ashish on 3/5/17.
 */
public final class StrategySelector {

    private final NavigateProcessorContext npc;
    private static final String NU_VERSION = "3.0-nu";

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
        UUID courseId = npc.requestContext().getCourseId();
        String version = ContentRepositoryBuilder.buildContentFinderRepository().findCourseVersion(courseId);
        return version != null && NU_VERSION.equals(version);
    }
}
