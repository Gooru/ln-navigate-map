package org.gooru.navigatemap.processor.data;

import java.util.Objects;

import org.gooru.navigatemap.processor.utilities.suggestionsapplicability.SuggestionsApplicabilityVerifier;

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
    private boolean qualifiedCurrentAddressSet = false;
    private final ContentAddress currentQualifiedContentAddress;
    private Boolean suggestionsFlagInitialized;

    public NavigateProcessorContext(RequestContext requestContext, NavigateMessageContext navigateMessageContext) {
        this.ctxIn = requestContext;
        this.nmc = navigateMessageContext;
        this.ctxOut = new ResponseContext(requestContext);
        ctxSuggestions = new SuggestionContext();
        nextContentAddress = new ContentAddress();
        currentContentAddress = new ContentAddress();
        currentQualifiedContentAddress = new ContentAddress();
        getCurrentContentAddress();
        getCurrentContentAddressQualified();
    }

    public boolean suggestionsTurnedOff() {
        if (suggestionsFlagInitialized == null) {
            suggestionsFlagInitialized = new SuggestionsApplicabilityVerifier(this).areSuggestionApplicable();
        }
        return !suggestionsFlagInitialized;
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
        nextContentAddress.setCurrentItem(address.getCurrentItem());
        nextContentAddress.setCurrentItemType(address.getCurrentItemType());
        nextContentAddress.setCurrentItemSubtype(address.getCurrentItemSubtype());
        nextAddressSet = true;
    }

    public ContentAddress getCurrentContentAddress() {
        if (!currentAddressSet) {
            currentContentAddress.setCollection(Objects.toString(requestContext().getCurrentItemId(), null));
            currentContentAddress.setCourse(requestContext().getCourseId().toString());
            currentContentAddress.setUnit(Objects.toString(requestContext().getUnitId(), null));
            currentContentAddress.setLesson(Objects.toString(requestContext().getLessonId(), null));
            currentContentAddress.setPathId(requestContext().getPathId());
            currentAddressSet = true;
            return currentContentAddress;
        }
        return currentContentAddress;
    }

    public ContentAddress getCurrentContentAddressQualified() {
        if (!qualifiedCurrentAddressSet) {
            currentQualifiedContentAddress.setCollection(Objects.toString(requestContext().getCollectionId(), null));

            currentQualifiedContentAddress.setCurrentItemSubtype(requestContext().getCurrentItemSubtype());
            currentQualifiedContentAddress.setCurrentItemType(requestContext().getCurrentItemType());
            currentQualifiedContentAddress.setCurrentItem(Objects.toString(requestContext().getCurrentItemId(), null));

            currentQualifiedContentAddress.setCourse(requestContext().getCourseId().toString());
            currentQualifiedContentAddress.setUnit(Objects.toString(requestContext().getUnitId(), null));
            currentQualifiedContentAddress.setLesson(Objects.toString(requestContext().getLessonId(), null));
            currentQualifiedContentAddress.setPathId(requestContext().getPathId());
            qualifiedCurrentAddressSet = true;
            return currentQualifiedContentAddress;
        }
        return currentQualifiedContentAddress;
    }

    public FinderContext createFinderContext() {
        return new FinderContext(requestContext().getState(), requestContext(), getCurrentContentAddressQualified(),
            navigateMessageContext().getUserId());
    }

}
