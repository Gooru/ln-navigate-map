package org.gooru.navigatemap.processor.next.pathfinder;

import java.util.List;

import org.gooru.navigatemap.infra.data.AlternatePath;
import org.gooru.navigatemap.infra.data.ContentAddress;
import org.gooru.navigatemap.infra.utilities.jdbi.AlternatePathMapper;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.BindBean;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.customizers.Mapper;

/**
 * @author ashish on 7/5/18.
 */
public interface AlternatePathDao {

    @Mapper(AlternatePathMapper.class)
    @SqlQuery("select id, ctx_user_id, ctx_class_id, ctx_course_id, ctx_unit_id, ctx_lesson_id, ctx_collection_id, "
                  + "suggestion_type, suggested_content_id, suggested_content_type, suggested_content_subtype from "
                  + "user_navigation_paths where id = :pathId and ctx_user_id = :user::uuid and ctx_class_id is null")
    AlternatePath findAlternatePathByPathIdAndUserForIL(@Bind("pathId") Long pathId, @Bind("user") String user);

    @Mapper(AlternatePathMapper.class)
    @SqlQuery("select id, ctx_user_id, ctx_class_id, ctx_course_id, ctx_unit_id, ctx_lesson_id, ctx_collection_id, "
                  + "suggestion_type, suggested_content_id, suggested_content_type, suggested_content_subtype  from "
                  + "user_navigation_paths where id = :pathId and ctx_user_id = :user::uuid and ctx_class_id = "
                  + ":classId::uuid")
    AlternatePath findAlternatePathByPathIdAndUserInClass(@Bind("pathId") Long pathId, @Bind("user") String user,
        @Bind("classId") String classId);

    @Mapper(AlternatePathMapper.class)
    @SqlQuery("select id, ctx_user_id, ctx_class_id, ctx_course_id, ctx_unit_id, ctx_lesson_id, ctx_collection_id, "
                  + "suggestion_type, suggested_content_id, suggested_content_type, suggested_content_subtype from "
                  + "user_navigation_paths where ctx_user_id = :user::uuid and ctx_class_id = :classId::uuid "
                  + " and ctx_course_id = :course::uuid and ctx_unit_id = :unit::uuid and ctx_lesson_id = "
                  + ":lesson::uuid and  ctx_collection_id = :collection::uuid and suggestion_type = 'teacher' order "
                  + "by id asc ")
    List<AlternatePath> findTeacherPathsForSpecifiedContext(@BindBean ContentAddress contentAddress,
        @Bind("user") String user, @Bind("classId") String classId);

}
