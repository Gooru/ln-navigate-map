package org.gooru.navigatemap.processor.next.pathfinder;

import java.util.List;

import org.gooru.navigatemap.infra.data.ContentAddress;
import org.gooru.navigatemap.infra.utilities.jdbi.SqlArrayMapper;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.customizers.Mapper;

/**
 * @author ashish
 */
public interface ContentFinderDao {

    @SqlQuery("select unit_id from unit where course_id = :courseId::uuid and is_deleted = false order by sequence_id "
                  + "asc")
    List<String> findUnitsInCourse(@Bind("courseId") String courseId);

    @SqlQuery("select lesson_id from lesson where course_id = :courseId::uuid and unit_id = :unitId::uuid and "
                  + "is_deleted = false order by sequence_id asc")
    List<String> findLessonsInCU(@Bind("courseId") String courseId, @Bind("unitId") String unitId);

    @SqlQuery("select course_id, unit_id, lesson_id, id, format, subformat, null as path_id, class_visibility as "
                  + "visibility from collection c where course_id = :courseId::uuid and unit_id = :unitId::uuid and  "
                  + "lesson_id = :lessonId::uuid and is_deleted = false order by sequence_id asc")
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

    @SqlQuery("select course_id, unit_id, lesson_id, id, format, subformat, null as path_id, class_visibility as "
                  + "visibility from collection c where course_id = :courseId::uuid and unit_id = :unitId::uuid and  "
                  + "lesson_id = :lessonId::uuid and is_deleted = false and sequence_id > (select sequence_id from "
                  + "collection where course_id = :courseId::uuid and unit_id = :unitId::uuid and  lesson_id = "
                  + ":lessonId::uuid and id = :collectionId::uuid) order by sequence_id asc")
    @Mapper(ContentAddressMapper.class)
    List<ContentAddress> findNextCollectionsInCUL(@Bind("courseId") String courseId, @Bind("unitId") String unitId,
        @Bind("lessonId") String lessonId, @Bind("collectionId") String collectionId);

    @SqlQuery("select course_id, unit_id, lesson_id, id, format, subformat, null as path_id, class_visibility as "
                  + "visibility from collection c where course_id = :courseId::uuid and unit_id = :unitId::uuid and  "
                  + "lesson_id = :lessonId::uuid and id = :collectionId::uuid and is_deleted = false ")
    @Mapper(ContentAddressMapper.class)
    ContentAddress findCULC(@Bind("courseId") String courseId, @Bind("unitId") String unitId,
        @Bind("lessonId") String lessonId, @Bind("collectionId") String collectionId);

    @SqlQuery("select course_id, unit_id, lesson_id, id, format, subformat, null as path_id, class_visibility as "
                  + "visibility from collection c where course_id = :courseId::uuid and unit_id = :unitId::uuid and  "
                  + "lesson_id = :lessonId::uuid and is_deleted = false order by sequence_id asc limit 1")
    @Mapper(ContentAddressMapper.class)
    List<ContentAddress> findFirstCollectionInLesson(@Bind("courseId") String courseId, @Bind("unitId") String unitId,
        @Bind("lessonId") String lessonId);

    @Mapper(SqlArrayMapper.class)
    @SqlQuery("select gut_codes from collection where course_id = :courseId::uuid and unit_id = :unitId::uuid and "
                  + "lesson_id = :lessonId::uuid and id = :collectionId::uuid and is_deleted = false")
    List<List<String>> findCompetenciesForCollection(@Bind("courseId") String course, @Bind("unitId") String unit,
        @Bind("lessonId") String lesson, @Bind("collectionId") String collectionId);

}
