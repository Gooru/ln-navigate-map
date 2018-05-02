package org.gooru.navigatemap.processor.systemsuggestions;

import org.gooru.navigatemap.processor.data.AlternatePath;
import org.gooru.navigatemap.processor.utilities.jdbi.AlternatePathMapper;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.BindBean;
import org.skife.jdbi.v2.sqlobject.GetGeneratedKeys;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.Mapper;

/**
 * @author ashish on 24/11/17.
 */
interface AddSystemSuggestionDao {

    @GetGeneratedKeys
    @SqlUpdate("insert into user_navigation_paths (ctx_user_id, ctx_course_id, ctx_unit_id, ctx_lesson_id, "
                   + "ctx_class_id, ctx_collection_id, suggested_content_id, suggested_content_type, "
                   + "suggested_content_subtype, suggestion_type) values (:ctxUserId, :ctxCourseId, :ctxUnitId, "
                   + ":ctxLessonId, :ctxClassId, :ctxCollectionId, :pathIndex, :suggestedContentId, "
                   + ":suggestedContentType, :suggestedContentSubType, :suggestionType) ")
    long addSystemSuggestion(@BindBean AddSystemSuggestionCommand.AddSystemSuggestionsBean command);

    @SqlQuery("select id from user_navigation_paths where ctx_course_id = :ctxCourseId and ctx_unit_id = "
                  + ":ctxUnitId and ctx_lesson_id = :ctxLessonId and ctx_class_id = :ctxClassId and "
                  + "ctx_collection_id = :ctxCollectionId and suggested_content_id =  :suggestedContentId and "
                  + "suggestion_type = 'system' and ctx_user_id = :ctxUserId")
    Long findUserPathHavingSpecifiedSuggestionForClassRootedAtCollection(
        @BindBean AddSystemSuggestionCommand.AddSystemSuggestionsBean bean);

    @SqlQuery("select id from user_navigation_paths where ctx_course_id = :ctxCourseId and ctx_unit_id = "
                  + ":ctxUnitId and ctx_lesson_id = :ctxLessonId and ctx_class_id is null and "
                  + "ctx_collection_id = :ctxCollectionId and suggested_content_id =  :suggestedContentId and "
                  + "suggestion_type = 'system' and ctx_user_id = :ctxUserId")
    Long findUserPathHavingSpecifiedSuggestionForCourseRootedAtCollection(
        @BindBean AddSystemSuggestionCommand.AddSystemSuggestionsBean bean);

    @Mapper(AlternatePathMapper.class)
    @SqlQuery("select id, ctx_user_id, ctx_course_id, ctx_unit_id, ctx_lesson_id, ctx_class_id, ctx_collection_id, "
                  + "suggested_content_id, suggested_content_type, suggested_content_subtype, "
                  + "suggestion_type, serve_count from user_navigation_paths where ctx_user_id = :ctxUserId and "
                  + "ctx_class_id = :ctxClassId and suggested_content_id = :suggestedContentId and suggestion_type = "
                  + ":suggestionType;")
    AlternatePath findUserPathForRoute0WithClass(AddSystemSuggestionCommand.AddSystemSuggestionsBean bean);

    @Mapper(AlternatePathMapper.class)
    @SqlQuery("select id, ctx_user_id, ctx_course_id, ctx_unit_id, ctx_lesson_id, ctx_class_id, ctx_collection_id, "
                  + "suggested_content_id, suggested_content_type, suggested_content_subtype, "
                  + "suggestion_type, serve_count from user_navigation_paths where ctx_user_id = :ctxUserId and "
                  + "ctx_class_id is null and suggested_content_id = :suggestedContentId and suggestion_type = "
                  + ":suggestionType;")
    AlternatePath findUserPathForRoute0WithoutClass(AddSystemSuggestionCommand.AddSystemSuggestionsBean bean);

    @SqlQuery("select exists (select 1 from collection l, class c where l.course_id = :ctxCourseId and l.unit_id = "
                  + ":ctxUnitId and l.lesson_id = :ctxLessonId and l.id = :ctxCollectionId and l.is_deleted = false "
                  + "and c.id = :ctxClassId and c.course_id = l.course_id and c.is_deleted = false)")
    Boolean validateContextInformationForClassRootedAtCollection(
        @BindBean AddSystemSuggestionCommand.AddSystemSuggestionsBean bean);

    @SqlQuery("select exists (select 1 from collection l where l.course_id = :ctxCourseId and l.unit_id = :ctxUnitId "
                  + "and l.lesson_id = :ctxLessonId and l.id = :ctxCollectionId and l.is_deleted = false) ")
    Boolean validateContextInformationForCourseRootedAtCollection(
        @BindBean AddSystemSuggestionCommand.AddSystemSuggestionsBean bean);

    @SqlQuery("select exists (select 1 from course l where l.id = :ctxCourseId and l.is_deleted = false)")
    Boolean validateContextInformationForRoute0WithoutClass(AddSystemSuggestionCommand.AddSystemSuggestionsBean bean);

    @SqlQuery("select exists (select 1 from course l, class c where l.id = :ctxCourseId and l.is_deleted = false and "
                  + "c.id = :ctxClassId and c.course_id = l.course_id and c.is_deleted = false)")
    Boolean validateContextInformationForRoute0WithClass(AddSystemSuggestionCommand.AddSystemSuggestionsBean bean);
}
