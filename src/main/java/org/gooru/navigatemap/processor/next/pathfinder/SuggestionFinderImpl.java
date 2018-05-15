package org.gooru.navigatemap.processor.next.pathfinder;

import java.util.Collections;
import java.util.List;

import org.gooru.navigatemap.app.components.utilities.DbLookupUtility;
import org.gooru.navigatemap.infra.utilities.CollectionUtils;
import org.skife.jdbi.v2.DBI;

/**
 * @author ashish on 11/5/18.
 */
public class SuggestionFinderImpl implements SuggestionFinder {

    private final DBI dbi;

    SuggestionFinderImpl(DBI dbi) {

        this.dbi = dbi;
    }

    @Override
    public List<String> findSignatureCollectionsForCompetencies(PathFinderContext context, List<String> competencies) {
        /* Signature Collections suggestions is dependent on score range */
        if (competencies == null || competencies.isEmpty()) {
            return Collections.emptyList();
        }

        SuggestionFinderDao dao = dbi.onDemand(SuggestionFinderDao.class);
        // Find signature collections honoring the score range
        List<String> signatureCollectionsForCompetency =
            dao.findSignatureCollectionForSpecifiedCompetenciesAndScoreRange(
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
                CollectionUtils.intersect(signatureCollectionsForCompetency, signatureItemsAlreadyAddedByUser);
            return CollectionUtils.uniqueMaintainOrder(signatureItemsNotAddedByUser);
        }
        return Collections.emptyList();
    }

    @Override
    public List<String> findSignatureAssessmentsForCompetencies(PathFinderContext context, List<String> competencies) {
        /* Suggestion for SignatureAssessment is not dependent on score */
        if (competencies == null || competencies.isEmpty()) {
            return Collections.emptyList();
        }
        SuggestionFinderDao dao = dbi.onDemand(SuggestionFinderDao.class);
        List<String> signatureAssessmentsForCompetencies = dao.findSignatureAssessmentsForSpecifiedCompetencies(
            CollectionUtils.convertToSqlArrayOfString(competencies));

        if (signatureAssessmentsForCompetencies != null && !signatureAssessmentsForCompetencies.isEmpty()) {
            List<String> signatureItemsAlreadyAddedByUser =
                dao.findSignatureItemsAddedByUserFromList(context.getUserId(),
                    CollectionUtils.convertToSqlArrayOfUUID(signatureAssessmentsForCompetencies));
            List<String> signatureItemsNotAddedByUser =
                CollectionUtils.intersect(signatureAssessmentsForCompetencies, signatureItemsAlreadyAddedByUser);
            return CollectionUtils.uniqueMaintainOrder(signatureItemsNotAddedByUser);
        }
        return Collections.emptyList();
    }
}
