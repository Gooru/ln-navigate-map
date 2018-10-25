package org.gooru.navigatemap.processor.next.pathfinder;

import org.gooru.navigatemap.app.constants.HttpConstants;
import org.gooru.navigatemap.app.exceptions.HttpResponseWrapperException;
import org.gooru.navigatemap.infra.data.ContentAddress;
import org.skife.jdbi.v2.DBI;

/**
 * @author ashish on 11/5/18.
 */
class AlternatePathUnawareSpecifiedPathContentFinder implements ContentFinder {

  private final DBI dbi;
  private ContentFinderDao finderDao;
  private PathFinderContext context;

  AlternatePathUnawareSpecifiedPathContentFinder(DBI dbi) {
    this.dbi = dbi;
  }

  @Override
  public ContentAddress findContent(PathFinderContext context) {
    validateCULValues(context.getContentAddress());
    finderDao = dbi.onDemand(ContentFinderDao.class);
    this.context = context;
    return fetchSpecifiedContentFromCoursePath();
  }

  private ContentAddress fetchSpecifiedContentFromCoursePath() {
    ContentAddress specifiedContentAddress = context.getContentAddress();
    return finderDao
        .findCULC(specifiedContentAddress.getCourse(), specifiedContentAddress.getUnit(),
            specifiedContentAddress.getLesson(), specifiedContentAddress.getCollection());
  }

  private void validateCULValues(ContentAddress contentAddress) {
    if (contentAddress.getCourse() == null || contentAddress.getUnit() == null
        || contentAddress.getLesson() == null) {
      throw new HttpResponseWrapperException(HttpConstants.HttpStatus.BAD_REQUEST,
          "Invalid CUL info in context");
    }
  }

}
