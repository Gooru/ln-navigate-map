package org.gooru.navigatemap.processor.next.pathfinder;

import java.util.List;

import org.gooru.navigatemap.app.constants.HttpConstants;
import org.gooru.navigatemap.app.exceptions.HttpResponseWrapperException;
import org.gooru.navigatemap.infra.data.AlternatePath;
import org.gooru.navigatemap.infra.data.ContentAddress;
import org.gooru.navigatemap.infra.data.CurrentItemType;
import org.gooru.navigatemap.infra.data.SuggestedContentSubType;
import org.gooru.navigatemap.infra.data.SuggestedContentType;
import org.skife.jdbi.v2.DBI;

// @formatter:off
/**
 *
 * In a nutshell, here is what we are trying to do. Note that we always enter here with content served state. The
 * suggestions state demands that caller converts it to start.
 *
 * - handle content-served state
 *     - if on main path
 *         - if assessment
 *             - if passed,
 *                 - update competency completion (track it)
 *                 - if signature item present for competency apply suggestion (track it)
 *                 - else loadNextItemFromMainPath
 *             - else
 *                 - if signature coll present for competency apply suggestion (track it)
 *                 - else loadNextItemFromMainPath
 *         - else
 *             - loadNextItemFromMainPath
 *     - if on teacher path
 *         - if assessment
 *             - if passed,
 *                 - update competency completion (track it)
 *                 - loadNextItemFromTeacherPath
 *             - else
 *                 - loadNextItemFromTeacherPath
 *         - else
 *             - loadNextItemFromTeacherPath
 *     - if on system path
 *         - if assessment (note that assessment won't be loaded with start flow as they won't be present on course map)
 *             - if passed,
 *                 - update competency mastery as it would be signature item (track it)
 *                 - loadNextItemFromMainPath
 *             - else
 *                 - if signature coll present for competency apply suggestion (track it)
 *                 - else continue
 *         - if collection
 *             - if there is any other collection present on system path for that context, load that
 *             - else loadNextItemFromMainPath
 *
 * - loadNextItemFromTeacherPath
 *     - if there is next item on teacher path, load
 *     - else loadNextItemFromMainPath
 *
 * - loadNextItemFromMainPath
 *     - find next item on main path considering skip and visibility
 *     - if there is an item found
 *         - if there a teacher path, load first item from that teacher path
 *         - else load the item itself
 *     - else
 *         - mark as done
 *
 * @author ashish on 7/5/18.
 */
// @formatter:on
class SuggestionsAwarePathFinderService implements PathFinder {

    private final DBI dbi;
    private PathFinderContext context;

    SuggestionsAwarePathFinderService(DBI dbi) {
        this.dbi = dbi;
    }

    @Override
    public PathFinderResult findPath(PathFinderContext context) {
        this.context = context;
        return process();
    }

    private PathFinderResult process() {
        if (context.getContentAddress().isOnMainPath()) {
            if (context.getContentAddress().getCurrentItemType() == CurrentItemType.Assessment) {
                CompetencyCompletionHandler competencyCompletionHandler = new CompetencyCompletionHandler(dbi, context);
                competencyCompletionHandler.handleCompetencyCompletion();
                List<String> competencies = competencyCompletionHandler.fetchCompetenciesForCollection();
                if (competencyCompletionHandler.isCompetencyCompleted()) {
                    List<String> signatureItems = SuggestionFinderBuilder.buildSuggestionFinder(dbi)
                        .findSignatureAssessmentsForCompetencies(context, competencies);
                    if (signatureItems != null && !signatureItems.isEmpty()) {
                        return new PathFinderResult(signatureItems, SuggestedContentType.Assessment,
                            SuggestedContentSubType.SignatureAssessment);
                    } else {
                        return loadNextItemFromMainpath();
                    }
                } else {
                    List<String> signatureItems = SuggestionFinderBuilder.buildSuggestionFinder(dbi)
                        .findSignatureCollectionsForCompetencies(context, competencies);
                    if (signatureItems != null && !signatureItems.isEmpty()) {
                        return new PathFinderResult(signatureItems, SuggestedContentType.Collection,
                            SuggestedContentSubType.SignatureCollection);
                    } else {
                        return loadNextItemFromMainpath();
                    }
                }
            } else {
                return loadNextItemFromMainpath();
            }
        } else {
            AlternatePath alternatePath = findAlternatePath();
            if (alternatePath.isSuggestionTeacherSuggestion()) {
                if (context.getContentAddress().getCurrentItemType() == CurrentItemType.Assessment) {
                    CompetencyCompletionHandler competencyCompletionHandler =
                        new CompetencyCompletionHandler(dbi, context);
                    competencyCompletionHandler.handleCompetencyCompletion();
                }
                return loadNextItemFromTeacherpath();
            } else if (alternatePath.isSuggestionSystemSuggestion()) {
                if (context.getContentAddress().getCurrentItemType() == CurrentItemType.Assessment) {
                    CompetencyCompletionHandler competencyCompletionHandler =
                        new CompetencyCompletionHandler(dbi, context);
                    competencyCompletionHandler.handleCompetencyMastery(alternatePath);
                    if (competencyCompletionHandler.isCompetencyCompleted()) {
                        return loadNextItemFromMainpath();
                    } else {
                        List<String> signatureItems = SuggestionFinderBuilder.buildSuggestionFinder(dbi)
                            .findSignatureCollectionsForCompetencies(context,
                                competencyCompletionHandler.fetchCompetenciesForCollection());
                        if (signatureItems != null && !signatureItems.isEmpty()) {
                            return new PathFinderResult(signatureItems, SuggestedContentType.Collection,
                                SuggestedContentSubType.SignatureCollection);
                        } else {
                            return loadNextItemFromMainpath();
                        }
                    }
                } else {
                    return loadNextItemFromSystemPath(alternatePath);
                }
            } else {
                throw new HttpResponseWrapperException(HttpConstants.HttpStatus.BAD_REQUEST, "Invalid path type");
            }
        }
    }

    /* Load item from teacher path, if not present delegate to finder for main path */
    private PathFinderResult loadNextItemFromTeacherpath() {
        ContentAddress teacherPathContentAddress =
            ContentFinderFactory.buildTeacherPathContentFinder(dbi).findContent(context);
        if (teacherPathContentAddress != null) {
            return new PathFinderResult(teacherPathContentAddress);
        } else {
            return loadCurrentItemFromMainpath();
        }
    }

    /* Teacher path is pre to current item. Once we have exhausted teacher path, we load current content */
    private PathFinderResult loadCurrentItemFromMainpath() {
        return new PathFinderResult(
            ContentFinderFactory.buildAlternatePathUnawareSpecifiedPathContentFinder(dbi).findContent(context));
    }

    /* Load next item from system path. If such an item is not present then delegate to finder for main path */
    private PathFinderResult loadNextItemFromSystemPath(AlternatePath currentAlternatePath) {
        List<AlternatePath> nextAlternatePaths;
        if (context.getClassId() != null) {
            nextAlternatePaths = getAlternatePathDao()
                .findNextSystemPathOfCollectionInClass(context.getContentAddress(), context.getUserId(),
                    context.getClassId().toString(), currentAlternatePath.getId());
        } else {
            nextAlternatePaths = getAlternatePathDao()
                .findNextSystemPathOfCollectionForIL(context.getContentAddress(), context.getUserId(),
                    currentAlternatePath.getId());
        }
        if (nextAlternatePaths != null && !nextAlternatePaths.isEmpty()) {
            return new PathFinderResult(nextAlternatePaths.get(0).toContentAddress());
        }
        return loadNextItemFromMainpath();
    }

    /* Load item from main path, and if there is teacher path on that item, return that else return the loaded path */
    private PathFinderResult loadNextItemFromMainpath() {
        return new PathFinderResult(
            ContentFinderFactory.buildTeacherPathAwareMainPathContentFinder(dbi).findContent(context));
    }

    private AlternatePath findAlternatePath() {
        AlternatePath specifiedPath;
        if (context.getClassId() == null) {
            specifiedPath = getAlternatePathDao()
                .findAlternatePathByPathIdAndUserForIL(context.getContentAddress().getPathId(), context.getUserId());
        } else {
            specifiedPath = getAlternatePathDao()
                .findAlternatePathByPathIdAndUserInClass(context.getContentAddress().getPathId(), context.getUserId(),
                    context.getClassId().toString());
        }

        if (specifiedPath == null) {
            throw new HttpResponseWrapperException(HttpConstants.HttpStatus.BAD_REQUEST, "Invalid path");
        }
        return specifiedPath;
    }

    private AlternatePathDao getAlternatePathDao() {
        return dbi.onDemand(AlternatePathDao.class);
    }
}
