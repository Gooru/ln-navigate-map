package org.gooru.navigatemap.processor.coursepath.repositories.global;

import java.util.List;

import org.gooru.navigatemap.processor.coursepath.repositories.AbstractUsageRepository;
import org.gooru.navigatemap.processor.data.AlternatePath;

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

    @Override
    public List<AlternatePath> filterChildPathsNotPlayedByUser(List<AlternatePath> childPaths, String user) {
        // TODO: Not implemented
        return childPaths;
    }
}
