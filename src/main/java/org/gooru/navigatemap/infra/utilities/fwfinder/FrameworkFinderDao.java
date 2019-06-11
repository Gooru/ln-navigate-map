package org.gooru.navigatemap.infra.utilities.fwfinder;

import java.util.UUID;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.SqlQuery;

/**
 * @author ashish.
 */

interface FrameworkFinderDao {

  @SqlQuery("select fw_code from grade_master where id = (select grade_current from class where id = :classId)")
  String findFrameworkForClass(@Bind("classId") UUID classId);

}
