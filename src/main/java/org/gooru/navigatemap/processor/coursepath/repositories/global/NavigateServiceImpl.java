package org.gooru.navigatemap.processor.coursepath.repositories.global;

import java.util.List;
import java.util.Objects;

import org.gooru.navigatemap.processor.data.*;
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
    private final UsageFilterRepository usageFilterRepository;
    private FinderContext finderContext;

    NavigateServiceImpl(ContentFilterRepository contentFilterRepository,
        ContentFinderRepository contentFinderRepository, UsageFilterRepository usageFilterRepository) {
        this.filterRepository = contentFilterRepository;
        this.finderRepository = contentFinderRepository;
        this.usageFilterRepository = usageFilterRepository;
    }

    @Override
    public ContentAddress navigateNext(FinderContext finderContext) {
        this.finderContext = finderContext;
        return process();
    }

    private ContentAddress process() {
        switch (finderContext.getState()) {
        case Start:
            if (finderContext.getRequestContext().needToStartLesson()) {
                return handleContentServed();
            }
            // Don't break here
        case Continue:
            LOGGER.warn("Invalid state flow in navigation service. Should not be Start/Continue");
            throw new AssertionError("Start/Continue state in navigation service");
        case LessonStartSuggested:
            return handleLessonStartSuggested();
        case LessonEndSuggested:
            return handleLessonEndSuggeted();
        case ContentEndSuggested:
            return handleContentEndSuggested();
        case ContentServed:
            return handleContentServed();
        default:
            LOGGER.warn("Invalid state in finder context, '{}'", finderContext.getState());
        }
        return null;
    }

    private ContentAddress handleContentServed() {
        // If we are not on alternate path, then we find the content on CUL, without getting done and let suggestions
        // machinery take care of it
        // If we are on alternate path, we have following cases
        // 1) If we have served Pre/Post, then backfill or BA will be taken care by suggestions machinery
        // 2) If we have served BA or backfill, then we may directly advance to next content
        return finderRepository.findNextContentFromCUL(finderContext.getCurrentAddress());
    }

    /*
     * We suggested BA/Backfill. Check if user has added any BA which is not yet attempted. If yes find that and
     * short circuit. Else find next content.
     */
    private ContentAddress handleContentEndSuggested() {
        // Find current path. We do not support content end suggestions on main path
        if (finderContext.getCurrentAddress().getPathId() == null) {
            LOGGER.error("Content end suggestions are not supported on main path");
            throw new IllegalStateException("Content end suggestions are not supported on main path");
        }

        AlternatePath currentPath;
        if (finderContext.getUserClass() == null) {
            currentPath =
                finderRepository.findAlternatePathForUser(finderContext.getCurrentAddress(), finderContext.getUser());
        } else {
            currentPath = finderRepository
                .findAlternatePathForUserInClass(finderContext.getCurrentAddress(), finderContext.getUser(),
                    finderContext.getUserClass());
        }

        Objects.requireNonNull(currentPath);
        List<AlternatePath> childPaths = null;
        // Find any child paths for this path for this user and Course (optionally class construct). The child path
        // could either be a backfill or a BA based on if current path is PostT or PreT
        if (currentPath.isTargetContentSubtypePostTest()) {
            childPaths = finderRepository.findChildPathsOfTypeBA(currentPath);
        } else if (currentPath.isTargetContentSubtypePreTest()) {
            childPaths = finderRepository.findChildPathsOfTypeBackfill(currentPath);
        } else {
            LOGGER.warn("Invalid current path obtained.");
        }

        if (childPaths != null && !childPaths.isEmpty()) {
            // Filter it for analytics data
            List<AlternatePath> filteredChildPaths =
                usageFilterRepository.filterChildPathsNotPlayedByUser(childPaths, finderContext.getUser());
            // If there is something to show, set it up in current item, w/o touching current address, set up short
            // circuit
            if (filteredChildPaths != null && !filteredChildPaths.isEmpty()) {
                AlternatePath targetPath = findATargetAlternatePath(filteredChildPaths);
                finderContext.setCurrentItem(targetPath.getTargetCollection(),
                    CollectionType.builder(targetPath.getTargetContentType()),
                    targetPath.getTargetContentSubtype() != null ?
                        CollectionSubtype.builder(targetPath.getTargetContentSubtype()) : null);
                finderContext.getCurrentAddress().setPathId(targetPath.getId());
                return finderContext.getCurrentAddress();
            }
        }
        // If there is nothing to show, use current address to find next content
        return finderRepository.findNextContentFromCUL(finderContext.getCurrentAddress());
    }

    /*
     * We suggested Post Test. Check if user has added any PT which is not yet attempted. If yes find that and
     * short circuit. Else find next content.
     */
    private ContentAddress handleLessonEndSuggeted() {
        // Find current path. We do not support lesson end suggestions on alternatepath
        if (finderContext.getCurrentAddress().isOnAlternatePath()) {
            LOGGER.error("Lesson end suggestions are not supported on alternate path");
            throw new IllegalStateException("Lesson end suggestions are not supported on alternate path");
        }

        List<AlternatePath> childPaths = finderRepository
            .findChildPathsOfTypePostTest(finderContext.getCurrentAddress(), finderContext.getUser(),
                finderContext.getUserClass());

        if (childPaths != null && !childPaths.isEmpty()) {
            // Filter it for analytics data
            List<AlternatePath> filteredChildPaths =
                usageFilterRepository.filterChildPathsNotPlayedByUser(childPaths, finderContext.getUser());
            // If there is something to show, set it up in current item, w/o touching current address, set up short
            // circuit
            if (filteredChildPaths != null && !filteredChildPaths.isEmpty()) {
                AlternatePath targetPath = findATargetAlternatePath(filteredChildPaths);
                finderContext.setCurrentItem(targetPath.getTargetCollection(),
                    CollectionType.builder(targetPath.getTargetContentType()),
                    CollectionSubtype.builder(targetPath.getTargetContentSubtype()));
                finderContext.getCurrentAddress().setPathId(targetPath.getId());
                return finderContext.getCurrentAddress();
            }
        }
        // If there is nothing to show, use current address to find next content
        return finderRepository.findNextContentFromCUL(finderContext.getCurrentAddress());
    }

    private ContentAddress handleLessonStartSuggested() {
        // Find current path. We do not support lesson start suggestions on alternatepath
        if (finderContext.getCurrentAddress().isOnAlternatePath()) {
            LOGGER.error("Lesson start suggestions are not supported on alternate path");
            throw new IllegalStateException("Lesson start suggestions are not supported on alternate path");
        }

        // Find any child paths for this path for this user and Course (optionally class construct). The child path
        // could either be a backfill or a BA based on if current path is PostT or PreT
        List<AlternatePath> childPaths = finderRepository
            .findChildPathsOfTypePreTest(finderContext.getCurrentAddress(), finderContext.getUser(),
                finderContext.getUserClass());

        if (childPaths != null && !childPaths.isEmpty()) {
            // Filter it for analytics data
            List<AlternatePath> filteredChildPaths =
                usageFilterRepository.filterChildPathsNotPlayedByUser(childPaths, finderContext.getUser());
            // If there is something to show, set it up in current item, w/o touching current address, set up short
            // circuit
            if (filteredChildPaths != null && !filteredChildPaths.isEmpty()) {
                AlternatePath targetPath = findATargetAlternatePath(filteredChildPaths);
                finderContext.setCurrentItem(targetPath.getTargetCollection(),
                    CollectionType.builder(targetPath.getTargetContentType()),
                    CollectionSubtype.builder(targetPath.getTargetContentSubtype()));
                finderContext.getCurrentAddress().setPathId(targetPath.getId());
                return finderContext.getCurrentAddress();
            }
        }
        // If there is nothing to show, use current address to find next content
        return finderRepository.findNextContentFromCUL(createContentAddressForLessonStart());
    }

    private ContentAddress createContentAddressForLessonStart() {
        ContentAddress currentAddressForLessonStart = new ContentAddress(finderContext.getCurrentAddress());
        currentAddressForLessonStart.setCollection(null);
        return currentAddressForLessonStart;
    }

    private AlternatePath findATargetAlternatePath(List<AlternatePath> filteredChildPaths) {
        // Find a way to define as to which one will be served. Currently served first one
        if (filteredChildPaths == null || filteredChildPaths.isEmpty()) {
            return null;
        }
        return filteredChildPaths.get(0);
    }

}
