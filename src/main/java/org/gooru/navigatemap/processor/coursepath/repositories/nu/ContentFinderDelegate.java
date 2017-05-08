package org.gooru.navigatemap.processor.coursepath.repositories.nu;

import java.util.List;

import org.gooru.navigatemap.processor.coursepath.repositories.dao.AlternatePathNUStrategyDao;
import org.gooru.navigatemap.processor.coursepath.repositories.dao.ContentFinderDao;
import org.gooru.navigatemap.processor.data.ContentAddress;
import org.gooru.navigatemap.processor.data.FinderContext;
import org.skife.jdbi.v2.DBI;

/**
 * @author ashish on 8/5/17.
 */
final class ContentFinderDelegate {

    private final FinderContext finderContext;
    private final DBI dbi;

    ContentFinderDelegate(FinderContext finderContext, DBI dbi) {
        this.finderContext = finderContext;
        this.dbi = dbi;
    }

    ContentAddress findFirstEligibleContentInCourse() {
        List<String> lessons;
        List<ContentAddress> contentAddresses;
        ContentFinderDao finderDao = dbi.onDemand(ContentFinderDao.class);
        AlternatePathNUStrategyDao alternatePathNUStrategyDao = dbi.onDemand(AlternatePathNUStrategyDao.class);

        String course = finderContext.getCurrentAddress().getCourse();
        List<String> units = finderDao.findUnitsInCourse(course);

        for (String unit : units) {
            lessons = finderDao.findLessonsInCU(course, unit);
            for (String lesson : lessons) {
                contentAddresses = finderDao.findCollectionsInCUL(course, unit, lesson);
                if (contentAddresses != null && !contentAddresses.isEmpty()) {
                    for (ContentAddress contentAddress : contentAddresses) {
                        final ContentCompletionChecker contentCompletionChecker =
                            new ContentCompletionChecker(contentAddress, finderDao, alternatePathNUStrategyDao,
                                finderContext.getUser());
                        if (contentCompletionChecker.isContentNotCompletedOnMainPath()) {
                            contentAddress.populateCurrentItemsFromCollections();
                            return contentAddress;
                        }
                    }
                }
            }
        }
        return new ContentAddress();
    }
}
