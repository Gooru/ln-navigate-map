package org.gooru.navigatemap.processor.data;

import java.util.Objects;

import org.gooru.navigatemap.app.components.AppConfiguration;
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

    public boolean suggestionsTurnedOff() {
        return nmc.isUserAnonymous() || !AppConfiguration.getInstance().suggestionsTurnedOn();
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

    public ContentAddress getCurrentContentAddress() {
        ContentAddress result = new ContentAddress();
        result.setCollectionSubtype(requestContext().getCurrentItemSubtype());
        result.setCollectionType(requestContext().getCurrentItemType());
        result.setCollection(Objects.toString(requestContext().getCurrentItemId(), null));
        result.setCourse(requestContext().getCourseId().toString());
        result.setUnit(Objects.toString(requestContext().getUnitId(), null));
        result.setLesson(Objects.toString(requestContext().getLessonId(), null));
        return result;
    }

    @Override
    public State getCurrentState() {
        return ctxIn.getState();
    }
}
