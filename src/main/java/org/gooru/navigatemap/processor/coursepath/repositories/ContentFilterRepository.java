package org.gooru.navigatemap.processor.coursepath.repositories;

import java.util.List;

/**
 * @author ashish on 15/3/17.
 */
public interface ContentFilterRepository {

    List<String> filterBAForNotAddedByUser(List<String> inputBAList, String userId);
}
