package org.gooru.navigatemap.infra.utilities.suggestionsapplicability;

import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.SqlQuery;

/**
 * @author ashish on 2/5/18.
 */
interface CourseVersionDao {

    @SqlQuery("select version from course where id = :courseId::uuid")
    String findCourseVersion(@Bind("courseId") String courseId);

}
