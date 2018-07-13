package org.gooru.navigatemap.processor.next.pathfinder;

import java.util.List;

import org.gooru.navigatemap.infra.data.AlternatePath;
import org.gooru.navigatemap.infra.data.ContentAddress;
import org.skife.jdbi.v2.DBI;

/**
 * This class encapsulates the logic to find next content on main path which is defined by Criteria. If such
 * content exists, then if there is a teacher path associated with that content, teacher path is returned. if there
 * is no teacher path, then content itself is returned. If there is no content, no content address is populated in
 * result.
 *
 * @author ashish on 7/5/18.
 */
class TeacherPathAwareMainPathContentFinder implements ContentFinder {

    private final ContentFinderCriteria contentFinderCriteria;
    private final DBI dbi;
    private AlternatePathDao alternatePathDao;

    TeacherPathAwareMainPathContentFinder(DBI dbi, ContentFinderCriteria contentFinderCriteria) {
        this.dbi = dbi;
        this.contentFinderCriteria = contentFinderCriteria;
    }

    @Override
    public ContentAddress findContent(PathFinderContext context) {
        ContentAddress result =
            ContentFinderFactory.buildAlternatePathUnawareMainPathContentFinder(dbi, contentFinderCriteria)
                .findContent(context);

        if (context.getClassId() != null) {
            List<AlternatePath> teacherPathsForContext = getAlternatePathDao()
                .findTeacherPathsForSpecifiedContext(result, context.getUserId(), context.getClassId().toString());
            if (teacherPathsForContext != null && !teacherPathsForContext.isEmpty()) {
                return teacherPathsForContext.get(0).toContentAddress();
            }
        }
        return result;
    }

    protected AlternatePathDao getAlternatePathDao() {
        if (alternatePathDao == null) {
            alternatePathDao = dbi.onDemand(AlternatePathDao.class);
        }
        return alternatePathDao;
    }


}
