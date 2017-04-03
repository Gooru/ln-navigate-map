package org.gooru.navigatemap.processor.coursepath.repositories;

import java.util.List;

/**
 * @author ashish on 3/4/17.
 */
final class UsageFilterRepositoryImpl extends AbstractUsageRepository implements UsageFilterRepository {
    @Override
    public List<String> filterBAForNotUsedByUser(List<String> inputBAList, String userId) {
        throw new AssertionError("Not implemented");
    }

    @Override
    public List<String> filterPreTestForNotUsedByUser(List<String> inputPreTestList, String userId) {
        throw new AssertionError("Not implemented");
    }

    @Override
    public List<String> filterPostTestForNotUsedByUser(List<String> inputPostTestList, String userId) {
        throw new AssertionError("Not implemented");
    }
}
