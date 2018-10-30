package org.gooru.navigatemap.processor.next.pathfinder;

import java.util.List;
import org.gooru.navigatemap.infra.data.AlternatePath;
import org.gooru.navigatemap.infra.data.ContentAddress;
import org.skife.jdbi.v2.DBI;

/**
 * This class encapsulates the logic to find the next content item on teacher path. If there is such
 * an item, then that item is returned.
 *
 * @author ashish on 7/5/18.
 */
class TeacherPathContentFinder implements ContentFinder {

  private final DBI dbi;
  private AlternatePathDao alternatePathDao;

  TeacherPathContentFinder(DBI dbi) {
    this.dbi = dbi;
  }

  @Override
  public ContentAddress findContent(PathFinderContext context) {
    if (context.getClassId() == null) {
      return null;
    }
    List<AlternatePath> teacherPathsForContext = getAlternatePathDao()
        .findNextTeacherPathsForSpecifiedContext(context.getContentAddress(), context.getUserId(),
            context.getClassId().toString());
    if (teacherPathsForContext == null || teacherPathsForContext.isEmpty()) {
      return null;
    } else {
      return teacherPathsForContext.get(0).toContentAddress();
    }
  }

  private AlternatePathDao getAlternatePathDao() {
    if (alternatePathDao == null) {
      alternatePathDao = dbi.onDemand(AlternatePathDao.class);
    }
    return alternatePathDao;
  }
}
