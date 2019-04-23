package org.gooru.navigatemap.infra.utilities.fwfinder;

import org.gooru.navigatemap.infra.data.RequestContext;
import org.skife.jdbi.v2.DBI;

/**
 * @author ashish.
 */

class FrameworkFinderImpl implements FrameworkFinder {

  private final DBI dbi;
  private final RequestContext requestContext;
  private FrameworkFinderDao dao;

  FrameworkFinderImpl(DBI dbi, RequestContext requestContext) {
    this.dbi = dbi;
    this.requestContext = requestContext;
  }


  @Override
  public String findFramework() {
    // Currently we only support it for class context
    if (requestContext.getClassId() != null) {
      return fetchDao().findFrameworkForClass(requestContext.getClassId());
    }
    return null;
  }

  private FrameworkFinderDao fetchDao() {
    if (dao == null) {
      dao = dbi.onDemand(FrameworkFinderDao.class);
    }
    return dao;
  }

}
