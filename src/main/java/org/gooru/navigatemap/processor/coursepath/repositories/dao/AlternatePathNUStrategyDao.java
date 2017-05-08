package org.gooru.navigatemap.processor.coursepath.repositories.dao;

import java.util.List;

import org.gooru.navigatemap.processor.coursepath.repositories.mappers.AlternatePathMapper;
import org.gooru.navigatemap.processor.coursepath.repositories.mappers.JavaSqlArrayToStringMapper;
import org.gooru.navigatemap.processor.data.AlternatePath;
import org.gooru.navigatemap.processor.utilities.jdbi.PGArray;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.SqlBatch;
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
    @SqlQuery("select id, ctx_user_id, ctx_class_id, ctx_course_id, ctx_unit_id, ctx_lesson_id, ctx_class_id, "
                  + "ctx_collection_id, parent_path_id, parent_path_type, target_course_id, target_unit_id, "
                  + "target_lesson_id, target_collection_id, target_content_type, target_content_subtype, "
                  + "target_resource_id from user_navigation_paths where id = :pathId and ctx_user_id = :user::uuid "
                  + "and ctx_class_id is null")
    AlternatePath findAlternatePathByPathIdAndUser(@Bind("pathId") Long pathId, @Bind("user") String user);

    @Mapper(AlternatePathMapper.class)
    @SqlQuery("select id, ctx_user_id, ctx_class_id, ctx_course_id, ctx_unit_id, ctx_lesson_id, ctx_class_id, "
                  + "ctx_collection_id, parent_path_id, parent_path_type, target_course_id, target_unit_id, "
                  + "target_lesson_id, target_collection_id, target_content_type, target_content_subtype, "
                  + "target_resource_id from user_navigation_paths where id = :pathId and ctx_user_id = :user::uuid "
                  + "and ctx_class_id = :classId::uuid")
    AlternatePath findAlternatePathByPathIdAndUserInClass(@Bind("pathId") Long pathId, @Bind("user") String user,
        @Bind("classId") String classId);

    @SqlQuery("select comp_mcomp_id from user_competency_completion where comp_mcomp_id = any(:competencyList) "
                  + "and user_id = :userId::uuid")
    List<String> findCompletedCompetenciesForUserInGivenList(@Bind("userId") String userId,
        @Bind("competencyList") PGArray<String> competencyList);

    @SqlBatch("insert into user_competency_completion (user_id, comp_mcomp_id, ctx_course_id, ctx_unit_id, "
                  + "ctx_lesson_id, ctx_class_id, ctx_collection_id)values(:userId::uuid, :competency, :course::uuid,"
                  + " :unit::uuid, :lesson::uuid, :classId::uuid, :collection::uuid)")
    void markCompetencyCompletedInClassContext(@Bind("userId") String userId,
        @Bind("competency") List<String> competency, @Bind("course") String course, @Bind("unit") String unit,
        @Bind("lesson") String lesson, @Bind("classId") String classId, @Bind("collection") String collection);

    @SqlBatch("insert into user_competency_completion (user_id, comp_mcomp_id, ctx_course_id, ctx_unit_id, "
                  + "ctx_lesson_id, ctx_collection_id)values(:userId::uuid, :competency, :course::uuid,"
                  + " :unit::uuid, :lesson::uuid, :collection::uuid)")
    void markCompetencyCompletedNoClassContext(@Bind("userId") String userId,
        @Bind("competency") List<String> competency, @Bind("course") String course, @Bind("unit") String unit,
        @Bind("lesson") String lesson, @Bind("collection") String collection);

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
        @Bind("resourceList") PGArray<String> resourceList, @Bind("course") String course,
        @Bind("userClass") String userClass);

    @SqlQuery(" select target_resource_id from user_navigation_paths where ctx_user_id = :user::uuid and "
                  + "ctx_course_id = :course::uuid and ctx_class_id is null and target_resource_id = any"
                  + "(:resourceList)")
    List<String> findResourceAlreadyAddedFromListInCourseNoClass(@Bind("user") String user,
        @Bind("resourceList") PGArray<String> resourceList, @Bind("course") String course);
}

