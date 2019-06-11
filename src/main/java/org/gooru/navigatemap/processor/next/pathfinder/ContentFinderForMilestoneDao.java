package org.gooru.navigatemap.processor.next.pathfinder;

import java.util.Collections;
import java.util.List;
import org.gooru.navigatemap.infra.data.ContentAddress;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.customizers.Mapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author ashish.
 */

abstract class ContentFinderForMilestoneDao {

  private static final Logger LOGGER = LoggerFactory.getLogger(ContentFinderForMilestoneDao.class);

  @SqlQuery(
      " select distinct mlp.milestone_id milestone_id, mlp.grade_seq from milestone_lesson_map mlp "
          + " inner join lesson l on l.lesson_id = mlp.lesson_id and l.course_id = mlp.course_id "
          + " where l.is_deleted = false and mlp.course_id = :courseId::uuid and fw_code = :fwCode "
          + " order by mlp.grade_seq asc")
  abstract List<String> findMilestonesInCourse(@Bind("courseId") String courseId,
      @Bind("fwCode") String fwCode);


  @SqlQuery(
      "select milestone_id from ("
          + " select distinct mlp.milestone_id milestone_id, mlp.grade_seq from milestone_lesson_map mlp"
          + " inner join lesson l on l.lesson_id = mlp.lesson_id and l.course_id = mlp.course_id"
          + " where l.is_deleted = false and mlp.course_id = :courseId::uuid and fw_code = :fwCode and"
          + " mlp.grade_seq >= (select grade_seq from milestone_lesson_map where milestone_id = :milestoneId limit 1)"
          + " order by mlp.grade_seq asc) AS milestones")
  abstract List<String> findNextMilestonesInCourse(@Bind("courseId") String courseId,
      @Bind("milestoneId") String milestoneId, @Bind("fwCode") String fwCode);


  @SqlQuery(
      "select mlp.lesson_id from milestone_lesson_map mlp "
          + " inner join lesson l on l.lesson_id = mlp.lesson_id and l.course_id = mlp.course_id "
          + " inner join unit u on u.unit_id = mlp.unit_id and u.course_id = mlp.course_id "
          + " where l.is_deleted = false and u.is_deleted = false and mlp.course_id = :courseId::uuid "
          + " and mlp.milestone_id = :milestoneId "
          + " order by grade_seq, u.sequence_Id, l.sequence_id asc")
  abstract List<String> findLessonsInCM(@Bind("courseId") String courseId,
      @Bind("milestoneId") String milestoneId);

  @SqlQuery(
      "select course_id, unit_id, :milestoneId as milestone_id, lesson_id, id, format, subformat, null as path_id, null as path_type,  "
          + " class_visibility as visibility from collection c where course_id = :courseId::uuid and "
          + " format != 'offline-activity'::content_container_type and "
          + " lesson_id = :lessonId::uuid and is_deleted = false and sequence_id > (select "
          + " sequence_id from collection where course_id = :courseId::uuid and "
          + " lesson_id = :lessonId::uuid and id = :collectionId::uuid) order by sequence_id asc")
  @Mapper(ContentAddressWithMilestoneMapper.class)
  abstract List<ContentAddress> findNextCollectionsInCML(@Bind("courseId") String courseId,
      @Bind("lessonId") String lessonId,
      @Bind("collectionId") String collectionId,
      @Bind("milestoneId") String milestoneId);


  @SqlQuery(
      "select course_id, unit_id, :milestoneId as milestone_id,  lesson_id, id, format, subformat, null as path_id, null as path_type, "
          + " class_visibility as visibility from collection c where course_id = :courseId::uuid and  "
          + " format != 'offline-activity'::content_container_type and "
          + " lesson_id = :lessonId::uuid and is_deleted = false order by sequence_id asc")
  @Mapper(ContentAddressWithMilestoneMapper.class)
  abstract List<ContentAddress> findCollectionsInCML(@Bind("courseId") String courseId,
      @Bind("lessonId") String lessonId,
      @Bind("milestoneId") String milestoneId);


  List<String> findNextLessonsInCM(String courseId, String milestoneId, String lessonId) {
    List<String> lessonsInMilestone = findLessonsInCM(courseId, milestoneId);
    if (lessonsInMilestone == null || lessonsInMilestone.isEmpty()) {
      LOGGER.warn("Milestone '{}' does not have any lessons", milestoneId);
      throw new IllegalArgumentException("Milestone does not have lessons: " + milestoneId);
    }

    int indexOfSpecifiedLesson = lessonsInMilestone.indexOf(lessonId);
    if (indexOfSpecifiedLesson < 0) {
      LOGGER.warn("Lesson: '{}' not found in Milestone: '{}'", lessonId, milestoneId);
      throw new IllegalArgumentException(
          "Lesson: " + lessonId + " does not exist in milestone: " + milestoneId);
    }

    List<String> nextLessonsInCMIncludingSpecified = lessonsInMilestone
        .subList(indexOfSpecifiedLesson, lessonsInMilestone.size());
    return Collections.unmodifiableList(nextLessonsInCMIncludingSpecified);
  }


}
