package org.gooru.navigatemap.infra.utilities.suggestionsapplicability;

import java.util.UUID;

import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.SqlQuery;

/**
 * @author ashish on 2/5/18.
 */
interface SuggestionsApplicabilityDao {

    @SqlQuery("select version from course where id = :courseId")
    String findCourseVersion(@Bind("courseId") UUID courseId);

    @SqlQuery("select setting from class where id = :classId")
    String fetchClassSetting(@Bind("classId") UUID classId);

}
