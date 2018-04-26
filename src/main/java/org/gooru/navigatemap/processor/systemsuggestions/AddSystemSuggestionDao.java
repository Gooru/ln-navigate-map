package org.gooru.navigatemap.processor.systemsuggestions;

import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.BindBean;
import org.skife.jdbi.v2.sqlobject.GetGeneratedKeys;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;

/**
 * @author ashish on 24/11/17.
 */
interface AddSystemSuggestionDao {

    @GetGeneratedKeys
    @SqlUpdate("insert into user_navigation_paths (ctx_user_id, ctx_course_id, ctx_unit_id, ctx_lesson_id, "
                   + "ctx_class_id, ctx_collection_id, parent_path_id, suggested_content_id, suggested_content_type, "
                   + "suggested_content_subtype, suggestion_type) values (:ctxUserId, :ctxCourseId, :ctxUnitId, "
                   + ":ctxLessonId, :ctxClassId, :ctxCollectionId, :parentPathId, :suggestedContentId, "
                   + ":suggestedContentType, :suggestedContentSubType, 'system') ")
    long addSystemSuggestion(@BindBean AddSystemSuggestionCommand.AddSystemSuggestionsBean command,
        @Bind("parentPathId") Long parentPathId);

    @SqlQuery("select id from user_navigation_paths where ctx_course_id = :ctxCourseId and ctx_unit_id = "
                  + ":ctxUnitId and ctx_lesson_id = :ctxLessonId and ctx_class_id =  :ctxClassId and "
                  + "ctx_collection_id is null and suggested_content_id =  :suggestedContentId and suggestion_type = "
                  + "'system' and ctx_user_id = :ctxUserId")
    Long findUserPathHavingSpecifiedSuggestionForClassRootedAtLesson(
        @BindBean AddSystemSuggestionCommand.AddSystemSuggestionsBean bean);

    @SqlQuery("select id from user_navigation_paths where ctx_course_id = :ctxCourseId and ctx_unit_id = "
                  + ":ctxUnitId and ctx_lesson_id = :ctxLessonId and ctx_class_id = :ctxClassId and "
                  + "ctx_collection_id = :ctxCollectionId and suggested_content_id =  :suggestedContentId and "
                  + "suggestion_type = 'system' and ctx_user_id = :ctxUserId")
    Long findUserPathHavingSpecifiedSuggestionForClassRootedAtCollection(
        @BindBean AddSystemSuggestionCommand.AddSystemSuggestionsBean bean);

    @SqlQuery("select id from user_navigation_paths where ctx_course_id = :ctxCourseId and ctx_unit_id = "
                  + ":ctxUnitId and ctx_lesson_id = :ctxLessonId and ctx_class_id is null and "
                  + "ctx_collection_id is null and suggested_content_id =  :suggestedContentId and suggestion_type = "
                  + "'system' and ctx_user_id = :ctxUserId")
    Long findUserPathHavingSpecifiedSuggestionForCourseRootedAtLesson(
        @BindBean AddSystemSuggestionCommand.AddSystemSuggestionsBean bean);

    @SqlQuery("select id from user_navigation_paths where ctx_course_id = :ctxCourseId and ctx_unit_id = "
                  + ":ctxUnitId and ctx_lesson_id = :ctxLessonId and ctx_class_id is null and "
                  + "ctx_collection_id = :ctxCollectionId and suggested_content_id =  :suggestedContentId and "
                  + "suggestion_type = 'system' and ctx_user_id = :ctxUserId")
    Long findUserPathHavingSpecifiedSuggestionForCourseRootedAtCollection(
        @BindBean AddSystemSuggestionCommand.AddSystemSuggestionsBean bean);

    @SqlQuery("select id from user_navigation_paths where ctx_user_id = :ctxUserId and ctx_course_id = :ctxCourseId "
                  + "and ctx_unit_id = :ctxUnitId and ctx_lesson_id = :ctxLessonId and ctx_collection_id is null and "
                  + "ctx_class_id = :ctxClassId and suggestion_type = 'system' order by id desc limit 1")
    Long findParentPathForClassRootedAtLesson(@BindBean AddSystemSuggestionCommand.AddSystemSuggestionsBean bean);

    @SqlQuery("select id from user_navigation_paths where ctx_user_id = :ctxUserId and ctx_course_id = :ctxCourseId "
                  + "and ctx_unit_id = :ctxUnitId and ctx_lesson_id = :ctxLessonId and ctx_collection_id "
                  + "=:ctxCollectionId and ctx_class_id = :ctxClassId and suggestion_type = 'system' order by id desc"
                  + " limit 1")
    Long findParentPathForClassRootedAtCollection(@BindBean AddSystemSuggestionCommand.AddSystemSuggestionsBean bean);

    @SqlQuery("select id from user_navigation_paths where ctx_user_id = :ctxUserId and ctx_course_id = :ctxCourseId "
                  + "and ctx_unit_id = :ctxUnitId and ctx_lesson_id = :ctxLessonId and ctx_collection_id is null and "
                  + "ctx_class_id is null and suggestion_type = 'system' order by id desc limit 1")
    Long findParentPathForCourseRootedAtLesson(@BindBean AddSystemSuggestionCommand.AddSystemSuggestionsBean bean);

    @SqlQuery("select id from user_navigation_paths where ctx_user_id = :ctxUserId and ctx_course_id = :ctxCourseId "
                  + "and ctx_unit_id = :ctxUnitId and ctx_lesson_id = :ctxLessonId and ctx_collection_id = "
                  + ":ctxCollectionId and ctx_class_id is null and suggestion_type = 'system' order by id desc limit 1")
    Long findParentPathForCourseRootedAtCollection(@BindBean AddSystemSuggestionCommand.AddSystemSuggestionsBean bean);

    @SqlQuery("select exists (select 1 from lesson l, class c where l.course_id = :ctxCourseId and l.unit_id = "
                  + ":ctxUnitId and l.lesson_id = :ctxLessonId and l.is_deleted = false and c.id = :ctxClassId and c"
                  + ".course_id = l.course_id and c.is_deleted = false)")
    Boolean validateContextInformationForClassRootedAtLesson(
        @BindBean AddSystemSuggestionCommand.AddSystemSuggestionsBean bean);

    @SqlQuery("select exists (select 1 from collection l, class c where l.course_id = :ctxCourseId and l.unit_id = "
                  + ":ctxUnitId and l.lesson_id = :ctxLessonId and l.id = :ctxCollectionId and l.is_deleted = false "
                  + "and c.id = :ctxClassId and c.course_id = l.course_id and c.is_deleted = false)")
    Boolean validateContextInformationForClassRootedAtCollection(
        @BindBean AddSystemSuggestionCommand.AddSystemSuggestionsBean bean);

    @SqlQuery("select exists (select 1 from lesson l where l.course_id = :ctxCourseId and l.unit_id = :ctxUnitId and "
                  + "l.lesson_id = :ctxLessonId and l.is_deleted = false) ")
    Boolean validateContextInformationForCourseRootedAtLesson(
        @BindBean AddSystemSuggestionCommand.AddSystemSuggestionsBean bean);

    @SqlQuery("select exists (select 1 from collection l where l.course_id = :ctxCourseId and l.unit_id = :ctxUnitId "
                  + "and l.lesson_id = :ctxLessonId and l.id = :ctxCollectionId and l.is_deleted = false) ")
    Boolean validateContextInformationForCourseRootedAtCollection(
        @BindBean AddSystemSuggestionCommand.AddSystemSuggestionsBean bean);
}
