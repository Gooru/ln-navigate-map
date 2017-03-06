package org.gooru.navigatemap.processor.coursepath.flows;

/**
 * @author ashish on 6/3/17.
 */
final class FlowBuilder {

    private FlowBuilder() {
        throw new AssertionError();
    }

    static Flow buildContentFinderFlow() {
        return new ContentFinderFlow();
    }

    static Flow buildContentServeFlow() {
        return new ContentServeFlow();
    }

    static Flow buildPostContentSuggestionsFlow() {
        return new PostContentSuggestionsFlow();
    }

    static Flow buildPostLessonSuggestionsFlow() {
        return new PostLessonSuggestionsFlow();
    }

    static Flow buildPreLessonSuggestionsFlow() {
        return new PreLessonSuggestionsFlow();
    }

}
