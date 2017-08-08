package org.gooru.navigatemap.processor.coursepath.repositories.nu;

import java.util.List;

import org.gooru.navigatemap.constants.HttpConstants;
import org.gooru.navigatemap.exceptions.HttpResponseWrapperException;
import org.gooru.navigatemap.processor.coursepath.repositories.dao.AlternatePathNUStrategyDao;
import org.gooru.navigatemap.processor.coursepath.repositories.dao.ContentFinderDao;
import org.gooru.navigatemap.processor.coursepath.repositories.dao.UserCompetencyCompletionDao;
import org.gooru.navigatemap.processor.data.*;
import org.skife.jdbi.v2.DBI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author ashish on 8/5/17.
 */
final class ContentFinderDelegate {

    private final FinderContext finderContext;
    private final DBI dbi;
    private static final Logger LOGGER = LoggerFactory.getLogger(ContentFinderDelegate.class);
    private final ContentFinderDao finderDao;
    private final AlternatePathNUStrategyDao alternatePathNUStrategyDao;
    private final UserCompetencyCompletionDao userCompetencyCompletionDao;

    ContentFinderDelegate(FinderContext finderContext, DBI dbi) {
        this.finderContext = finderContext;
        this.dbi = dbi;
        finderDao = this.dbi.onDemand(ContentFinderDao.class);
        alternatePathNUStrategyDao = this.dbi.onDemand(AlternatePathNUStrategyDao.class);
        userCompetencyCompletionDao = this.dbi.onDemand(UserCompetencyCompletionDao.class);
    }

    ContentAddress findFirstEligibleContentInCourse() {
        List<String> lessons;
        List<ContentAddress> contentAddresses;

        String course = finderContext.getCurrentAddress().getCourse();
        List<String> units = finderDao.findUnitsInCourse(course);
        final ContentCompletionChecker contentCompletionChecker =
            new ContentCompletionChecker(finderDao, userCompetencyCompletionDao, finderContext.getUser());

        for (String unit : units) {
            lessons = finderDao.findLessonsInCU(course, unit);
            for (String lesson : lessons) {
                contentAddresses = finderDao.findCollectionsInCUL(course, unit, lesson);
                if (contentAddresses != null && !contentAddresses.isEmpty()) {
                    for (ContentAddress contentAddress : contentAddresses) {
                        if (contentCompletionChecker.isContentNotCompletedOnMainPath(contentAddress)) {
                            contentAddress.populateCurrentItemsFromCollections();
                            return contentAddress;
                        }
                    }
                }
            }
        }
        return new ContentAddress();
    }

    ContentAddress findNextEligibleContentInCourse() {
        if (!finderContext.getCurrentAddress().isOnAlternatePath() && finderContext.getState() == State.Start
            && finderContext.getCurrentAddress().getCurrentItem() == null) {
            return findFirstContentInLesson();
        } else if (!finderContext.getCurrentAddress().isOnAlternatePath()
            && finderContext.getState() == State.ContentServed) {
            return findNextContentInLesson();
        } else if (!finderContext.getCurrentAddress().isOnAlternatePath()
            && finderContext.getState() == State.ContentEndSuggested) {
            return findAlternatePath();
        } else if (finderContext.getCurrentAddress().isOnAlternatePath()
            && finderContext.getState() == State.ContentServed) {
            return findNextContentInLesson();
        } else {
            LOGGER.warn("Not sure what to do. Will abort");
            throw new HttpResponseWrapperException(HttpConstants.HttpStatus.BAD_REQUEST,
                "Invalid state supplied in context");
        }
    }

    private ContentAddress findAlternatePath() {
        List<AlternatePath> paths;
        ContentAddress address = finderContext.getCurrentAddress();
        if (finderContext.getUserClass() == null) {
            paths = alternatePathNUStrategyDao
                .findResourceAlternatePathsForCULAndUser(address.getCourse(), address.getUnit(), address.getLesson(),
                    address.getCurrentItem(), finderContext.getUser());
        } else {
            paths = alternatePathNUStrategyDao
                .findResourceAlternatePathsForCULAndUserInClass(address.getCourse(), address.getUnit(),
                    address.getLesson(), address.getCurrentItem(), finderContext.getUser(),
                    finderContext.getUserClass());
        }
        if (paths != null && !paths.isEmpty()) {
            AlternatePath targetPath = selectAlternatePath(paths);
            return getContentAddressFromAlternatePath(address, targetPath);
        } else {
            return findNextContentItem();
        }
    }

    private ContentAddress getContentAddressFromAlternatePath(ContentAddress address, AlternatePath targetPath) {
        ContentAddress resultAddress = new ContentAddress(address);
        resultAddress.setCurrentItem(targetPath.getTargetResourceId().toString());
        resultAddress.setCurrentItemType(CurrentItemType.builder(targetPath.getTargetContentType()));
        if (targetPath.getTargetContentSubtype() != null) {
            resultAddress.setCurrentItemSubtype(CurrentItemSubtype.builder(targetPath.getTargetContentSubtype()));
        }
        resultAddress.setPathId(targetPath.getId());
        return resultAddress;
    }

    private AlternatePath selectAlternatePath(List<AlternatePath> paths) {
        return paths.get(0);
    }

    private ContentAddress findNextContentInLesson() {
        return findNextContentItem();
    }

    private ContentAddress findFirstContentInLesson() {
        return findNextContentItem();
    }

    private ContentAddress findNextContentItem() {
        List<String> lessons;
        List<ContentAddress> contentAddresses;
        ContentAddress address = finderContext.getCurrentAddress();
        final ContentCompletionChecker contentCompletionChecker =
            new ContentCompletionChecker(finderDao, userCompetencyCompletionDao, finderContext.getUser());

        List<String> units = finderDao.findNextUnitsInCourse(address.getCourse(), address.getUnit());
        for (String unit : units) {
            if (unit.equalsIgnoreCase(address.getUnit())) {
                lessons = finderDao.findNextLessonsInCU(address.getCourse(), unit, address.getLesson());
            } else {
                lessons = finderDao.findLessonsInCU(address.getCourse(), unit);
            }
            for (String lesson : lessons) {
                if (lesson.equalsIgnoreCase(address.getLesson()) && unit.equalsIgnoreCase(address.getUnit())
                    && address.getCollection() != null) {
                    contentAddresses =
                        finderDao.findNextCollectionsInCUL(address.getCourse(), unit, lesson, address.getCollection());
                } else {
                    contentAddresses = finderDao.findCollectionsInCUL(address.getCourse(), unit, lesson);
                }
                if (contentAddresses != null && !contentAddresses.isEmpty()) {
                    for (ContentAddress contentAddress : contentAddresses) {
                        if (contentCompletionChecker.isContentNotCompletedOnMainPath(contentAddress)) {
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
