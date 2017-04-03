package org.gooru.navigatemap.processor.coursepath.repositories;

import java.util.List;

import org.gooru.navigatemap.processor.coursepath.repositories.dao.ContentFinderDao;
import org.gooru.navigatemap.processor.utilities.CollectionUtils;

/**
 * @author ashish on 15/3/17.
 */
final class ContentFilterRepositoryImpl extends AbstractContentRepository implements ContentFilterRepository {

    @Override
    public List<String> filterBAForNotAddedByUser(List<String> inputBAList, String userId) {
        ContentFinderDao dao = dbi.onDemand(ContentFinderDao.class);
        List<String> baAddedByUser =
            dao.findBenchmarksAddedByUserFromList(userId, CollectionUtils.convertToSqlArrayOfUUID(inputBAList));

        List<String> baNotAddedByUser = CollectionUtils.intersect(inputBAList, baAddedByUser);
        return CollectionUtils.unique(baNotAddedByUser);
    }

    @Override
    public List<String> filterPreTestForNotAddedByUser(List<String> inputPreTestList, String userId) {
        ContentFinderDao dao = dbi.onDemand(ContentFinderDao.class);
        List<String> preTestAddedByUser =
            dao.findPreTestsAddedByUserFromList(userId, CollectionUtils.convertToSqlArrayOfUUID(inputPreTestList));

        List<String> preTestsNotAddedByUser = CollectionUtils.intersect(inputPreTestList, preTestAddedByUser);
        return CollectionUtils.unique(preTestsNotAddedByUser);
    }

    @Override
    public List<String> filterPostTestForNotAddedByUser(List<String> inputPostTestList, String userId) {
        ContentFinderDao dao = dbi.onDemand(ContentFinderDao.class);
        List<String> postTestsAddedByUser =
            dao.findPostTestsAddedByUserFromList(userId, CollectionUtils.convertToSqlArrayOfUUID(inputPostTestList));

        List<String> postTestsNotAddedByUser = CollectionUtils.intersect(inputPostTestList, postTestsAddedByUser);
        return CollectionUtils.unique(postTestsNotAddedByUser);
    }
}
