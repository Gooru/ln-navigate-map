package org.gooru.navigatemap.processor.next.pathfinder;

import org.skife.jdbi.v2.DBI;

/**
 * This is a finder which is called in Next flow when system last suggested some content to user
 * however, user did not accept it. Note that in case of acceptance, caller would need to "start"
 * that specific content. In this sense it is similar to {@link StraightPathFinderService}, however,
 * this may extend in future hence pulling it out for proper organization of code with respect to
 * events in flow.
 *
 * @author ashish.
 */
class PostSuggestionItemFinderService implements PathFinder {

  private final DBI dbi;

  PostSuggestionItemFinderService(DBI dbi) {
    this.dbi = dbi;
  }

  @Override
  public PathFinderResult findPath(PathFinderContext context) {
    return new PathFinderResult(ContentFinderFactory
        .buildAlternatePathUnawareMainPathContentFinder(dbi,
            ContentFinderCriteria.CRITERIA_NON_SKIPPABLE)
        .findContent(context));
  }
}
