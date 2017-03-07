package org.gooru.navigatemap.processor.coursepath.repositories;

/**
 * @author ashish on 3/3/17.
 */
public final class ContentRepositoryBuilder {

    private ContentRepositoryBuilder() {
        throw new AssertionError();
    }

    public static ContentFinderRepository buildContentFinderService() {
        return new ContentFinderRepositoryImpl();
    }
}
