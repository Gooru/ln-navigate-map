package org.gooru.navigatemap.processor.coursepath.repositories;

/**
 * @author ashish on 3/3/17.
 */
public final class ContentRepositoryBuilder {

    private ContentRepositoryBuilder() {
        throw new AssertionError();
    }

    public static ContentFinderRepository buildContentFinderRepository() {
        return new ContentFinderRepositoryImpl();
    }

    public static ContentFilterRepository buildContentFilterRepository() {
        return new ContentFilterRepositoryImpl();
    }

    public static ContentSuggestionsService buildContentSuggestionsService() {
        return new ContentSuggestionsServiceImpl(buildContentFilterRepository(), buildContentFinderRepository());
    }

    public static NavigateService buildNavigateService() {
        return new NavigateServiceImpl(buildContentFilterRepository(), buildContentFinderRepository());
    }
}
