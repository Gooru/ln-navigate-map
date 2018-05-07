package org.gooru.navigatemap.processor.next.pathfinder;

import org.skife.jdbi.v2.DBI;

/**
 * @author ashish on 7/5/18.
 */
final class ContentFinderFactory {

    static ContentFinder buildAlternatePathUnawareMainPathContentFinder(DBI dbi, ContentFinderCriteria criteria) {
        return new AlternatePathUnawareMainPathContentFinder(dbi, criteria);
    }

    static ContentFinder buildTeacherPathContentFinder(DBI dbi) {
        return new TeacherPathContentFinder(dbi);
    }

    static ContentFinder buildTeacherPathAwareMainPathContentFinder(DBI dbi) {
        return new TeacherPathAwareMainPathContentFinder(dbi);
    }

}
