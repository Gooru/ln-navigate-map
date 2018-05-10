package org.gooru.navigatemap.processor.next.pathfinder;

import org.skife.jdbi.v2.DBI;

/**
 * @author ashish on 10/5/18.
 */
final class SuggestionFinderBuilder {

    private SuggestionFinderBuilder() {
        throw new AssertionError();
    }

    static SuggestionFinder buildSuggestionFinder(DBI dbi) {
        throw new IllegalStateException("Not implemented");
    }
}
