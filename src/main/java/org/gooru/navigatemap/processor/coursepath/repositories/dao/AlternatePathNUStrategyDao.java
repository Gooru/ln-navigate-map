package org.gooru.navigatemap.processor.coursepath.repositories.dao;

import java.util.List;
import java.util.UUID;

import org.gooru.navigatemap.processor.coursepath.repositories.mappers.AlternatePathMapper;
import org.gooru.navigatemap.processor.coursepath.repositories.mappers.JavaSqlArrayToStringMapper;
import org.gooru.navigatemap.processor.data.AlternatePath;
import org.gooru.navigatemap.processor.utilities.jdbi.PGArray;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.customizers.Mapper;

/**
 * @author ashish on 8/5/17.
 */
public interface AlternatePathNUStrategyDao {

    @SqlQuery("select count(*) from user_navigation_paths where id = :pathId and target_resource_id = "
                  + ":resourceId::uuid and target_content_type = 'resource' ")
    long validatePath(@Bind("pathId") Long pathId, @Bind("resourceId") String resource);

    @Mapper(AlternatePathMapper.class)
    @SqlQuery("select id, ctx_user_id, ctx_class_id, ctx_course_id, ctx_unit_id, ctx_lesson_id, "
                  + "ctx_collection_id, parent_path_id, parent_path_type, target_course_id, target_unit_id, "
                  + "target_lesson_id, target_collection_id, target_content_type, target_content_subtype, "
                  + "target_resource_id from user_navigation_paths where ctx_course_id = :courseId::uuid and "
                  + "ctx_unit_id = :unitId::uuid and ctx_lesson_id = :lessonId::uuid and ctx_collection_id = "
                  + ":collectionId::uuid and ctx_user_id = :userId::uuid and ctx_class_id = :classId::uuid and "
                  + "target_content_type = 'resource' order by created_at desc")
    List<AlternatePath> findResourceAlternatePathsForCULAndUserInClass(@Bind("courseId") String course,
        @Bind("unitId") String unit, @Bind("lessonId") String lesson, @Bind("collectionId") String collection,
        @Bind("userId") String user, @Bind("classId") String classId);

    @Mapper(AlternatePathMapper.class)
    @SqlQuery("select id, ctx_user_id, ctx_class_id, ctx_course_id, ctx_unit_id, ctx_lesson_id, "
                  + "ctx_collection_id, parent_path_id, parent_path_type, target_course_id, target_unit_id, "
                  + "target_lesson_id, target_collection_id, target_content_type, target_content_subtype, "
                  + "target_resource_id from user_navigation_paths where ctx_course_id = :courseId::uuid and "
                  + "ctx_unit_id = :unitId::uuid and ctx_lesson_id = :lessonId::uuid and ctx_collection_id = "
                  + ":collectionId::uuid and ctx_user_id = :userId::uuid and ctx_class_id is null and "
                  + "target_content_type = 'resource' order by created_at desc")
    List<AlternatePath> findResourceAlternatePathsForCULAndUser(@Bind("courseId") String course,
        @Bind("unitId") String unit, @Bind("lessonId") String lesson, @Bind("collectionId") String collection,
        @Bind("userId") String user);

    @SqlQuery("select ids_to_suggest from concept_based_resource_suggest where (competency_internal_code = any"
                  + "(:competencyList) OR micro_competency_internal_code = any(:competencyList)) and "
                  + "performance_range = :scoreRange")
    @Mapper(JavaSqlArrayToStringMapper.class)
    List<String[]> findResourceSuggestionsBasedOnCompetencyAndScoreRange(
        @Bind("competencyList") PGArray<String> competencyList, @Bind("scoreRange") String scoreRange);

    @SqlQuery(" select target_resource_id from user_navigation_paths where ctx_user_id = :user::uuid and "
                  + "ctx_course_id = :course::uuid and ctx_class_id = :userClass::uuid and target_resource_id = any"
                  + "(:resourceList)")
    List<String> findResourceAlreadyAddedFromListInCourseClass(@Bind("user") String user,
        @Bind("resourceList") PGArray<UUID> resourceList, @Bind("course") String course,
        @Bind("userClass") String userClass);

    @SqlQuery(" select target_resource_id from user_navigation_paths where ctx_user_id = :user::uuid and "
                  + "ctx_course_id = :course::uuid and ctx_class_id is null and target_resource_id = any"
                  + "(:resourceList)")
    List<String> findResourceAlreadyAddedFromListInCourseNoClass(@Bind("user") String user,
        @Bind("resourceList") PGArray<UUID> resourceList, @Bind("course") String course);
}

