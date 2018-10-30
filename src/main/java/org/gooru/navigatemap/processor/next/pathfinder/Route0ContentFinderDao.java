package org.gooru.navigatemap.processor.next.pathfinder;

import java.util.List;
import java.util.UUID;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.BindBean;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.customizers.Mapper;

/**
 * @author ashish on 12/7/18.
 */
interface Route0ContentFinderDao {

  @SqlQuery(
      "select exists (select 1 from user_route0_content_detail where id = :pathId and unit_id = :unitId and "
          + "lesson_id = :lessonId and collection_id = :collectionId and collection_type = :collectionType "
          + "and user_route0_content_id = (select id from user_route0_content where user_id = :userId and "
          + "course_id = :courseId and class_id = :classId))")
  boolean validateRoute0ContextInClass(@BindBean Route0ContentFinderContext context);

  @SqlQuery(
      "select exists (select 1 from user_route0_content_detail where id = :pathId and unit_id = :unitId and "
          + "lesson_id = :lessonId and collection_id = :collectionId and collection_type = :collectionType "
          + "and user_route0_content_id = (select id from user_route0_content where user_id = :userId and "
          + "course_id = :courseId and class_id is null))")
  boolean validateRoute0ContextForIL(@BindBean Route0ContentFinderContext context);

  @Mapper(UserRoute0ContentDetailModelMapper.class)
  @SqlQuery(
      "select id, user_route0_content_id, unit_id, unit_title, unit_sequence, lesson_id, lesson_title, "
          + "lesson_sequence, collection_id, collection_type, collection_sequence, route0_sequence from "
          + "user_route0_content_detail where user_route0_content_id = (select id from user_route0_content "
          + "where user_id = :userId and course_id = :courseId and class_id is null) and route0_sequence > "
          + "(select route0_sequence from user_route0_content_detail where id = :pathId) order by "
          + "route0_sequence asc")
  List<UserRoute0ContentDetailModel> fetchNextContentsFromRoute0ForIL(
      @BindBean Route0ContentFinderContext context);

  @Mapper(UserRoute0ContentDetailModelMapper.class)
  @SqlQuery(
      "select id, user_route0_content_id, unit_id, unit_title, unit_sequence, lesson_id, lesson_title, "
          + "lesson_sequence, collection_id, collection_type, collection_sequence, route0_sequence from "
          + "user_route0_content_detail where user_route0_content_id = (select id from user_route0_content "
          + "where user_id = :userId and course_id = :courseId and class_id = :classId) and route0_sequence > "
          + "(select route0_sequence from user_route0_content_detail where id = :pathId) order by "
          + "route0_sequence asc")
  List<UserRoute0ContentDetailModel> fetchNextContentsFromRoute0InClass(
      @BindBean Route0ContentFinderContext context);

  @Mapper(UserRoute0ContentDetailModelMapper.class)
  @SqlQuery(
      "select id, user_route0_content_id, unit_id, unit_title, unit_sequence, lesson_id, lesson_title, "
          + "lesson_sequence, collection_id, collection_type, collection_sequence, route0_sequence from "
          + "user_route0_content_detail where user_route0_content_id = (select id from user_route0_content "
          + "where user_id = :userId and course_id = :courseId and class_id is null) and id = :pathId order by "
          + "route0_sequence asc")
  UserRoute0ContentDetailModel fetchSpecifiedItemFromRoute0ForIL(
      @BindBean Route0ContentFinderContext context);

  @Mapper(UserRoute0ContentDetailModelMapper.class)
  @SqlQuery(
      "select id, user_route0_content_id, unit_id, unit_title, unit_sequence, lesson_id, lesson_title, "
          + "lesson_sequence, collection_id, collection_type, collection_sequence, route0_sequence from "
          + "user_route0_content_detail where user_route0_content_id = (select id from user_route0_content "
          + "where user_id = :userId and course_id = :courseId and class_id = :classId) and id = :pathId "
          + "order by route0_sequence asc")
  UserRoute0ContentDetailModel fetchSpecifiedItemFromRoute0InClass(
      @BindBean Route0ContentFinderContext context);

  @Mapper(UserRoute0ContentDetailModelMapper.class)
  @SqlQuery(
      "select id, user_route0_content_id, unit_id, unit_title, unit_sequence, lesson_id, lesson_title, "
          + "lesson_sequence, collection_id, collection_type, collection_sequence, route0_sequence from "
          + "user_route0_content_detail where user_route0_content_id = (select id from user_route0_content "
          + "where user_id = :userId and course_id = :courseId and class_id is null) and unit_id = :unitId "
          + "and lesson_id = :lessonId order by route0_sequence asc limit 1")
  UserRoute0ContentDetailModel fetchFirstItemFromLessonForRoute0ForIL(
      @BindBean Route0ContentFinderContext context);

  @Mapper(UserRoute0ContentDetailModelMapper.class)
  @SqlQuery(
      "select id, user_route0_content_id, unit_id, unit_title, unit_sequence, lesson_id, lesson_title, "
          + "lesson_sequence, collection_id, collection_type, collection_sequence, route0_sequence from "
          + "user_route0_content_detail where user_route0_content_id = (select id from user_route0_content "
          + "where user_id = :userId and course_id = :courseId and class_id = :classId) and unit_id = :unitId "
          + "and lesson_id = :lessonId order by route0_sequence asc limit 1")
  UserRoute0ContentDetailModel fetchFirstItemFromLessonForRoute0InClass(
      @BindBean Route0ContentFinderContext context);

  @Mapper(UserRoute0ContentDetailModelMapper.class)
  @SqlQuery(
      "select id, user_route0_content_id, unit_id, unit_title, unit_sequence, lesson_id, lesson_title, "
          + "lesson_sequence, collection_id, collection_type, collection_sequence, route0_sequence from "
          + "user_route0_content_detail where user_route0_content_id = (select id from user_route0_content "
          + "where user_id = :userId and course_id = :courseId and class_id is null) order by "
          + "route0_sequence asc")
  List<UserRoute0ContentDetailModel> fetchAllContentsFromRoute0ForIL(
      @BindBean Route0ContentFinderContext context);

  @Mapper(UserRoute0ContentDetailModelMapper.class)
  @SqlQuery(
      "select id, user_route0_content_id, unit_id, unit_title, unit_sequence, lesson_id, lesson_title, "
          + "lesson_sequence, collection_id, collection_type, collection_sequence, route0_sequence from "
          + "user_route0_content_detail where user_route0_content_id = (select id from user_route0_content "
          + "where user_id = :userId and course_id = :courseId and class_id = :classId) order by "
          + "route0_sequence asc")
  List<UserRoute0ContentDetailModel> fetchAllContentsFromRoute0InClass(
      @BindBean Route0ContentFinderContext context);

  @SqlQuery(
      "select exists (select 1 from user_route0_content where user_id = :userId and course_id = :courseId and"
          + " class_id is null and status = 'accepted')")
  boolean route0ExistsAndAcceptedByUserForIL(@Bind("userId") UUID userId,
      @Bind("courseId") UUID courseId);

  @SqlQuery(
      "select exists (select 1 from user_route0_content where user_id = :userId and course_id = :courseId and"
          + " class_id = :classId and status = 'accepted')")
  boolean route0ExistsAndAcceptedByUserInClass(@Bind("userId") UUID userId,
      @Bind("courseId") UUID courseId,
      @Bind("classId") UUID classId);
}
