package org.gooru.navigatemap.processor.coursepath.repositories.dao;

import org.gooru.navigatemap.processor.coursepath.repositories.mappers.AlternatePathMapper;
import org.gooru.navigatemap.processor.data.AlternatePath;
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

}
