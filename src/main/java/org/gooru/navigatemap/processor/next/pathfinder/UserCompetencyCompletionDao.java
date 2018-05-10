package org.gooru.navigatemap.processor.next.pathfinder;

import java.util.List;

import org.gooru.navigatemap.infra.utilities.jdbi.PGArray;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.SqlBatch;
import org.skife.jdbi.v2.sqlobject.SqlQuery;

/**
 * @author ashish on 8/5/18.
 */
public interface UserCompetencyCompletionDao {

    /*
     * Inserts a record into user_competency_status table with the completionStatus provided by caller. It could either
     * be completed or mastered
     */
    @SqlBatch("insert into user_competency_status (user_id, comp_mcomp_id, completion_status) values(:userId::uuid,"
                  + " :competency, :completionStatus) ON CONFLICT (user_id, comp_mcomp_id) DO UPDATE SET "
                  + " completion_status = EXCLUDED.completion_status where "
                  + " EXCLUDED.completion_status > user_competency_status.completion_status")
    void markCompetencyCompletedOrMastered(@Bind("userId") String userId, @Bind("competency") List<String> competency,
        @Bind("completionStatus") int completionStatus);

    /*
     * Checks for record existence. It could either be mastered or completed.
     */
    @SqlQuery("select comp_mcomp_id from user_competency_status where comp_mcomp_id = any(:competencyList) "
                  + "and user_id = :userId::uuid")
    List<String> findCompletedOrMasteredCompetenciesForUserInGivenList(@Bind("userId") String userId,
        @Bind("competencyList") PGArray<String> competencyList);

    /*
     * Fetches the competencies which are completed or mastered based on status
     */
    @SqlQuery("select comp_mcomp_id from user_competency_status where comp_mcomp_id = any(:competencyList) "
                  + "and user_id = :userId::uuid and completion_status = :completionStatus")
    List<String> findCompetenciesBasedOnCompletionStatusForUserInGivenList(@Bind("userId") String userId,
        @Bind("competencyList") PGArray<String> competencyList, @Bind("completionStatus") int completionStatus);

}
