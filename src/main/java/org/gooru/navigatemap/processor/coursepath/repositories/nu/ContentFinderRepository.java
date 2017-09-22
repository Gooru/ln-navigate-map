package org.gooru.navigatemap.processor.coursepath.repositories.nu;

import java.util.List;

import org.gooru.navigatemap.processor.data.ContentAddress;
import org.gooru.navigatemap.processor.data.FinderContext;
import org.gooru.navigatemap.processor.data.SignatureResource;

/**
 * @author ashish on 8/5/17.
 */
public interface ContentFinderRepository {
    boolean validateContentAddress(ContentAddress currentContentAddress);

    ContentAddress findFirstNotCompletedContentInCourse(FinderContext finderContext);

    ContentAddress fetchNextItem(FinderContext finderContext);

    ContentAddress findNextContentFromCULWithoutSkipLogicAndAlternatePaths(ContentAddress currentContentAddress,
        FinderContext finderContext);

    /*
     * Note that will not assume that competency is not mastered. It will check for all competencies for that
     * assessment first to validate if at least one of them is not completed, and then will suggest on that.
     * If all of them are completed, then it may return empty or in case we are out of suggestions, it will return
     * empty list
     */
    List<SignatureResource> findResourceSuggestionsForAssessment(FinderContext finderContext);

    /*
     * This will mark the competencies associated with current items to be completed only if they are not already
     * completed.
     */
    void markCompetencyCompletedForUser(FinderContext finderContext);
}
