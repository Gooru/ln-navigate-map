package org.gooru.navigatemap.processor.next.pathfinder;

import java.util.List;

import org.gooru.navigatemap.app.components.utilities.DbLookupUtility;
import org.gooru.navigatemap.infra.data.CurrentItemType;
import org.gooru.navigatemap.infra.utilities.CollectionUtils;
import org.skife.jdbi.v2.DBI;

/**
 * @author ashish on 8/5/18.
 */
class CompetencyCompletionHandler {

    private final PathFinderContext context;
    private final DBI dbi;
    private List<String> competencies;
    private boolean areCompetenciesFetched;

    private static final int COMPETENCY_STATUS_COMPLETED = 4;
    private static final int COMPETENCY_STATUS_MASTERED = 5;

    CompetencyCompletionHandler(DBI dbi, PathFinderContext context) {
        this.dbi = dbi;
        this.context = context;
        areCompetenciesFetched = false;
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
                listOfListOfComps = finderDao.findCompetenciesForCollection(context.getContentAddress().getCourse(),
                    context.getContentAddress().getUnit(), context.getContentAddress().getLesson(),
                    context.getContentAddress().getCollection());
            }
            if (listOfListOfComps != null && !listOfListOfComps.isEmpty()) {
                competencies = listOfListOfComps.get(0);
            }
            areCompetenciesFetched = true;
        }
        return competencies;
    }

    private void markCompetencyCompletedForUser() {
        List<String> competencyList = fetchCompetenciesForCollection();
        if (!competencyList.isEmpty()) {
            UserCompetencyCompletionDao userCompetencyCompletionDao = dbi.onDemand(UserCompetencyCompletionDao.class);
            List<String> completedCompetenciesByUser = userCompetencyCompletionDao
                .findCompletedOrMasteredCompetenciesForUserInGivenList(context.getUserId(),
                    CollectionUtils.convertToSqlArrayOfString(competencyList));
            competencyList.removeAll(completedCompetenciesByUser);
            if (!competencyList.isEmpty()) {
                userCompetencyCompletionDao.markCompetencyCompletedOrMastered(context.getUserId(), competencyList,
                    COMPETENCY_STATUS_COMPLETED);
            }
        }
    }

    boolean isCompetencyCompleted() {
        return context.getScore() != null && context.getScore() >= DbLookupUtility.getInstance()
            .thresholdForCompetencyCompletionBasedOnAssessment();
    }

}
