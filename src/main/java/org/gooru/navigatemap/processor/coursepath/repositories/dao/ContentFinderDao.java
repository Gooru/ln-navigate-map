package org.gooru.navigatemap.processor.coursepath.repositories.dao;

import org.gooru.navigatemap.processor.coursepath.repositories.mappers.ContentAddressMapper;
import org.gooru.navigatemap.processor.data.ContentAddress;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.customizers.Mapper;

/**
 * @author ashish on 7/3/17.
 */
public interface ContentFinderDao {

    @SqlQuery("select course_id, unit_id, lesson_id, id, format, subformat from collection c where course_id = "
                  + ":courseId::uuid and unit_id = (select unit_id from unit where course_id = :courseId::uuid and "
                  + "is_deleted = false order by sequence_id asc limit 1) and lesson_id =   (select lesson_id from "
                  + "lesson where course_id = :courseId::uuid and unit_id = c.unit_id  and is_deleted = false order "
                  + "by sequence_id asc limit 1)  order by sequence_id limit 1;")
    @Mapper(ContentAddressMapper.class)
    ContentAddress findFirstContentInCourse(@Bind("courseId") String courseId);
}
