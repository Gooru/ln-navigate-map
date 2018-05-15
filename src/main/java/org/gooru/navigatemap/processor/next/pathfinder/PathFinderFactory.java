package org.gooru.navigatemap.processor.next.pathfinder;

import org.gooru.navigatemap.infra.utilities.jdbi.DBICreator;

/**
 * @author ashish on 4/5/18.
 */
final class PathFinderFactory {

    private PathFinderFactory() {
        throw new AssertionError();
    }

    static PathFinder buildStraightPathFinderService() {
        return new StraightPathFinderService(DBICreator.getDbiForDefaultDS());
    }

    static PathFinder buildExplicitStartPathFinderService() {
        return new ExplicitStartPathFinderService(DBICreator.getDbiForDefaultDS());
    }

    static PathFinder buildSuggestionsAwarePathFinderService() {
        return new SuggestionsAwarePathFinderService(DBICreator.getDbiForDefaultDS());
    }

    static PathFinder buildSpecifiedItemFinderService() {
        return new SpecifiedItemFinderService(DBICreator.getDbiForDefaultDS());
    }
}
