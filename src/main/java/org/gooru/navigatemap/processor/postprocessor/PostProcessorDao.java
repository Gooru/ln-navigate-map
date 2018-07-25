package org.gooru.navigatemap.processor.postprocessor;

import java.util.List;

import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.BindBean;
import org.skife.jdbi.v2.sqlobject.SqlBatch;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.Mapper;

/**
 * @author ashish on 22/9/17.
 */
interface PostProcessorDao {

    @SqlUpdate("update user_navigation_paths set serve_count = serve_count + 1 where id = :id")
    void updatePathServeCount(@Bind("id") long id);

    @SqlBatch("insert into suggestions_tracker (user_id, course_id, unit_id, lesson_id, class_id, collection_id, "
                  + "suggested_content_id, suggestion_origin, suggestion_originator_id, suggestion_criteria, "
                  + "suggested_content_type, suggested_to, accepted, accepted_at) values (:userId, :courseId, :unitId, "
                  + ":lessonId, :classId, :collectionId, :suggestedContentId, :suggestionOrigin, "
                  + ":suggestionOriginatorId, :suggestionCriteria, :suggestedContentType, :suggestedTo, :accepted, "
                  + ":acceptedAt)")
    void insertAllSuggestions(@BindBean List<SuggestionTrackerModel> models);

    @Mapper(SuggestionTrackerModelMapper.class)
    @SqlQuery("select * from suggestions_tracker where user_id = :userId and course_id = :courseId and unit_id = "
                  + ":unitId and lesson_id = :lessonId and collection_id is null and class_id = :classId and "
                  + "suggested_content_id = :suggestedContentId")
    SuggestionTrackerModel fetchSystemSuggestionInClassAtLesson(
        @BindBean PostProcessSystemSuggestionsAddHandler.SystemSuggestionPayload bean);

    @Mapper(SuggestionTrackerModelMapper.class)
    @SqlQuery("select * from suggestions_tracker where user_id = :userId and course_id = :courseId and unit_id = "
                  + ":unitId and lesson_id = :lessonId and collection_id is null and class_id is null and "
                  + "suggested_content_id = :suggestedContentId")
    SuggestionTrackerModel fetchSystemSuggestionForILAtLesson(
        @BindBean PostProcessSystemSuggestionsAddHandler.SystemSuggestionPayload bean);

    @Mapper(SuggestionTrackerModelMapper.class)
    @SqlQuery("select * from suggestions_tracker where user_id = :userId and course_id = :courseId and unit_id = "
                  + ":unitId and lesson_id = :lessonId and collection_id = :collectionId and class_id = :classId and "
                  + "suggested_content_id = :suggestedContentId")
    SuggestionTrackerModel fetchSystemSuggestionInClassAtCollection(
        @BindBean PostProcessSystemSuggestionsAddHandler.SystemSuggestionPayload bean);

    @Mapper(SuggestionTrackerModelMapper.class)
    @SqlQuery("select * from suggestions_tracker where user_id = :userId and course_id = :courseId and unit_id = "
                  + ":unitId and lesson_id = :lessonId and collection_id = :collectionId and class_id is null and "
                  + "suggested_content_id = :suggestedContentId")
    SuggestionTrackerModel fetchSystemSuggestionForILAtCollection(
        @BindBean PostProcessSystemSuggestionsAddHandler.SystemSuggestionPayload bean);

    @SqlUpdate("update suggestions_tracker set accepted = true, accepted_at = now() where id = :id")
    void acceptSystemSuggestion(@Bind("id") Long id);
}
