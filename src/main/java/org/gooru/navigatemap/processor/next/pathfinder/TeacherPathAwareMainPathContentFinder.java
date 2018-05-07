package org.gooru.navigatemap.processor.next.pathfinder;

import org.gooru.navigatemap.infra.data.ContentAddress;
import org.skife.jdbi.v2.DBI;

/**
 * This class encapsulates the logic to find next content on main path which is both visible and non skippable. If such
 * content exists, then if there is a teacher path associated with that content, teacher path is returned. if there
 * is no teacher path, then content itself is returned. If there is no content, no content address is populated in
 * result.
 *
 * @author ashish on 7/5/18.
 */
class TeacherPathAwareMainPathContentFinder extends AbstractContentFinder {

    TeacherPathAwareMainPathContentFinder(DBI dbi) {
        super(dbi);
    }

    @Override
    public ContentAddress findContent(PathFinderContext context) {
        return null;
    }
}
