package org.gooru.navigatemap.processor.coursepath.repositories;

/**
 * @author ashish on 8/5/17.
 */
public final class SuggestionServiceBuilder {
    private SuggestionServiceBuilder() {
        throw new AssertionError();
    }

    public static SuggestionCardService buildContentSuggestionsService() {
        return new SuggestionCardServiceImpl();
    }
}
