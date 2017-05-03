package org.gooru.navigatemap.processor.coursepath.flows;

import org.gooru.navigatemap.processor.data.NavigateProcessorContext;

/**
 * @author ashish on 3/5/17.
 */
public interface FlowBuilder {

    Flow<NavigateProcessorContext> buildContentFinderFlow();

    Flow<NavigateProcessorContext> buildPostContentSuggestionsFlow();

    Flow<NavigateProcessorContext> buildPostLessonSuggestionsFlow();

    Flow<NavigateProcessorContext> buildPreLessonSuggestionsFlow();
}
