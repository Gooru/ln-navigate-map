package org.gooru.navigatemap.processor.next.pathfinder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.gooru.navigatemap.infra.data.CurrentItemType;
import org.gooru.navigatemap.infra.utilities.CollectionUtils;
import org.skife.jdbi.v2.DBI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author ashish on 8/5/18.
 */
class CompetencyCompletionHandler {

  private final PathFinderContext context;
  private final DBI dbi;
  private List<String> competencies;
  private boolean areCompetenciesFetched;

  private static final int COMPETENCY_STATUS_COMPLETED = 4;
  private static final double DEFAULT_COMPLETION_SCORE = 80.00;
  private static final Logger LOGGER = LoggerFactory.getLogger(CompetencyCompletionHandler.class);


  CompetencyCompletionHandler(DBI dbi, PathFinderContext context) {
    this.dbi = dbi;
    this.context = context;
    areCompetenciesFetched = false;
  }
  private TenantSettingService tenantSettingService = 
      new TenantSettingService();
  
  private double getCompletionScoreThreshold() {
    String tenantId = context.getTenantId();
    if(tenantId != null && !tenantId.isEmpty()) {
      try  {
        Double completionScore = tenantSettingService.fetchTenantCompletionScore(tenantId);
        return completionScore;
      } catch(Exception e) {
        LOGGER.error("Invalid completion score for tenant settings '{}'",tenantId);
      } 
    }
    // returning default score incase of error or null
    return DEFAULT_COMPLETION_SCORE;
  }

  void handleCompetencyCompletion() {
    if ((context.getContentAddress().isOnMainPath() || context.getContentAddress().isOnRoute0())
        && context.getContentAddress().getCurrentItemType() == CurrentItemType.Assessment) {
      if (isCompetencyCompleted()) {
        markCompetencyCompletedForUser();
      }
    }
  }

  List<String> fetchCompetenciesForCollection() {
    ContentFinderDao finderDao = dbi.onDemand(ContentFinderDao.class);
    if (!areCompetenciesFetched) {
      List<List<String>> listOfListOfComps;
      if (context.getContentAddress().isOnRoute0()) {
        listOfListOfComps =
            finderDao.findCompetenciesForCollection(context.getContentAddress().getCurrentItem());
      } else {
        listOfListOfComps = finderDao
            .findCompetenciesForCollection(context.getContentAddress().getCourse(),
                context.getContentAddress().getUnit(), context.getContentAddress().getLesson(),
                context.getContentAddress().getCollection());
      }
      if (listOfListOfComps != null && !listOfListOfComps.isEmpty()) {
        competencies = listOfListOfComps.get(0);
        if (competencies == null) {
          competencies = Collections.emptyList();
        }
      }
      areCompetenciesFetched = true;
    }
    return Collections.unmodifiableList(competencies);
  }

  private void markCompetencyCompletedForUser() {
    List<String> competencyList = new ArrayList<>(fetchCompetenciesForCollection());
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
                COMPETENCY_STATUS_COMPLETED);
      }
    }
  }

  boolean isCompetencyCompleted() {
    return context.getScore() != null && context.getScore() >= getCompletionScoreThreshold();
  }

}
