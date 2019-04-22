package org.gooru.navigatemap.infra.utilities.milestoneapplicability;

import org.gooru.navigatemap.infra.data.NavigateProcessorContext;
import org.gooru.navigatemap.infra.data.RequestContext;
import org.gooru.navigatemap.infra.utilities.jdbi.DBICreator;
import org.skife.jdbi.v2.DBI;

/**
 * @author ashish.
 */

public interface MilestoneApplicabilityVerifier {

  boolean isMilestoneViewApplicable();

  static MilestoneApplicabilityVerifier build(RequestContext requestContext) {
    return new MilestoneApplicabilityVerifierImpl(DBICreator.getDbiForDefaultDS(), requestContext);
  }

  static MilestoneApplicabilityVerifier build(RequestContext requestContext, DBI dbi) {
    return new MilestoneApplicabilityVerifierImpl(dbi, requestContext);
  }

}
