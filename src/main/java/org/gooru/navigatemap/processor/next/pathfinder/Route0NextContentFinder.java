package org.gooru.navigatemap.processor.next.pathfinder;

import org.gooru.navigatemap.infra.data.ContentAddress;
import org.skife.jdbi.v2.DBI;

/**
 * @author ashish on 11/7/18.
 */
class Route0NextContentFinder implements ContentFinder {

    private final DBI dbi;

    Route0NextContentFinder(DBI dbi) {
        this.dbi = dbi;
    }

    @Override
    public ContentAddress findContent(PathFinderContext context) {
        // TODO: Provide implementation
        return null;
    }
}
