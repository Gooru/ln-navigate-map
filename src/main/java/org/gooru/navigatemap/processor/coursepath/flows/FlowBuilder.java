package org.gooru.navigatemap.processor.coursepath.flows;

import org.gooru.navigatemap.processor.data.NavigateProcessorContext;

/**
 * @author ashish on 6/3/17.
 */
final class FlowBuilder {

    private FlowBuilder() {
        throw new AssertionError();
    }

    static Flow<NavigateProcessorContext> buildContentFinderFlow() {
        return new ContentFinderFlow();
    }

    static Flow<NavigateProcessorContext> buildPostContentSuggestionsFlow() {
        return new PostContentSuggestionsFlow();
    }

    static Flow<NavigateProcessorContext> buildPostLessonSuggestionsFlow() {
        return new PostLessonSuggestionsFlow();
    }

    static Flow<NavigateProcessorContext> buildPreLessonSuggestionsFlow() {
        return new PreLessonSuggestionsFlow();
    }

}
