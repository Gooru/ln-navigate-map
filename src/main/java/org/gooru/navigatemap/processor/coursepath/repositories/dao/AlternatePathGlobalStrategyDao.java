package org.gooru.navigatemap.processor.coursepath.repositories.dao;

import java.util.List;
import java.util.UUID;

import org.gooru.navigatemap.processor.coursepath.repositories.mappers.AlternatePathMapper;
import org.gooru.navigatemap.processor.data.AlternatePath;
import org.gooru.navigatemap.processor.utilities.jdbi.PGArray;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.customizers.Mapper;

/**
 * @author ashish on 8/5/17.
 */
public interface AlternatePathGlobalStrategyDao {
    @SqlQuery("select competency_id from competency_assessment_map where assessment_id = :assessmentId::uuid and "
                  + "assessment_type = 'post-test'")
    List<String> findCompetenciesForPostTest(@Bind("assessmentId") String assessmentId);

    @SqlQuery("select assessment_id from competency_assessment_map where competency_id = any(:competencyList) "
                  + "and assessment_type = 'benchmark'")
    List<String> findBenchmarksForCompetencyList(@Bind("competencyList") PGArray<String> competencyList);

    @SqlQuery("select assessment_id from competency_assessment_map where competency_id = any(:competencyList) "
                  + "and assessment_type = 'pre-test'")
    List<String> findPreTestsForCompetencyList(@Bind("competencyList") PGArray<String> competencyList);

    @SqlQuery("select assessment_id from competency_assessment_map where competency_id = any (:competencyList) "
                  + "and assessment_type = 'post-test'")
    List<String> findPostTestsForCompetencyList(@Bind("competencyList") PGArray<String> competencyList);

    @SqlQuery("select suggested_content_id from user_navigation_paths where suggested_content_subtype = 'benchmark' "
                  + "and ctx_user_id = :userId::uuid and suggested_content_id = any(:assessmentList)")
    List<String> findBenchmarksAddedByUserFromList(@Bind("userId") String userId,
        @Bind("assessmentList") PGArray<UUID> assessmentList);

    @SqlQuery("select suggested_content_id from user_navigation_paths where suggested_content_subtype = 'pre-test' and "
                  + "ctx_user_id = :userId::uuid and suggested_content_id = any(:preTestsList)")
    List<String> findPreTestsAddedByUserFromList(@Bind("userId") String userId,
        @Bind("preTestsList") PGArray<UUID> preTestsList);

    @SqlQuery("select suggested_content_id from user_navigation_paths where suggested_content_subtype = 'post-test' "
                  + "and ctx_user_id = :userId::uuid and suggested_content_id = any(:postTestsList)")
    List<String> findPostTestsAddedByUserFromList(@Bind("userId") String userId,
        @Bind("postTestsList") PGArray<UUID> postTestsList);

    @SqlQuery("select count(*) from user_navigation_paths where id = :pathId and suggested_content_id = "
                  + ":collectionId::uuid and suggested_content_type = :contentType")
    long validatePath(@Bind("pathId") Long pathId, @Bind("collectionId") String collection,
        @Bind("contentType") String contentType);

    @SqlQuery("select linked_content_id from assessment_extension where assessment_id = :preTestId and "
                  + "score_range_name = :scoreRangeName")
    List<String> findBackfillsForPreTestAndScoreRange(@Bind("preTestId") UUID preTestId,
        @Bind("scoreRangeName") String scoreRangeName);

    @Mapper(AlternatePathMapper.class)
    @SqlQuery("select id, ctx_user_id, ctx_class_id, ctx_course_id, ctx_unit_id, ctx_lesson_id, ctx_collection_id, "
                  + "parent_path_id, suggested_content_id, suggested_content_type, suggested_content_subtype from "
                  + "user_navigation_paths where id = :pathId and ctx_user_id = :user::uuid and ctx_class_id is null")
    AlternatePath findAlternatePathByPathIdAndUser(@Bind("pathId") Long pathId, @Bind("user") String user);

    @Mapper(AlternatePathMapper.class)
    @SqlQuery("select id, ctx_user_id, ctx_class_id, ctx_course_id, ctx_unit_id, ctx_lesson_id, ctx_collection_id, "
                  + "parent_path_id,   suggested_content_id, suggested_content_type, suggested_content_subtype  from "
                  + "user_navigation_paths where id = :pathId and ctx_user_id = :user::uuid and ctx_class_id = "
                  + ":classId::uuid")
    AlternatePath findAlternatePathByPathIdAndUserInClass(@Bind("pathId") Long pathId, @Bind("user") String user,
        @Bind("classId") String classId);

    @Mapper(AlternatePathMapper.class)
    @SqlQuery("select id, ctx_user_id, ctx_class_id, ctx_course_id, ctx_unit_id, ctx_lesson_id, ctx_collection_id, "
                  + "parent_path_id,   suggested_content_id, suggested_content_type, suggested_content_subtype  from "
                  + "user_navigation_paths where parent_path_id = :id and suggested_content_subtype = 'benchmark' "
                  + "order by created_at desc")
    List<AlternatePath> findBASubPathsForGivenPath(@Bind("id") Long id);

    @Mapper(AlternatePathMapper.class)
    @SqlQuery("select id, ctx_user_id, ctx_class_id, ctx_course_id, ctx_unit_id, ctx_lesson_id, ctx_collection_id, "
                  + "parent_path_id suggested_content_id, suggested_content_type, suggested_content_subtype  from "
                  + "user_navigation_paths where parent_path_id = :id and suggested_content_type = 'collection' order"
                  + " by created_at desc")
    List<AlternatePath> findBackfillsSubPathsForGivenPath(@Bind("id") Long id);

    @Mapper(AlternatePathMapper.class)
    @SqlQuery("select id, ctx_user_id, ctx_class_id, ctx_course_id, ctx_unit_id, ctx_lesson_id, ctx_collection_id, "
                  + "parent_path_id,  suggested_content_id, suggested_content_type, suggested_content_subtype  from "
                  + "user_navigation_paths where ctx_course_id = :courseId::uuid and ctx_unit_id = :unitId::uuid and "
                  + "ctx_lesson_id = :lessonId::uuid and ctx_user_id = :userId::uuid and ctx_class_id = "
                  + ":classId::uuid and suggested_content_subtype = 'post-test' order by created_at desc")
    List<AlternatePath> findPostTestAlternatePathsForCULAndUserInClass(@Bind("courseId") String course,
        @Bind("unitId") String unit, @Bind("lessonId") String lesson, @Bind("userId") String user,
        @Bind("classId") String classId);

    @Mapper(AlternatePathMapper.class)
    @SqlQuery("select id, ctx_user_id, ctx_class_id, ctx_course_id, ctx_unit_id, ctx_lesson_id, ctx_collection_id, "
                  + "parent_path_id,  suggested_content_id, suggested_content_type, suggested_content_subtype  from "
                  + "user_navigation_paths where ctx_course_id = :courseId::uuid and ctx_unit_id = :unitId::uuid and "
                  + "ctx_lesson_id = :lessonId::uuid and ctx_user_id = :userId::uuid and ctx_class_id is null and "
                  + "suggested_content_subtype = 'post-test' order by created_at desc")
    List<AlternatePath> findPostTestAlternatePathsForCULAndUser(@Bind("courseId") String course,
        @Bind("unitId") String unit, @Bind("lessonId") String lesson, @Bind("userId") String user);

    @Mapper(AlternatePathMapper.class)
    @SqlQuery("select id, ctx_user_id, ctx_class_id, ctx_course_id, ctx_unit_id, ctx_lesson_id, ctx_collection_id, "
                  + "parent_path_id, suggested_content_id, suggested_content_type, suggested_content_subtype  from "
                  + "user_navigation_paths where ctx_course_id = :courseId::uuid and ctx_unit_id = :unitId::uuid and "
                  + "ctx_lesson_id = :lessonId::uuid and ctx_user_id = :userId::uuid and ctx_class_id = "
                  + ":classId::uuid and suggested_content_subtype = 'pre-test' order by created_at desc")
    List<AlternatePath> findPreTestAlternatePathsForCULAndUserInClass(@Bind("courseId") String course,
        @Bind("unitId") String unit, @Bind("lessonId") String lesson, @Bind("userId") String user,
        @Bind("classId") String classId);

    @Mapper(AlternatePathMapper.class)
    @SqlQuery("select id, ctx_user_id, ctx_class_id, ctx_course_id, ctx_unit_id, ctx_lesson_id, ctx_collection_id, "
                  + "parent_path_id,  suggested_content_id, suggested_content_type, suggested_content_subtype  from "
                  + "user_navigation_paths where ctx_course_id = :courseId::uuid and ctx_unit_id = :unitId::uuid and "
                  + "ctx_lesson_id = :lessonId::uuid and ctx_user_id = :userId::uuid and ctx_class_id is null and "
                  + "suggested_content_subtype = 'pre-test' order by created_at desc")
    List<AlternatePath> findPreTestAlternatePathsForCULAndUser(@Bind("courseId") String course,
        @Bind("unitId") String unit, @Bind("lessonId") String lesson, @Bind("userId") String user);
}
