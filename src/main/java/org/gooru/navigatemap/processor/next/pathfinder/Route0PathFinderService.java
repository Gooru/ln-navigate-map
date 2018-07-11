package org.gooru.navigatemap.processor.next.pathfinder;

import org.skife.jdbi.v2.DBI;

/**
 * @author ashish on 11/7/18.
 */
class Route0PathFinderService implements PathFinder {

    private final DBI dbi;

    Route0PathFinderService(DBI dbi) {
        this.dbi = dbi;
    }

    @Override
    public PathFinderResult findPath(PathFinderContext context) {
        // TODO: Provide implementation
        return null;
    }
}
