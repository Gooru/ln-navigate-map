package org.gooru.navigatemap.processor.coursepath.repositories.nu;

import java.util.List;

import org.gooru.navigatemap.processor.coursepath.repositories.dao.ContentFinderDao;
import org.gooru.navigatemap.processor.coursepath.repositories.dao.UserCompetencyCompletionDao;
import org.gooru.navigatemap.processor.coursepath.repositories.helpers.TaxonomyParserHelper;
import org.gooru.navigatemap.processor.data.CollectionType;
import org.gooru.navigatemap.processor.data.ContentAddress;
import org.gooru.navigatemap.processor.utilities.CollectionUtils;

/**
 * @author ashish on 8/5/17.
 */
class ContentCompletionChecker {

    private final String user;
    private final ContentFinderDao finderDao;
    private final UserCompetencyCompletionDao userCompetencyCompletionDao;

    ContentCompletionChecker(ContentFinderDao finderDao, UserCompetencyCompletionDao userCompetencyCompletionDao,
        String user) {
        this.finderDao = finderDao;
        this.user = user;
        this.userCompetencyCompletionDao = userCompetencyCompletionDao;
    }

    boolean isContentNotCompletedOnMainPath(ContentAddress contentAddress) {
        if (contentAddress.getCollectionType() != CollectionType.Assessment) {
            return true;
        } else {
            String competencies = finderDao
                .findCompetenciesForCollection(contentAddress.getCourse(), contentAddress.getUnit(),
                    contentAddress.getLesson(), contentAddress.getCollection());

            List<String> competencyList = TaxonomyParserHelper.parseCollectionTaxonomy(contentAddress, competencies);
            if (competencyList.isEmpty()) {
                return true;
            } else {
                List<String> completedCompetenciesByUser = userCompetencyCompletionDao
                    .findCompletedCompetenciesForUserInGivenList(user,
                        CollectionUtils.convertToSqlArrayOfString(competencyList));
                competencyList.removeAll(completedCompetenciesByUser);
                if (!competencyList.isEmpty()) {
                    return true;
                }
            }
        }
        return false;
    }

}
