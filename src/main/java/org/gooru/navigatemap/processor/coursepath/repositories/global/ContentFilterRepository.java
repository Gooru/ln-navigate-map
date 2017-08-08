package org.gooru.navigatemap.processor.coursepath.repositories.global;

import java.util.List;

/**
 * @author ashish on 15/3/17.
 */
public interface ContentFilterRepository {

    List<String> filterBAForNotAddedByUser(List<String> inputBAList, String userId);

    List<String> filterPreTestForNotAddedByUser(List<String> inputPreTestList, String userId);

    List<String> filterPostTestForNotAddedByUser(List<String> inputPostTestList, String userId);
}
