package org.gooru.navigatemap.processor.coursepath.flows.strategy.nu;

import org.gooru.navigatemap.processor.coursepath.flows.Flow;
import org.gooru.navigatemap.processor.coursepath.flows.FlowBuilder;
import org.gooru.navigatemap.processor.coursepath.flows.strategy.NoopFlow;
import org.gooru.navigatemap.processor.data.NavigateProcessorContext;

/**
 * @author ashish on 6/3/17.
 */
public final class NuFlowBuilder implements FlowBuilder {

    public Flow<NavigateProcessorContext> buildContentFinderFlow() {
        return new ContentFinderFlow();
    }

    public Flow<NavigateProcessorContext> buildPostContentSuggestionsFlow() {
        return new PostContentSuggestionsFlow();
    }

    public Flow<NavigateProcessorContext> buildPostLessonSuggestionsFlow() {
        return new NoopFlow();
    }

    public Flow<NavigateProcessorContext> buildPreLessonSuggestionsFlow() {
        return new NoopFlow();
    }

}
