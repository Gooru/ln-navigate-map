package org.gooru.navigatemap.infra.utilities.milestoneapplicability;

import java.util.UUID;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.SqlQuery;

/**
 * @author ashish.
 */

interface MilestoneApplicabilityDao {

  @SqlQuery("select milestone_view_applicable from class where id = :classId and is_deleted = false")
  Boolean isMilestoneViewApplicableForSpecifiedClass(@Bind("classId") UUID classId);
}
