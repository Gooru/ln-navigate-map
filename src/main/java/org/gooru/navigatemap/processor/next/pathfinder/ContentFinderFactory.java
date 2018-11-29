package org.gooru.navigatemap.processor.next.pathfinder;

import org.skife.jdbi.v2.DBI;

/**
 * @author ashish on 7/5/18.
 */
final class ContentFinderFactory {

  static ContentFinder buildAlternatePathUnawareMainPathContentFinder(DBI dbi,
      ContentFinderCriteria criteria) {
    return new AlternatePathUnawareMainPathContentFinder(dbi, criteria);
  }

  static ContentFinder buildTeacherPathContentFinder(DBI dbi) {
    return new TeacherPathContentFinder(dbi);
  }

  static ContentFinder buildTeacherPathAwareMainPathContentFinder(DBI dbi,
      ContentFinderCriteria criteria) {
    return new TeacherPathAwareMainPathContentFinder(dbi, criteria);
  }

  static ContentFinder buildAlternatePathUnawareSpecifiedPathContentFinder(DBI dbi) {
    return new AlternatePathUnawareSpecifiedPathContentFinder(dbi);
  }

  static ContentFinder buildRoute0NextContentFinder(DBI dbi, ContentVerifier verifier) {
    return new Route0NextContentFinder(dbi, verifier);
  }
}
