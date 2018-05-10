package org.gooru.navigatemap.processor.next.pathfinder;

import java.util.List;

import org.gooru.navigatemap.app.components.utilities.DbLookupUtility;
import org.gooru.navigatemap.infra.data.AlternatePath;
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
        if (context.getContentAddress().isOnMainPath()
            && context.getContentAddress().getCurrentItemType() == CurrentItemType.Assessment) {
            if (isCompetencyCompleted()) {
                markCompetencyCompletedForUser();
            }
        }
    }

    void handleCompetencyMastery(AlternatePath alternatePath) {
        if (context.getContentAddress().isOnAlternatePath()
            && context.getContentAddress().getCurrentItemType() == CurrentItemType.Assessment && alternatePath
            .isSuggestionSysemSuggestion() && alternatePath.isSuggestionSignatureAssessment()) {
            if (isCompetencyCompleted()) {
                markCompetencyMasteredForUser();
            }
        }
    }

    private void markCompetencyMasteredForUser() {
        List<String> competencyList = fetchCompetenciesForCollection();
        if (!competencyList.isEmpty()) {
            UserCompetencyCompletionDao userCompetencyCompletionDao = dbi.onDemand(UserCompetencyCompletionDao.class);
            List<String> masteredCompetenciesByUser = userCompetencyCompletionDao
                .findCompetenciesBasedOnCompletionStatusForUserInGivenList(context.getUserId(),
                    CollectionUtils.convertToSqlArrayOfString(competencyList), COMPETENCY_STATUS_MASTERED);
            competencyList.removeAll(masteredCompetenciesByUser);
            if (!competencyList.isEmpty()) {
                userCompetencyCompletionDao.markCompetencyCompletedOrMastered(context.getUserId(), competencyList,
                    COMPETENCY_STATUS_MASTERED);
            }
        }

    }

    List<String> fetchCompetenciesForCollection() {
        // TODO: Do we fetch GUT codes or framework codes
        ContentFinderDao finderDao = dbi.onDemand(ContentFinderDao.class);
        if (!areCompetenciesFetched) {
            String competenciesForCollection = finderDao.findCompetenciesForCollection(context.getContentAddress().getCourse(),
                context.getContentAddress().getUnit(), context.getContentAddress().getLesson(),
                context.getContentAddress().getCollection());
            competencies = TaxonomyParserHelper.parseCollectionTaxonomy(context.getContentAddress(), competenciesForCollection);
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
