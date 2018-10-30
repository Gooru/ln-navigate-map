package org.gooru.navigatemap.processor.next.pathfinder;

import java.util.Collections;
import java.util.List;
import org.gooru.navigatemap.app.components.utilities.DbLookupUtility;
import org.gooru.navigatemap.infra.data.CurrentItemType;
import org.gooru.navigatemap.infra.utilities.CollectionUtils;
import org.skife.jdbi.v2.DBI;

/**
 * @author ashish on 11/5/18.
 */
public class SuggestionFinderImpl implements SuggestionFinder {

  private final DBI dbi;
  private PathFinderContext context;
  private List<String> competencies;
  private SuggestionFinderDao dao;

  SuggestionFinderImpl(DBI dbi) {

    this.dbi = dbi;
  }

  @Override
  public List<String> findSignatureCollectionsForCompetencies(PathFinderContext context,
      List<String> competencies) {
    this.context = context;
    this.competencies = competencies;

    if (isNotEligibleForSuggestion()) {
      return Collections.emptyList();
    }

    List<String> signatureCollectionsForCompetency =
        getSuggestionFinderDao().findSignatureCollectionForSpecifiedCompetenciesAndScoreRange(
            CollectionUtils.convertToSqlArrayOfString(competencies),
            DbLookupUtility.getInstance().scoreRangeNameByScore(context.getScore()));

    if (signatureCollectionsForCompetency == null || signatureCollectionsForCompetency.isEmpty()) {
      // No collections found, may be because of score range. Find again without score range
      signatureCollectionsForCompetency = dao.findSignatureCollectionForSpecifiedCompetencies(
          CollectionUtils.convertToSqlArrayOfString(competencies));
    }
    if (signatureCollectionsForCompetency != null && !signatureCollectionsForCompetency.isEmpty()) {
      List<String> signatureItemsAlreadyAddedByUser =
          dao.findSignatureItemsAddedByUserFromList(context.getUserId(),
              CollectionUtils.convertToSqlArrayOfUUID(signatureCollectionsForCompetency));
      List<String> signatureItemsNotAddedByUser =
          CollectionUtils
              .intersect(signatureCollectionsForCompetency, signatureItemsAlreadyAddedByUser);
      return CollectionUtils.uniqueMaintainOrder(signatureItemsNotAddedByUser);
    }
    return Collections.emptyList();
  }

  @Override
  public List<String> findSignatureAssessmentsForCompetencies(PathFinderContext context,
      List<String> competencies) {
    this.context = context;
    this.competencies = competencies;
    if (isNotEligibleForSuggestion()) {
      return Collections.emptyList();
    }
    List<String> signatureAssessmentsForCompetencies = getSuggestionFinderDao()
        .findSignatureAssessmentsForSpecifiedCompetencies(
            CollectionUtils.convertToSqlArrayOfString(competencies));

    if (signatureAssessmentsForCompetencies != null && !signatureAssessmentsForCompetencies
        .isEmpty()) {
      List<String> signatureItemsAlreadyAddedByUser =
          dao.findSignatureItemsAddedByUserFromList(context.getUserId(),
              CollectionUtils.convertToSqlArrayOfUUID(signatureAssessmentsForCompetencies));
      List<String> signatureItemsNotAddedByUser =
          CollectionUtils
              .intersect(signatureAssessmentsForCompetencies, signatureItemsAlreadyAddedByUser);
      return CollectionUtils.uniqueMaintainOrder(signatureItemsNotAddedByUser);
    }
    return Collections.emptyList();
  }

  private boolean isNotEligibleForSuggestion() {
    return (competencies == null || competencies.isEmpty() || isAssessmentForTeacherGrading());
  }

  private boolean isAssessmentForTeacherGrading() {
    return (context.getContentAddress().getCurrentItemType() == CurrentItemType.Assessment)
        && (getSuggestionFinderDao()
        .isAssessmentTeacherGraded(context.getContentAddress().getCurrentItem()));
  }

  private SuggestionFinderDao getSuggestionFinderDao() {
    if (dao == null) {
      dao = dbi.onDemand(SuggestionFinderDao.class);
    }
    return dao;
  }

}
