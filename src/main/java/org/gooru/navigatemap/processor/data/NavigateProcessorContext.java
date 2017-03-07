package org.gooru.navigatemap.processor.data;

import org.gooru.navigatemap.processor.coursepath.state.Stateful;

/**
 * @author ashish on 27/2/17.
 */
public final class NavigateProcessorContext implements Stateful {

    private final NavigateMessageContext nmc;
    private final RequestContext ctxIn;
    private final ResponseContext ctxOut;
    private final SuggestionContext ctxSuggestions;
    private final ContentAddress nextContentAddress;

    public NavigateProcessorContext(RequestContext requestContext, NavigateMessageContext navigateMessageContext) {
        this.ctxIn = requestContext;
        this.nmc = navigateMessageContext;
        this.ctxOut = new ResponseContext(requestContext);
        ctxSuggestions = new SuggestionContext();
        nextContentAddress = new ContentAddress();
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
        return nextContentAddress;
    }

    public void setNextContextAddress(ContentAddress address) {
        nextContentAddress.setCourse(address.getCourse());
        nextContentAddress.setUnit(address.getUnit());
        nextContentAddress.setLesson(address.getLesson());
        nextContentAddress.setCollection(address.getCollection());
        nextContentAddress.setCollectionType(address.getCollectionType());
        nextContentAddress.setCollectionSubtype(address.getCollectionSubtype());
    }

    @Override
    public State getCurrentState() {
        return ctxIn.getState();
    }
}
