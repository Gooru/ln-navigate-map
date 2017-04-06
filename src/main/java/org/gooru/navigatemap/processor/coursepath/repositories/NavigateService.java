package org.gooru.navigatemap.processor.coursepath.repositories;

import org.gooru.navigatemap.processor.data.ContentAddress;
import org.gooru.navigatemap.processor.data.FinderContext;

/**
 * @author ashish on 5/4/17.
 */
public interface NavigateService {

    ContentAddress navigateNext(FinderContext finderContext);

}
