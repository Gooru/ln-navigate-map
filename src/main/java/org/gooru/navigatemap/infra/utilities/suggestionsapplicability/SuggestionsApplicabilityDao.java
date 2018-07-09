package org.gooru.navigatemap.infra.utilities.suggestionsapplicability;

import java.util.UUID;

import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.SqlQuery;

/**
 * @author ashish on 2/5/18.
 */
interface SuggestionsApplicabilityDao {

    @SqlQuery("select course_id from class where id = :classId")
    String fetchCourseForClass(@Bind("classId") UUID classId);

    @SqlQuery("select version from course where id = :courseId")
    String fetchCourseVersion(@Bind("courseId") UUID courseId);

    @SqlQuery("select version from course where id = :courseId::uuid")
    String fetchCourseVersion(@Bind("courseId") String courseId);
    
}
