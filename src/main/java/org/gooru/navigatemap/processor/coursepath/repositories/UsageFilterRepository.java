package org.gooru.navigatemap.processor.coursepath.repositories;

import java.util.List;

/**
 * @author ashish on 3/4/17.
 */
public interface UsageFilterRepository {

    List<String> filterBAForNotUsedByUser(List<String> inputBAList, String userId);

    List<String> filterPreTestForNotUsedByUser(List<String> inputPreTestList, String userId);

    List<String> filterPostTestForNotUsedByUser(List<String> inputPostTestList, String userId);

}
