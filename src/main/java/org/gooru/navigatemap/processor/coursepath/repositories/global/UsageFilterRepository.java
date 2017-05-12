package org.gooru.navigatemap.processor.coursepath.repositories.global;

import java.util.List;

import org.gooru.navigatemap.processor.data.AlternatePath;

/**
 * @author ashish on 3/4/17.
 */
public interface UsageFilterRepository {

    List<String> filterBAForNotUsedByUser(List<String> inputBAList, String userId);

    List<String> filterPreTestForNotUsedByUser(List<String> inputPreTestList, String userId);

    List<String> filterPostTestForNotUsedByUser(List<String> inputPostTestList, String userId);

    List<AlternatePath> filterChildPathsNotPlayedByUser(List<AlternatePath> childPaths, String user);
}
