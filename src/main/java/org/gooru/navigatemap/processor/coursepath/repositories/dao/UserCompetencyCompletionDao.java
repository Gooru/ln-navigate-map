package org.gooru.navigatemap.processor.coursepath.repositories.dao;

import java.util.List;

import org.gooru.navigatemap.processor.utilities.jdbi.PGArray;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.SqlBatch;
import org.skife.jdbi.v2.sqlobject.SqlQuery;

/**
 * @author ashish on 9/5/17.
 */
public interface UserCompetencyCompletionDao {
    @SqlBatch("insert into user_competency_completion (user_id, comp_mcomp_id, ctx_course_id, ctx_unit_id, "
                  + "ctx_lesson_id, ctx_class_id, ctx_collection_id)values(:userId::uuid, :competency, :course::uuid,"
                  + " :unit::uuid, :lesson::uuid, :classId::uuid, :collection::uuid)")
    void markCompetencyCompletedInClassContext(@Bind("userId") String userId,
        @Bind("competency") List<String> competency, @Bind("course") String course, @Bind("unit") String unit,
        @Bind("lesson") String lesson, @Bind("classId") String classId, @Bind("collection") String collection);

    @SqlBatch("insert into user_competency_completion (user_id, comp_mcomp_id, ctx_course_id, ctx_unit_id, "
                  + "ctx_lesson_id, ctx_collection_id)values(:userId::uuid, :competency, :course::uuid,"
                  + " :unit::uuid, :lesson::uuid, :collection::uuid)")
    void markCompetencyCompletedNoClassContext(@Bind("userId") String userId,
        @Bind("competency") List<String> competency, @Bind("course") String course, @Bind("unit") String unit,
        @Bind("lesson") String lesson, @Bind("collection") String collection);

    @SqlQuery("select comp_mcomp_id from user_competency_completion where comp_mcomp_id = any(:competencyList) "
                  + "and user_id = :userId::uuid")
    List<String> findCompletedCompetenciesForUserInGivenList(@Bind("userId") String userId,
        @Bind("competencyList") PGArray<String> competencyList);

}
