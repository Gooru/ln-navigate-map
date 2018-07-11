package org.gooru.navigatemap.infra.data;

import java.util.Objects;

import org.gooru.navigatemap.infra.utilities.suggestionsapplicability.SuggestionsApplicabilityVerifier;

/**
 * @author ashish on 27/2/17.
 */
public final class NavigateProcessorContext {

    private final NavigateMessageContext nmc;
    private final RequestContext ctxIn;
    private final ResponseContext ctxOut;
    private final SuggestionContext ctxSuggestions;
    private final ContentAddress nextContentAddress;
    private final ContentAddress currentContentAddress;
    private boolean nextAddressSet = false;
    private boolean currentAddressSet = false;
    private Boolean suggestionsFlagInitialized;

    public NavigateProcessorContext(RequestContext requestContext, NavigateMessageContext navigateMessageContext) {
        this.ctxIn = requestContext;
        this.nmc = navigateMessageContext;
        this.ctxOut = new ResponseContext(requestContext);
        ctxSuggestions = new SuggestionContext();
        nextContentAddress = new ContentAddress();
        currentContentAddress = new ContentAddress();
        getCurrentContentAddress();
    }

    public boolean suggestionsApplicable() {
        if (suggestionsFlagInitialized == null) {
            suggestionsFlagInitialized = new SuggestionsApplicabilityVerifier(this).areSuggestionApplicable();
        }
        return suggestionsFlagInitialized;
    }

    public boolean needToStartCourse() {
        // When NPC is created, it is initialized in one of following sequences:
        // 1. Context fetched from Redis. We never store "Continue" there
        // 2. If we don't find context in Redis, we initialize context with whatever is coming in. The client is
        // supposed to send either "Continue" or "Start" state. While start would mean that user has provided start
        // point, Continue now would mean that Course needs to be started.
        return ctxIn.getState() == State.Continue;
    }

    public boolean userExplicitlyAskedToStartHere() {
        return ctxIn.getState() == State.Start;
    }

    public boolean userWasSuggestedAnItem() {
        return ctxIn.getState() == State.ContentEndSuggested;
    }

    public boolean userExplicitlyAskedToStartLesson() {
        return userExplicitlyAskedToStartHere() && ctxIn.getCollectionId() == null;
    }

    public boolean userExplicitlyAskedToStartCollection() {
        return userExplicitlyAskedToStartHere() && ctxIn.getCollectionId() != null;
    }

    public boolean onMainPath() {
        return (ctxIn.getPathId() == null || ctxIn.getPathId() == 0) && ctxIn.getPathType() == null;
    }

    public NavigateMessageContext navigateMessageContext() {
        return nmc;
    }

    public RequestContext requestContext() {
        return ctxIn;
    }

    public ResponseContext responseContext() {
        return ctxOut;
    }

    public SuggestionContext getCtxSuggestions() {
        return ctxSuggestions;
    }

    public ContentAddress getNextContentAddress() {
        if (!nextAddressSet) {
            throw new IllegalStateException("Next content address not set");
        }
        return nextContentAddress;
    }

    public void setNextContextAddress(ContentAddress address) {
        nextContentAddress.setCourse(address.getCourse());
        nextContentAddress.setUnit(address.getUnit());
        nextContentAddress.setLesson(address.getLesson());
        nextContentAddress.setCollection(address.getCollection());
        nextContentAddress.setPathId(address.getPathId());
        nextContentAddress.setPathType(address.getPathType());
        nextContentAddress.setCurrentItem(address.getCurrentItem());
        nextContentAddress.setCurrentItemType(address.getCurrentItemType());
        nextContentAddress.setCurrentItemSubtype(address.getCurrentItemSubtype());
        nextAddressSet = true;
    }

    public ContentAddress getCurrentContentAddress() {
        if (!currentAddressSet) {
            currentContentAddress.setCourse(requestContext().getCourseId().toString());
            currentContentAddress.setUnit(Objects.toString(requestContext().getUnitId(), null));
            currentContentAddress.setLesson(Objects.toString(requestContext().getLessonId(), null));
            currentContentAddress.setCollection(Objects.toString(requestContext().getCollectionId(), null));
            currentContentAddress.setCurrentItem(Objects.toString(requestContext().getCurrentItemId(), null));
            currentContentAddress.setCurrentItemType(requestContext().getCurrentItemType());
            currentContentAddress.setCurrentItemSubtype(requestContext().getCurrentItemSubtype());
            currentContentAddress.setPathId(requestContext().getPathId());
            currentContentAddress.setPathType(requestContext().getPathType());
            currentAddressSet = true;
            return currentContentAddress;
        }
        return currentContentAddress;
    }

    public FinderContext createFinderContext() {
        return new FinderContext(requestContext().getState(), requestContext(), getCurrentContentAddress(),
            navigateMessageContext().getUserId());
    }

    public void serveContent(ContentAddress targetAddress) {
        setNextContextAddress(targetAddress);
        responseContext().setContentAddress(targetAddress);
        responseContext().setState(State.ContentServed);
    }
}
