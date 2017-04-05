package org.gooru.navigatemap.processor.coursepath.repositories;

import org.gooru.navigatemap.processor.data.ContentAddress;
import org.gooru.navigatemap.processor.data.RequestContext;

/**
 * @author ashish on 5/4/17.
 */
public interface NavigateService {

    ContentAddress navigateNext(ContentAddress contentAddress, RequestContext requestContext);

}
