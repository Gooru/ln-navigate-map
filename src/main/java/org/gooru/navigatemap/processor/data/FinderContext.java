package org.gooru.navigatemap.processor.data;

import java.util.Objects;
import java.util.UUID;

/**
 * @author ashish on 6/4/17.
 */
public final class FinderContext {
    private final State state;
    private final RequestContext requestContext;
    private final ContentAddress currentAddress;
    private boolean statusDone;
    private UUID currentItemId;
    private CollectionType currentItemType;
    private CollectionSubtype currentItemSubtype;

    public State getState() {
        return state;
    }

    public RequestContext getRequestContext() {
        return requestContext;
    }

    public ContentAddress getCurrentAddress() {
        return currentAddress;
    }

    public boolean isStatusDone() {
        return statusDone;
    }

    public UUID getCurrentItemId() {
        return currentItemId;
    }

    public CollectionType getCurrentItemType() {
        return currentItemType;
    }

    public CollectionSubtype getCurrentItemSubtype() {
        return currentItemSubtype;
    }

    public void setCurrentItem(UUID itemId, CollectionType itemType, CollectionSubtype itemSubtype) {
        Objects.requireNonNull(itemId);
        Objects.requireNonNull(itemType);
        Objects.requireNonNull(itemSubtype);

        currentItemId = itemId;
        currentItemType = itemType;
        currentItemSubtype = itemSubtype;
        statusDone = true;
    }

    public FinderContext(State state, RequestContext requestContext, ContentAddress currentAddress) {
        this.state = state;
        this.requestContext = requestContext;
        this.currentAddress = currentAddress;

    }
}
