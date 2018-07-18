package org.gooru.navigatemap.processor.next.pathfinder;

import org.gooru.navigatemap.infra.data.ContentAddress;
import org.gooru.navigatemap.infra.data.NavigateProcessorContext;
import org.gooru.navigatemap.infra.data.State;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.vertx.core.Future;
import io.vertx.core.Vertx;

/**
 * This is the main workhorse to find the content on path. This class triggers different kind of flows e.g. straight
 * path flow, explicit start flow etc by calling and delegating different services. Based on the outcome of service,
 * this class decides what needs to be done with respect to API response by modifying {@link NavigateProcessorContext}
 *
 * @author ashish on 26/2/17.
 */
public class PathFinderProcessor {
    private final Vertx vertx;
    private NavigateProcessorContext npc;
    private static final Logger LOGGER = LoggerFactory.getLogger(PathFinderProcessor.class);

    public PathFinderProcessor(Vertx vertx) {
        this.vertx = vertx;
    }

    public Future<NavigateProcessorContext> findNext(NavigateProcessorContext navigateProcessorContext) {
        Future<NavigateProcessorContext> future = Future.future();
        this.npc = navigateProcessorContext;
        /*
         * NOTE
         * The flow get tricky here. We have two options:
         * 1. We encapsulate the whole process around vertx executeBlocking and then do downstream operations in sync
         * mode. The con for this approach is that we are now bound by number of worker threads in system for the
         * scalability. However, this also means that the remaining classes can be modelled with proper workflow
         * responsibilities in place with proper OO. There is no mixing of infra with domain code.
         * 2. We do not do execute blocking here. However, we provide a delegate which is now responsible for
         * calling blocking operation in executeBlocking and non blocking operations on event loop. Then we do
         * future compose or callbacks to stitch the whole flow. This is going to be complex, will look more
         * procedural, difficult to maintain (as infra code e.g. future, executeBlocking are sprinkled everywhere).
         * This may prove more performant however even in that case, if there are too many context switches between
         * threads (eventloop to worker and vice versa), this may just be as performant as #1. Another alternative
         * here could be to shift to vertx jdbc client but then code will be more evolved (instead of JDBI). This
         * also means more maintenance.
         *
         * Considering all things, especially the timelines of development, at this point of time we go ahead with
         * first approach. If time permits, we shall come back and revisit it.
         */
        vertx.<NavigateProcessorContext>executeBlocking(pathMapperFuture -> {
            try {
                Future<NavigateProcessorContext> resultFuture = doPathMapping();
                resultFuture.setHandler(result -> pathMapperFuture.complete(result.result()));
            } catch (Throwable throwable) {
                LOGGER.warn("Error while mapping path", throwable);
                future.fail(throwable);
            }
        }, pathMapResult -> future.complete(pathMapResult.result()));
        return future;
    }

    private Future<NavigateProcessorContext> doPathMapping() {
        Future<NavigateProcessorContext> resultFuture = Future.future();
        if (!npc.suggestionsApplicable()) {
            LOGGER.debug("Suggestions not applicable. Will handle accordingly.");
            handleNoSuggestionsRoute();
        } else {
            // Delegate it to Suggestion oriented path finder
            LOGGER.debug("Suggestions are applicable. Will handle accordingly.");
            handleSuggestionsOrientedRoute();
        }
        resultFuture.complete(npc);
        return resultFuture;
    }

    private void handleSuggestionsOrientedRoute() {
        if (npc.needToStartCourse()) {
            LOGGER.debug("Need to start course");
            PathFinderResult result =
                PathFinderFactory.buildCourseStartPathFinderService().findPath(PathFinderContext.buildContext(npc));
            serveTheContent(result.getContentAddress());
        } else if (npc.onRoute0() && npc.userExplicitlyAskedToStartHere()) {
            LOGGER.debug("User is currently on Route0 and explicitly asking for start");
            PathFinderResult result = PathFinderFactory.buildRoute0ExplicitStartPathFinderService()
                .findPath(PathFinderContext.buildContext(npc));
            serveTheContent(result.getContentAddress());
        } else if (npc.onRoute0()) {
            LOGGER.debug("User is currently on Route0");
            PathFinderResult result =
                PathFinderFactory.buildRoute0PathFinderService().findPath(PathFinderContext.buildContext(npc));
            serveTheContent(result.getContentAddress());
        } else if (npc.userExplicitlyAskedToStartHere()) {
            LOGGER.debug("User explicitly requested to start the content.");
            PathFinderResult result =
                PathFinderFactory.buildExplicitStartPathFinderService().findPath(PathFinderContext.buildContext(npc));
            serveTheContent(result.getContentAddress());
        } else if (npc.userWasSuggestedAnItem()) {
            LOGGER.debug("User was served an suggestion. Need to start from there.");
            PathFinderResult result =
                PathFinderFactory.buildPostSuggestionItemFinderService().findPath(PathFinderContext.buildContext(npc));
            serveTheContent(result.getContentAddress());
        } else {
            // This is our default content served state
            LOGGER.debug("Default state handling.");
            PathFinderResult result =
                PathFinderFactory.buildSuggestionsAwarePathFinderService(npc.requestContext().getRouteContextData())
                    .findPath(PathFinderContext.buildContext(npc));
            if (result.hasSuggestions()) {
                serveTheSuggestions(result);
            } else {
                serveTheContent(result.getContentAddress());
            }
        }
    }

    private void serveTheSuggestions(PathFinderResult result) {
        switch (result.getSuggestedContentType()) {
        case Assessment:
            result.getSuggestions().forEach(suggestedItem -> npc.getCtxSuggestions().addAssessment(suggestedItem));
            break;
        case Collection:
            result.getSuggestions().forEach(suggestedItem -> npc.getCtxSuggestions().addCollection(suggestedItem));
            break;
        case Unit:
        case Course:
        case Lesson:
        case Resource:
        default:
            throw new IllegalStateException(
                "Suggestion type " + result.getSuggestedContentType().getName() + " not supported yet");
        }
        npc.getCtxSuggestions().setSuggestedContentSubType(result.getSuggestedContentSubType().getName());
        npc.responseContext().setState(State.ContentEndSuggested);
    }

    private void handleNoSuggestionsRoute() {
        if (npc.userExplicitlyAskedToStartCollection()) {
            LOGGER.debug("User explicitly asked to start collection on no suggestions route.");
            PathFinderResult result =
                PathFinderFactory.buildSpecifiedItemFinderService().findPath(PathFinderContext.buildContext(npc));
            serveTheContent(result.getContentAddress());
        } else {
            LOGGER.debug("Will find next item on no suggestions route.");
            PathFinderResult result =
                PathFinderFactory.buildStraightPathFinderService().findPath(PathFinderContext.buildContext(npc));
            serveTheContent(result.getContentAddress());
        }
    }

    private void serveTheContent(ContentAddress contentToServe) {
        if (contentToServe != null) {
            if (contentToServe.getCollection() != null) {
                handleRepeatIfServingSignatureCollection(contentToServe);
                npc.serveContent(contentToServe);
            } else {
                npc.responseContext().setState(State.Done);
            }
        } else {
            LOGGER.warn("Not able to locate valid content in course");
            npc.responseContext().setState(State.Done);
        }
    }

    private void handleRepeatIfServingSignatureCollection(ContentAddress contentToServe) {
        if (contentToServe.isCurrentItemSignatureCollection()) {
            npc.requestContext().getRouteContextData().turnOnRepeatAssessmentPostSignatureCollection();
        }

    }

}
