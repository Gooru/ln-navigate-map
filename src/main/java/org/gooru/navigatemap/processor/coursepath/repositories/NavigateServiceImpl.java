package org.gooru.navigatemap.processor.coursepath.repositories;

import org.gooru.navigatemap.processor.data.ContentAddress;
import org.gooru.navigatemap.processor.data.FinderContext;
import org.gooru.navigatemap.processor.data.RequestContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The purpose of this class is to find the next content. Note that for the cases of start of course or when suggestions
 * are off, the control should not come here.
 *
 * @author ashish on 5/4/17.
 */
final class NavigateServiceImpl implements NavigateService {

    private final ContentFilterRepository filterRepository;
    private final ContentFinderRepository finderRepository;
    private static final Logger LOGGER = LoggerFactory.getLogger(NavigateService.class);

    NavigateServiceImpl(ContentFilterRepository contentFilterRepository,
        ContentFinderRepository contentFinderRepository) {
        this.filterRepository = contentFilterRepository;
        this.finderRepository = contentFinderRepository;
    }

    @Override
    public ContentAddress navigateNext(FinderContext finderContext) {
        throw new AssertionError("Not implemented");
    }

    private ContentAddress process(ContentAddress contentAddress, RequestContext requestContext) {
        switch (requestContext.getState()) {
        case Start:
        case Continue:
            LOGGER.warn("Invalid state flow in navigation service. Should not be Start/Continue");
            throw new AssertionError("Start/Continue state in navigation service");
        case LessonStartSuggested:
        case LessonEndSuggested:
        case ContentEndSuggested:
        case ContentServed:
            break;
        }
        return null;
    }

}
