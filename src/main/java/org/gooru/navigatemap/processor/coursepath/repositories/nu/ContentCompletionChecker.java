package org.gooru.navigatemap.processor.coursepath.repositories.nu;

import java.util.List;

import org.gooru.navigatemap.processor.coursepath.repositories.dao.AlternatePathNUStrategyDao;
import org.gooru.navigatemap.processor.coursepath.repositories.dao.ContentFinderDao;
import org.gooru.navigatemap.processor.coursepath.repositories.helpers.TaxonomyParserHelper;
import org.gooru.navigatemap.processor.data.CollectionType;
import org.gooru.navigatemap.processor.data.ContentAddress;
import org.gooru.navigatemap.processor.utilities.CollectionUtils;

/**
 * @author ashish on 8/5/17.
 */
public class ContentCompletionChecker {

    private final String user;
    private final AlternatePathNUStrategyDao alternatePathNUStrategyDao;
    private final ContentFinderDao finderDao;
    private final ContentAddress contentAddress;

    public ContentCompletionChecker(ContentAddress contentAddress, ContentFinderDao finderDao,
        AlternatePathNUStrategyDao alternatePathNUStrategyDao, String user) {
        this.contentAddress = contentAddress;
        this.finderDao = finderDao;
        this.alternatePathNUStrategyDao = alternatePathNUStrategyDao;
        this.user = user;
    }

    public boolean isContentNotCompletedOnMainPath() {
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
                List<String> completedCompetenciesByUser = alternatePathNUStrategyDao
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
