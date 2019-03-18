package org.gooru.navigatemap.processor.next.pathfinder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.gooru.navigatemap.app.components.utilities.DbLookupUtility;
import org.gooru.navigatemap.infra.data.CurrentItemSubtype;
import org.gooru.navigatemap.infra.data.CurrentItemType;
import org.gooru.navigatemap.infra.utilities.CollectionUtils;
import org.skife.jdbi.v2.DBI;

/**
 * @author ashish
 */
class CompetencyMasteredHandler {

  private final PathFinderContext context;
  private final DBI dbi;
  private List<String> competencies;
  private boolean areCompetenciesFetched;

  private static final int COMPETENCY_STATUS_MASTERED = 5;

  CompetencyMasteredHandler(DBI dbi, PathFinderContext context) {
    this.dbi = dbi;
    this.context = context;
    areCompetenciesFetched = false;
  }

  void handleCompetencyMastered() {
    if (context.getContentAddress().isOnSystemPath()
        && context.getContentAddress().getCurrentItemType() == CurrentItemType.Assessment
        && context.getContentAddress().getCurrentItemSubtype()
        == CurrentItemSubtype.SignatureAssessment) {

      if (isCompetencyMastered()) {
        markCompetencyMasteredForUser();
      }
    }
  }

  private List<String> fetchCompetenciesForSignatureAssessment() {
    ContentFinderDao finderDao = dbi.onDemand(ContentFinderDao.class);
    if (!areCompetenciesFetched) {
      competencies = finderDao
          .findCompetenciesForSignatureAssessment(context.getContentAddress().getCurrentItem());
      if (competencies == null) {
        competencies = Collections.emptyList();
      }
      areCompetenciesFetched = true;
    }
    return Collections.unmodifiableList(competencies);
  }

  private void markCompetencyMasteredForUser() {
    List<String> competencyList = new ArrayList<>(fetchCompetenciesForSignatureAssessment());
    if (!competencyList.isEmpty()) {
      UserCompetencyCompletionDao userCompetencyCompletionDao = dbi
          .onDemand(UserCompetencyCompletionDao.class);
      List<String> completedCompetenciesByUser = userCompetencyCompletionDao
          .findCompletedOrMasteredCompetenciesForUserInGivenList(context.getUserId(),
              CollectionUtils.convertToSqlArrayOfString(competencyList));
      competencyList.removeAll(completedCompetenciesByUser);
      if (!competencyList.isEmpty()) {
        userCompetencyCompletionDao
            .markCompetencyCompletedOrMastered(context.getUserId(), competencyList,
                COMPETENCY_STATUS_MASTERED);
      }
    }
  }

  private boolean isCompetencyMastered() {
    return context.getScore() != null && context.getScore() >= DbLookupUtility.getInstance()
        .thresholdForCompetencyCompletionBasedOnAssessment();
  }

}
