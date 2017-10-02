package org.gooru.navigatemap.processor.coursepath.repositories.dao;

import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.SqlQuery;

/**
 * @author ashish on 12/9/17.
 */
public interface ClassDao {

    @SqlQuery("select content_visibility from class where id = :classId::uuid and is_deleted = false")
    String getClassVisibility(@Bind("classId") String classId);
}
