package org.gooru.navigatemap.processor.postprocessor;

import java.util.List;

import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.BindBean;
import org.skife.jdbi.v2.sqlobject.SqlBatch;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;

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

}
