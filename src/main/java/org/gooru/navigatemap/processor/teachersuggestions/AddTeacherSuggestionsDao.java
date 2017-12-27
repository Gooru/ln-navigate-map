package org.gooru.navigatemap.processor.teachersuggestions;

import java.util.List;
import java.util.UUID;

import org.gooru.navigatemap.processor.utilities.jdbi.PGArray;
import org.gooru.navigatemap.processor.utilities.jdbi.UUIDMapper;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.BindBean;
import org.skife.jdbi.v2.sqlobject.SqlBatch;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.customizers.Mapper;

/**
 * @author ashish on 24/11/17.
 */
interface AddTeacherSuggestionsDao {

    @SqlBatch("insert into user_navigation_paths (ctx_user_id, ctx_course_id, ctx_unit_id, ctx_lesson_id, "
                  + "ctx_class_id, ctx_collection_id, parent_path_id, suggested_content_id, suggested_content_type, "
                  + "suggested_content_subtype, suggestion_type) values (:ctxUserId, :ctxCourseId, :ctxUnitId, "
                  + ":ctxLessonId, :ctxClassId, :ctxCollectionId, null, :suggestedContentId, :suggestedContentType, "
                  + ":suggestedContentSubType, 'teacher')")
    void addTeacherSuggestion(@BindBean AddTeacherSuggestionsCommand.AddTeacherSuggestionsBean command,
        @Bind("ctxUserId") List<UUID> ctxUserIds);

    @SqlQuery("select ctx_user_id from user_navigation_paths where ctx_course_id = :ctxCourseId and ctx_unit_id = "
                  + ":ctxUnitId and ctx_lesson_id = :ctxLessonId and ctx_class_id =  :ctxClassId and "
                  + "ctx_collection_id is null and suggested_content_id =  :suggestedContentId and suggestion_type = "
                  + "'teacher' and ctx_user_id = any(:ctxUserIds)")
    @Mapper(UUIDMapper.class)
    List<UUID> findUsersHavingSpecifiedSuggestionForClassRootedAtLesson(
        @BindBean AddTeacherSuggestionsCommand.AddTeacherSuggestionsBean bean,
        @Bind("ctxUserIds") PGArray<UUID> ctxUserIds);

    @SqlQuery("select ctx_user_id from user_navigation_paths where ctx_course_id = :ctxCourseId and ctx_unit_id = "
                  + ":ctxUnitId and ctx_lesson_id = :ctxLessonId and ctx_class_id =  :ctxClassId and "
                  + "ctx_collection_id = :ctxCollectionId and suggested_content_id =  :suggestedContentId and "
                  + "suggestion_type = 'teacher' and ctx_user_id = any(:ctxUserIds)")
    @Mapper(UUIDMapper.class)
    List<UUID> findUsersHavingSpecifiedSuggestionForClassRootedAtCollection(
        @BindBean AddTeacherSuggestionsCommand.AddTeacherSuggestionsBean bean,
        @Bind("ctxUserIds") PGArray<UUID> ctxUserIds);

    @SqlQuery("select exists (select 1 from lesson l, class c where l.course_id = :ctxCourseId and l.unit_id = "
                  + ":ctxUnitId and l.lesson_id = :ctxLessonId and l.is_deleted = false and c.id = :ctxClassId and c"
                  + ".course_id = l.course_id and c.is_deleted = false)")
    Boolean validateContextInformationForClassRootedAtLesson(
        @BindBean AddTeacherSuggestionsCommand.AddTeacherSuggestionsBean bean);

    @SqlQuery("select exists (select 1 from collection l, class c where l.course_id = :ctxCourseId and l.unit_id = "
                  + ":ctxUnitId and l.lesson_id = :ctxLessonId and l.id = :ctxCollectionId and l.is_deleted = false "
                  + "and c.id = :ctxClassId and c.course_id = l.course_id and c.is_deleted = false)")
    Boolean validateContextInformationForClassRootedAtCollection(
        @BindBean AddTeacherSuggestionsCommand.AddTeacherSuggestionsBean bean);

}
