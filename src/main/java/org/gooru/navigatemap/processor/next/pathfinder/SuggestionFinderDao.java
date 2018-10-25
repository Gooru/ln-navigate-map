package org.gooru.navigatemap.processor.next.pathfinder;

import java.util.List;
import java.util.UUID;
import org.gooru.navigatemap.infra.utilities.jdbi.PGArray;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.SqlQuery;

/**
 * @author ashish on 11/5/18.
 */
interface SuggestionFinderDao {

  @SqlQuery(
      "select suggested_content_id from user_navigation_paths where ctx_user_id = :userId::uuid and "
          + "suggested_content_id = any(:itemList)")
  List<String> findSignatureItemsAddedByUserFromList(@Bind("userId") String userId,
      @Bind("itemList") PGArray<UUID> itemList);

  @SqlQuery("select item_id from signature_items where item_format = 'assessment' and "
      + "(competency_gut_code = any(:competencies) OR (micro_competency_gut_code = any(:competencies))) "
      + " order by weight desc")
  List<String> findSignatureAssessmentsForSpecifiedCompetencies(
      @Bind("competencies") PGArray<String> competencies);

  @SqlQuery("select item_id from signature_items where item_format = 'collection' and "
      + "(competency_gut_code = any(:competencies) OR (micro_competency_gut_code = any(:competencies))) "
      + " order by weight desc")
  List<String> findSignatureCollectionForSpecifiedCompetencies(
      @Bind("competencies") PGArray<String> stringPGArray);

  @SqlQuery("select item_id from signature_items where item_format = 'collection' and "
      + "(competency_gut_code = any(:competencies) OR (micro_competency_gut_code = any(:competencies))) "
      + "  and performance_range = :scoreRangeName order by weight desc")
  List<String> findSignatureCollectionForSpecifiedCompetenciesAndScoreRange(
      @Bind("competencies") PGArray<String> stringPGArray,
      @Bind("scoreRangeName") String scoreRangeName);

  @SqlQuery(
      "select exists (select 1 from collection where id = :id::uuid and is_deleted = false and grading = "
          + "'teacher'::grading_type)")
  boolean isAssessmentTeacherGraded(@Bind("id") String id);

}
