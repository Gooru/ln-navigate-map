package org.gooru.navigatemap.processor.coursepath.flows.strategy.nu;

import org.gooru.navigatemap.processor.coursepath.flows.Flow;
import org.gooru.navigatemap.processor.coursepath.flows.FlowBuilder;
import org.gooru.navigatemap.processor.coursepath.flows.FlowVersion;
import org.gooru.navigatemap.processor.coursepath.flows.strategy.NoopFlow;
import org.gooru.navigatemap.processor.data.NavigateProcessorContext;

/**
 * @author ashish on 6/3/17.
 */
public final class NuFlowBuilder implements FlowBuilder {

    @Override
    public Flow<NavigateProcessorContext> buildContentFinderFlow() {
        return new ContentFinderFlow();
    }

    @Override
    public Flow<NavigateProcessorContext> buildPostContentSuggestionsFlow() {
        return new PostContentSuggestionsFlow();
    }

    @Override
    public Flow<NavigateProcessorContext> buildPostLessonSuggestionsFlow() {
        return new NoopFlow();
    }

    @Override
    public Flow<NavigateProcessorContext> buildPreLessonSuggestionsFlow() {
        return new NoopFlow();
    }

    @Override
    public FlowVersion version() {
        return FlowVersion.V2_NU;
    }

}
