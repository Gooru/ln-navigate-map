package org.gooru.navigatemap.processor.next.pathfinder;

import java.util.List;

import org.gooru.navigatemap.app.constants.HttpConstants;
import org.gooru.navigatemap.app.exceptions.HttpResponseWrapperException;
import org.gooru.navigatemap.infra.data.AlternatePath;
import org.gooru.navigatemap.infra.data.ContentAddress;
import org.skife.jdbi.v2.DBI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * If user has explicitly asked for collection either on main path or on alternate path, we serve it without
 * considering skip logic.
 * If user asks to start a lesson, then we assume that user wants to study this lesson (whatever is visible of this
 * lesson). In this case also, we do not apply skip logic (based on competency), but we find the first visible
 * content of this lesson. However, teacher suggestions are treated as pre for a content. So once we get the content,
 * we check if any teacher suggestion exists for this content. If there is, we return first one. If there is none, we
 * go ahead and serve the content that we originally fetched.
 *
 * @author ashish on 4/5/18.
 */
class ExplicitStartPathFinderService implements PathFinder {

    private final DBI dbi;
    private final ContentFinderDao finderDao;
    private PathFinderContext context;
    private ContentAddress specifiedContentAddress;
    private static final Logger LOGGER = LoggerFactory.getLogger(ExplicitStartPathFinderService.class);

    ExplicitStartPathFinderService(DBI dbi) {
        this.dbi = dbi;
        finderDao = dbi.onDemand(ContentFinderDao.class);
    }

    @Override
    public PathFinderResult findPath(PathFinderContext context) {
        this.context = context;
        specifiedContentAddress = context.getContentAddress();
        ContentAddress result;
        validateCULValues(specifiedContentAddress);
        if (specifiedContentAddress.getCollection() == null) {
            // This is user asking to start a lesson
            LOGGER.debug("User asking to start a lesson.");
            result = fetchFirstItemFromLesson();
        } else if (specifiedContentAddress.isOnTeacherOrSystemPath()) {
            // User is asking to play a content which was already added to alternate path
            LOGGER.debug("User asking to start a system/teacher suggestion.");
            result = fetchSpecifiedAlternatePath();
        } else {
            // User wanted to play something on course path
            LOGGER.debug("User asking to start an item on main path.");
            result = fetchSpecifiedContentFromCoursePath();
        }
        return new PathFinderResult(result);
    }

    private ContentAddress fetchFirstItemFromLesson() {
        // Note that Teacher suggestions are pre to actual item. So this could be teacher suggestion on first visible
        // item, or it could be first visible item. If there is no visible item in lesson, then it is an error.
        ContentVerifier visibilityVerifier =
            ContentVerifierBuilder.buildContentVisibilityVerifier(context.getClassId(), dbi);

        ContentAddress firstVisibleItem = visibilityVerifier.findFirstVerifiedContent(finderDao
            .findFirstCollectionInLesson(specifiedContentAddress.getCourse(), specifiedContentAddress.getUnit(),
                specifiedContentAddress.getLesson()));

        if (firstVisibleItem == null) {
            throw new HttpResponseWrapperException(HttpConstants.HttpStatus.BAD_REQUEST,
                "No visible content in lesson");
        }

        ContentAddress teacherSuggestedItemForContext =
            findFirstTeacherSuggestedItemForSpecifiedContext(firstVisibleItem);
        if (teacherSuggestedItemForContext != null) {
            return teacherSuggestedItemForContext;
        } else {
            return firstVisibleItem;
        }
    }

    private ContentAddress findFirstTeacherSuggestedItemForSpecifiedContext(ContentAddress firstVisibleItem) {
        if (context.getClassId() == null) {
            return null; // No teacher path for IL
        }
        AlternatePathDao alternatePathDao = dbi.onDemand(AlternatePathDao.class);
        List<AlternatePath> teacherPathsForContext = alternatePathDao
            .findTeacherPathsForSpecifiedContext(firstVisibleItem, context.getUserId(),
                context.getClassId().toString());
        if (teacherPathsForContext == null || teacherPathsForContext.isEmpty()) {
            return null;
        } else {
            return teacherPathsForContext.get(0).toContentAddress();
        }
    }

    private ContentAddress fetchSpecifiedAlternatePath() {
        // Find the AlternatePath and map it to ContentAddress. No need to check visibility. Just return.
        // In case of failure, throw.
        AlternatePathDao alternatePathDao = dbi.onDemand(AlternatePathDao.class);
        AlternatePath specifiedPath;
        if (context.getClassId() == null) {
            specifiedPath = alternatePathDao
                .findAlternatePathByPathIdAndUserForIL(specifiedContentAddress.getPathId(), context.getUserId());
        } else {
            specifiedPath = alternatePathDao
                .findAlternatePathByPathIdAndUserInClass(specifiedContentAddress.getPathId(), context.getUserId(),
                    context.getClassId().toString());
        }

        if (specifiedPath == null) {
            throw new HttpResponseWrapperException(HttpConstants.HttpStatus.BAD_REQUEST, "Invalid content");
        }
        return specifiedPath.toContentAddress();
    }

    private ContentAddress fetchSpecifiedContentFromCoursePath() {
        ContentAddress result = finderDao
            .findCULC(specifiedContentAddress.getCourse(), specifiedContentAddress.getUnit(),
                specifiedContentAddress.getLesson(), specifiedContentAddress.getCollection());
        validateVisibility(result);
        return result;
    }

    private void validateVisibility(ContentAddress result) {
        ContentVerifier visibilityVerifier =
            ContentVerifierBuilder.buildContentVisibilityVerifier(context.getClassId(), dbi);
        if (result == null || !visibilityVerifier.isContentVerified(result)) {
            throw new HttpResponseWrapperException(HttpConstants.HttpStatus.BAD_REQUEST, "Invalid content");
        }
    }

    private void validateCULValues(ContentAddress contentAddress) {
        if (contentAddress.getCourse() == null || contentAddress.getUnit() == null
            || contentAddress.getLesson() == null) {
            throw new HttpResponseWrapperException(HttpConstants.HttpStatus.BAD_REQUEST, "Invalid CUL info in context");
        }
    }

}
