package org.gooru.navigatemap.processor.next.pathfinder;

import org.gooru.navigatemap.app.constants.HttpConstants;
import org.gooru.navigatemap.app.exceptions.HttpResponseWrapperException;
import org.skife.jdbi.v2.DBI;

/**
 * This is encapsulation for straight path navigation when user is trying to play a collection/assessment/external
 * assessment.
 *
 * @author ashish on 11/5/18.
 */
class SpecifiedItemFinderService implements PathFinder {

    private final DBI dbi;

    SpecifiedItemFinderService(DBI dbi) {
        this.dbi = dbi;
    }

    @Override
    public PathFinderResult findPath(PathFinderContext context) {
        if (context.getContentAddress().isOnAlternatePath()) {
            throw new HttpResponseWrapperException(HttpConstants.HttpStatus.BAD_REQUEST,
                "Alternate path navigation is disabled");
        }

        return new PathFinderResult(
            ContentFinderFactory.buildAlternatePathUnawareSpecifiedPathContentFinder(dbi).findContent(context));
    }
}
