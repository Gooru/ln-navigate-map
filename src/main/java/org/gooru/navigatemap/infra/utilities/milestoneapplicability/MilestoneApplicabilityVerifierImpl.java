package org.gooru.navigatemap.infra.utilities.milestoneapplicability;

import org.gooru.navigatemap.infra.data.RequestContext;
import org.skife.jdbi.v2.DBI;

/**
 * @author ashish.
 */

class MilestoneApplicabilityVerifierImpl implements MilestoneApplicabilityVerifier {

  private final DBI dbi;
  private final RequestContext requestContext;
  private MilestoneApplicabilityDao dao;

  MilestoneApplicabilityVerifierImpl(DBI dbi, RequestContext requestContext) {

    this.dbi = dbi;
    this.requestContext = requestContext;
  }

  @Override
  public boolean isMilestoneViewApplicable() {
    if (requestContext.isContextInClass()) {
      Boolean result = fetchDao()
          .isMilestoneViewApplicableForSpecifiedClass(requestContext.getClassId());
      return (result != null ? result : false);
    } else {
      // For IL we do not support it yet
      return false;
    }
  }

  private MilestoneApplicabilityDao fetchDao() {
    if (dao == null) {
      dao = dbi.onDemand(MilestoneApplicabilityDao.class);
    }
    return dao;
  }
}
