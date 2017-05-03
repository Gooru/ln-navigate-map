package org.gooru.navigatemap.processor.coursepath.flows.strategy.global;

import org.gooru.navigatemap.processor.coursepath.flows.Flow;
import org.gooru.navigatemap.processor.coursepath.flows.FlowBuilder;
import org.gooru.navigatemap.processor.data.NavigateProcessorContext;

/**
 * @author ashish on 6/3/17.
 */
public final class GlobalFlowBuilder implements FlowBuilder {

    public Flow<NavigateProcessorContext> buildContentFinderFlow() {
        return new ContentFinderFlow();
    }

    public Flow<NavigateProcessorContext> buildPostContentSuggestionsFlow() {
        return new PostContentSuggestionsFlow();
    }

    public Flow<NavigateProcessorContext> buildPostLessonSuggestionsFlow() {
        return new PostLessonSuggestionsFlow();
    }

    public Flow<NavigateProcessorContext> buildPreLessonSuggestionsFlow() {
        return new PreLessonSuggestionsFlow();
    }

}
