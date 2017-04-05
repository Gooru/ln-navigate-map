package org.gooru.navigatemap.processor.coursepath.repositories;

import org.gooru.navigatemap.processor.data.ContentAddress;
import org.gooru.navigatemap.processor.data.RequestContext;

/**
 * @author ashish on 5/4/17.
 */
final class NavigateServiceImpl implements NavigateService {

    private final ContentFilterRepository filterRepository;
    private final ContentFinderRepository finderRepository;

    NavigateServiceImpl(ContentFilterRepository contentFilterRepository,
        ContentFinderRepository contentFinderRepository) {
        this.filterRepository = contentFilterRepository;
        this.finderRepository = contentFinderRepository;
    }

    @Override
    public ContentAddress navigateNext(ContentAddress contentAddress, RequestContext requestContext) {
        throw new AssertionError("Not implemented");
    }
}
