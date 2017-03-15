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
}
