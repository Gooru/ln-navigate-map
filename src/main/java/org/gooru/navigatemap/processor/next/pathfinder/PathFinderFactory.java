package org.gooru.navigatemap.processor.next.pathfinder;

import org.gooru.navigatemap.infra.data.context.RouteContextData;
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

  static PathFinder buildSuggestionsAwarePathFinderService(RouteContextData routeContextData) {
    return new SuggestionsAwarePathFinderService(DBICreator.getDbiForDefaultDS(), routeContextData);
  }

  static PathFinder buildSpecifiedItemFinderService() {
    return new SpecifiedItemFinderService(DBICreator.getDbiForDefaultDS());
  }

  static PathFinder buildPostSuggestionItemFinderService() {
    return new PostSuggestionItemFinderService(DBICreator.getDbiForDefaultDS());
  }

  static PathFinder buildCourseStartPathFinderService() {
    return new CourseStartPathFinderService(DBICreator.getDbiForDefaultDS());
  }

  static PathFinder buildRoute0PathFinderService() {
    return new Route0PathFinderService(DBICreator.getDbiForDefaultDS());
  }

  public static PathFinder buildRoute0ExplicitStartPathFinderService() {
    return new Route0ExplicitStartPathFinderService(DBICreator.getDbiForDefaultDS());
  }
}
