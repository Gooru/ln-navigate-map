package org.gooru.navigatemap.infra.utilities.preferredlanguagefinder;

import java.util.UUID;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.SqlQuery;

/**
 * @author ashish.
 */

interface PreferredLanguageFinderDao {

  @SqlQuery("select primary_language from class where id = :classId")
  Integer fetchPrimaryLanguageIdFromClass(@Bind("classId") UUID classId);

}
