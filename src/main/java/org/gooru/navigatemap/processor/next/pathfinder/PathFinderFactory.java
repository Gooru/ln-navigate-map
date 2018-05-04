package org.gooru.navigatemap.processor.next.pathfinder;

import org.gooru.navigatemap.infra.utilities.jdbi.DBICreator;

/**
 * @author ashish on 4/5/18.
 */
final class PathFinderFactory {

    private PathFinderFactory() {
        throw new AssertionError();
    }

    public static StraightPathFinderService buildStraightPathFinderService() {
        return new StraightPathFinderService(DBICreator.getDbiForDefaultDS());
    }
}
