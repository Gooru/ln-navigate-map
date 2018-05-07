package org.gooru.navigatemap.processor.next.pathfinder;

import org.gooru.navigatemap.infra.data.CurrentItemType;
import org.skife.jdbi.v2.DBI;

// @formatter:off
/**
 *
 * In a nutshell, here is what we are trying to do. Note that we always enter here with content served state. The
 * suggestions state demands that caller converts it to start.
 *
 * - handle content-served state
 *     - if on main path
 *         - if assessment
 *             - if passed,
 *                 - update competency completion (track it)
 *                 - if signature item present for competency apply suggestion (track it)
 *                 - else loadNextItemFromMainPath
 *             - else
 *                 - if signature coll present for competency apply suggestion (track it)
 *                 - else loadNextItemFromMainPath
 *         - else
 *             - loadNextItemFromMainPath
 *     - if on teacher path
 *         - if assessment
 *             - if passed,
 *                 - update competency completion (track it)
 *                 - loadNextItemFromTeacherPath
 *             - else
 *                 - loadNextItemFromTeacherPath
 *         - else
 *             - loadNextItemFromTeacherPath
 *     - if on system path
 *         - if assessment (note that assessment won't be loaded with start flow as they won't be present on course map)
 *             - if passed,
 *                 - update competency mastery as it would be signature item (track it)
 *                 - loadNextItemFromMainPath
 *             - else
 *                 - if signature coll present for competency apply suggestion (track it)
 *                 - else continue
 *         - if collection
 *             - if there is any other collection present on system path for that context, load that
 *             - else loadNextItemFromMainPath
 *
 * - loadNextItemFromTeacherPath
 *     - if there is next item on teacher path, load
 *     - else loadNextItemFromMainPath
 *
 * - loadNextItemFromMainPath
 *     - find next item on main path considering skip and visibility
 *     - if there is an item found
 *         - if there a teacher path, load first item from that teacher path
 *         - else load the item itself
 *     - else
 *         - mark as done
 *
 * @author ashish on 7/5/18.
 */
// @formatter:on
class SuggestionsAwarePathFinderService implements PathFinder {

    private final DBI dbi;
    private ContentFinderDao finderDao;
    private PathFinderContext context;

    SuggestionsAwarePathFinderService(DBI dbi) {
        this.dbi = dbi;
        finderDao = dbi.onDemand(ContentFinderDao.class);
    }

    @Override
    public PathFinderResult findPath(PathFinderContext context) {
        this.context = context;
        // TODO provide implementation
        throw new IllegalStateException("Not implemented");
    }

    private PathFinderResult process() {
        if (!context.getContentAddress().isOnAlternatePath()) {
            if (context.getContentAddress().getCurrentItemType() == CurrentItemType.Assessment) {

            } else {
                return loadNextItemFromMainpath();
            }
        } else {

        }
        throw new IllegalStateException("Not implemented");
    }

    private PathFinderResult loadNextItemFromMainpath() {
        return null;
    }
}
