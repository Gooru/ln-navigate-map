package org.gooru.navigatemap.processor.coursepath.repositories.dao;

import java.util.List;
import java.util.UUID;

import org.gooru.navigatemap.processor.coursepath.repositories.mappers.AlternatePathMapper;
import org.gooru.navigatemap.processor.coursepath.repositories.mappers.ContentAddressMapper;
import org.gooru.navigatemap.processor.coursepath.repositories.mappers.SuggestionCardMapper;
import org.gooru.navigatemap.processor.data.AlternatePath;
import org.gooru.navigatemap.processor.data.ContentAddress;
import org.gooru.navigatemap.processor.data.SuggestionCard4Collection;
import org.gooru.navigatemap.processor.utilities.jdbi.PGArray;
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

    @SqlQuery("select target_collection_id from user_navigation_paths where target_content_subtype = 'benchmark' and "
                  + "ctx_user_id = :userId::uuid and target_collection_id = any(:assessmentList)")
    List<String> findBenchmarksAddedByUserFromList(@Bind("userId") String userId,
        @Bind("assessmentList") PGArray<UUID> assessmentList);

    @SqlQuery("select target_collection_id from user_navigation_paths where target_content_subtype = 'pre-test' and "
                  + "ctx_user_id = :userId::uuid and target_collection_id = any(:preTestsList)")
    List<String> findPreTestsAddedByUserFromList(@Bind("userId") String userId,
        @Bind("preTestsList") PGArray<UUID> preTestsList);

    @SqlQuery("select target_collection_id from user_navigation_paths where target_content_subtype = 'post-test' and "
                  + "ctx_user_id = :userId::uuid and target_collection_id = any(:postTestsList)")
    List<String> findPostTestsAddedByUserFromList(@Bind("userId") String userId,
        @Bind("postTestsList") PGArray<UUID> postTestsList);

    @SqlQuery("select taxonomy from lesson where course_id = :courseId::uuid and unit_id = :unitId::uuid and "
                  + "lesson_id = :lessonId::uuid and is_deleted = false")
    String findCompetenciesForLesson(@Bind("courseId") String course, @Bind("unitId") String unit,
        @Bind("lessonId") String lesson);

    @Mapper(SuggestionCardMapper.class)
    @SqlQuery("select id, title, format, subformat, thumbnail, metadata, taxonomy from collection where id = any"
                  + "(:collections)")
    List<SuggestionCard4Collection> createSuggestionsCardForCollections(@Bind("collections") PGArray<UUID> collections);

    @SqlQuery("select linked_content_id from assessment_extension where assessment_id = :preTestId and "
                  + "score_range_name = :scoreRangeName")
    List<String> findBackfillsForPreTestAndScoreRange(@Bind("preTestId") UUID preTestId,
        @Bind("scoreRangeName") String scoreRangeName);

    @Mapper(AlternatePathMapper.class)
    @SqlQuery("select id, ctx_user_id, ctx_class_id, ctx_course_id, ctx_unit_id, ctx_lesson_id, ctx_class_id, "
                  + "ctx_collection_id, parent_path_id, parent_path_type, target_course_id, target_unit_id, "
                  + "target_lesson_id, target_collection_id, target_content_type, target_content_subtype from "
                  + "user_navigation_paths where id = :pathId and ctx_user_id = :user::uuid and ctx_class_id is null")
    AlternatePath findAlternatePathByPathIdAndUser(@Bind("pathId") Long pathId, @Bind("user") String user);

    @Mapper(AlternatePathMapper.class)
    @SqlQuery("select id, ctx_user_id, ctx_class_id, ctx_course_id, ctx_unit_id, ctx_lesson_id, ctx_class_id, "
                  + "ctx_collection_id, parent_path_id, parent_path_type, target_course_id, target_unit_id, "
                  + "target_lesson_id, target_collection_id, target_content_type, target_content_subtype from "
                  + "user_navigation_paths where id = :pathId and ctx_user_id = :user::uuid and ctx_class_id = "
                  + ":classId::uuid")
    AlternatePath findAlternatePathByPathIdAndUserInClass(@Bind("pathId") Long pathId, @Bind("user") String user,
        @Bind("classId") String classId);

    @Mapper(AlternatePathMapper.class)
    @SqlQuery("select id, ctx_user_id, ctx_class_id, ctx_course_id, ctx_unit_id, ctx_lesson_id, ctx_class_id, "
                  + "ctx_collection_id, parent_path_id, parent_path_type, target_course_id, target_unit_id, "
                  + "target_lesson_id, target_collection_id, target_content_type, target_content_subtype from "
                  + "user_navigation_paths where parent_path_id = :id and target_content_subtype = 'benchmark'")
    List<AlternatePath> findBASubPathsForGivenPath(@Bind("id") Long id);

    @Mapper(AlternatePathMapper.class)
    @SqlQuery("select id, ctx_user_id, ctx_class_id, ctx_course_id, ctx_unit_id, ctx_lesson_id, ctx_class_id, "
                  + "ctx_collection_id, parent_path_id, parent_path_type, target_course_id, target_unit_id, "
                  + "target_lesson_id, target_collection_id, target_content_type, target_content_subtype from "
                  + "user_navigation_paths where parent_path_id = :id and target_content_type = 'collection'")
    List<AlternatePath> findBackfillsSubPathsForGivenPath(@Bind("id") Long id);

    @Mapper(AlternatePathMapper.class)
    @SqlQuery("select id, ctx_user_id, ctx_class_id, ctx_course_id, ctx_unit_id, ctx_lesson_id, ctx_class_id, "
                  + "ctx_collection_id, parent_path_id, parent_path_type, target_course_id, target_unit_id, "
                  + "target_lesson_id, target_collection_id, target_content_type, target_content_subtype from "
                  + "user_navigation_paths where ctx_course_id = :courseId::uuid and ctx_unit_id = :unitId::uuid and "
                  + "ctx_lesson_id = :lessonId::uuid and ctx_user_id = :userId::uuid and ctx_class_id = "
                  + ":classId::uuid and target_content_subtype = 'post-test'")
    List<AlternatePath> findPostTestAlternatePathsForCULAndUserInClass(@Bind("courseId") String course,
        @Bind("unitId") String unit, @Bind("lessonId") String lesson, @Bind("userId") String user,
        @Bind("classId") String classId);

    @Mapper(AlternatePathMapper.class)
    @SqlQuery("select id, ctx_user_id, ctx_class_id, ctx_course_id, ctx_unit_id, ctx_lesson_id, ctx_class_id, "
                  + "ctx_collection_id, parent_path_id, parent_path_type, target_course_id, target_unit_id, "
                  + "target_lesson_id, target_collection_id, target_content_type, target_content_subtype from "
                  + "user_navigation_paths where ctx_course_id = :courseId::uuid and ctx_unit_id = :unitId::uuid and "
                  + "ctx_lesson_id = :lessonId::uuid and ctx_user_id = :userId::uuid and ctx_class_id is null and "
                  + "target_content_subtype = 'post-test'")
    List<AlternatePath> findPostTestAlternatePathsForCULAndUser(@Bind("courseId") String course,
        @Bind("unitId") String unit, @Bind("lessonId") String lesson, @Bind("userId") String user);

    @Mapper(AlternatePathMapper.class)
    @SqlQuery("select id, ctx_user_id, ctx_class_id, ctx_course_id, ctx_unit_id, ctx_lesson_id, ctx_class_id, "
                  + "ctx_collection_id, parent_path_id, parent_path_type, target_course_id, target_unit_id, "
                  + "target_lesson_id, target_collection_id, target_content_type, target_content_subtype from "
                  + "user_navigation_paths where ctx_course_id = :courseId::uuid and ctx_unit_id = :unitId::uuid and "
                  + "ctx_lesson_id = :lessonId::uuid and ctx_user_id = :userId::uuid and ctx_class_id = "
                  + ":classId::uuid and target_content_subtype = 'pre-test'")
    List<AlternatePath> findPreTestAlternatePathsForCULAndUserInClass(@Bind("courseId") String course,
        @Bind("unitId") String unit, @Bind("lessonId") String lesson, @Bind("userId") String user,
        @Bind("classId") String classId);

    @Mapper(AlternatePathMapper.class)
    @SqlQuery("select id, ctx_user_id, ctx_class_id, ctx_course_id, ctx_unit_id, ctx_lesson_id, ctx_class_id, "
                  + "ctx_collection_id, parent_path_id, parent_path_type, target_course_id, target_unit_id, "
                  + "target_lesson_id, target_collection_id, target_content_type, target_content_subtype from "
                  + "user_navigation_paths where ctx_course_id = :courseId::uuid and ctx_unit_id = :unitId::uuid and "
                  + "ctx_lesson_id = :lessonId::uuid and ctx_user_id = :userId::uuid and ctx_class_id is null and "
                  + "target_content_subtype = 'post-test'")
    List<AlternatePath> findPreTestAlternatePathsForCULAndUser(@Bind("courseId") String course,
        @Bind("unitId") String unit, @Bind("lessonId") String lesson, @Bind("userId") String user);
}
