package org.gooru.navigatemap.processor.coursepath.repositories.dao;

import java.util.List;

import org.gooru.navigatemap.processor.coursepath.repositories.mappers.ContentAddressMapper;
import org.gooru.navigatemap.processor.data.ContentAddress;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.customizers.Mapper;

/**
 * @author ashish on 7/3/17.
 */
public interface ContentFinderDao {

    @SqlQuery("select course_id, unit_id, lesson_id, id, format, subformat, null as path_id from collection c where "
                  + "course_id = :courseId::uuid and unit_id = (select unit_id from unit where course_id = "
                  + ":courseId::uuid and is_deleted = false order by sequence_id asc limit 1) and lesson_id =   "
                  + "(select lesson_id from lesson where course_id = :courseId::uuid and unit_id = c.unit_id  and "
                  + "is_deleted = false order by sequence_id asc limit 1)  order by sequence_id limit 1;")
    @Mapper(ContentAddressMapper.class)
    ContentAddress findFirstContentInCourse(@Bind("courseId") String courseId);

    @SqlQuery("select unit_id from unit where course_id = :courseId::uuid and is_deleted = false order by sequence_id "
                  + "asc")
    List<String> findUnitsInCourse(@Bind("courseId") String courseId);

    @SqlQuery("select lesson_id from lesson where course_id = :courseId::uuid and unit_id = :unitId::uuid and "
                  + "is_deleted = false order by sequence_id asc")
    List<String> findLessonsInCU(@Bind("courseId") String courseId, @Bind("unitId") String unitId);

    @SqlQuery("select course_id, unit_id, lesson_id, id, format, subformat, null as path_id from collection c where "
                  + "course_id = :courseId::uuid and unit_id = :unitId::uuid and  lesson_id = :lessonId::uuid and "
                  + "is_deleted = false order by sequence_id asc")
    @Mapper(ContentAddressMapper.class)
    List<ContentAddress> findCollectionsInCUL(@Bind("courseId") String courseId, @Bind("unitId") String unitId,
        @Bind("lessonId") String lessonId);

    @SqlQuery("select unit_id from unit where course_id = :courseId::uuid and is_deleted = false and sequence_id >= "
                  + "(select sequence_id from unit where course_id = :courseId::uuid and unit_id = :unitId::uuid) "
                  + "order by sequence_id ;")
    List<String> findNextUnitsInCourse(@Bind("courseId") String courseId, @Bind("unitId") String unitId);

    @SqlQuery("select lesson_id from lesson where course_id = :courseId::uuid and unit_id = :unitId::uuid and "
                  + "is_deleted = false and sequence_id >= (select sequence_id from lesson where course_id = "
                  + ":courseId::uuid and unit_id = :unitId::uuid and lesson_id = :lessonId::uuid) order by "
                  + "sequence_id ;")
    List<String> findNextLessonsInCU(@Bind("courseId") String courseId, @Bind("unitId") String unitId,
        @Bind("lessonId") String lessonId);

    @SqlQuery("select course_id, unit_id, lesson_id, id, format, subformat, null as path_id from collection c where "
                  + "course_id = :courseId::uuid and unit_id = :unitId::uuid and  lesson_id = :lessonId::uuid and "
                  + "is_deleted = false and sequence_id > (select sequence_id from collection where course_id = "
                  + ":courseId::uuid and unit_id = :unitId::uuid and  lesson_id = :lessonId::uuid and id = "
                  + ":collectionId::uuid) order by sequence_id asc")
    @Mapper(ContentAddressMapper.class)
    List<ContentAddress> findNextCollectionsInCUL(@Bind("courseId") String courseId, @Bind("unitId") String unitId,
        @Bind("lessonId") String lessonId, @Bind("collectionId") String collectionId);

    @SqlQuery("select course_id, unit_id, lesson_id, id, format, subformat, null as path_id from collection c where "
                  + "course_id = :courseId::uuid and unit_id = :unitId::uuid and  lesson_id = :lessonId::uuid and "
                  + "is_deleted = false order by sequence_id asc limit 1")
    @Mapper(ContentAddressMapper.class)
    List<ContentAddress> findFirstCollectionInLesson(@Bind("courseId") String courseId, @Bind("unitId") String unitId,
        @Bind("lessonId") String lessonId);

    @SqlQuery("select taxonomy from lesson where course_id = :courseId::uuid and unit_id = :unitId::uuid and "
                  + "lesson_id = :lessonId::uuid and is_deleted = false")
    String findCompetenciesForLesson(@Bind("courseId") String course, @Bind("unitId") String unit,
        @Bind("lessonId") String lesson);

    @SqlQuery("select count(*) from collection where course_id = :courseId::uuid and unit_id = :unitId::uuid and  "
                  + "lesson_id = :lessonId::uuid and id = :collectionId::uuid and is_deleted = false")
    long validateCULC(@Bind("courseId") String courseId, @Bind("unitId") String unitId,
        @Bind("lessonId") String lessonId, @Bind("collectionId") String collectionId);

    @SqlQuery("select count(*) from collection where course_id = :courseId::uuid and unit_id = :unitId::uuid and  "
                  + "lesson_id = :lessonId::uuid and is_deleted = false")
    long validateCUL(@Bind("courseId") String courseId, @Bind("unitId") String unitId,
        @Bind("lessonId") String lessonId);

    @SqlQuery("select version from course where id = :courseId::uuid")
    String findCourseVersion(@Bind("courseId") String courseId);

}
