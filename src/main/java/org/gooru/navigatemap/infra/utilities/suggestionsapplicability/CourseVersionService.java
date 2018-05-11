package org.gooru.navigatemap.infra.utilities.suggestionsapplicability;

import java.util.UUID;

import org.skife.jdbi.v2.DBI;

/**
 * @author ashish on 2/5/18.
 */
class CourseVersionService {

    private CourseVersionDao courseVersionDao;
    private final DBI dbi;

    CourseVersionService(DBI dbi) {
        this.dbi = dbi;
    }

    String findCourseVersion(UUID course) {
        courseVersionDao = dbi.onDemand(CourseVersionDao.class);

        return courseVersionDao.findCourseVersion(course.toString());
    }

}
