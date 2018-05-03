package org.gooru.navigatemap.infra.data;

import java.util.Objects;
import java.util.UUID;

/**
 * @author ashish on 6/4/17.
 */
public final class FinderContext {
    private final State state;
    private final RequestContext requestContext;
    private final ContentAddress currentAddress;
    private final String user;
    private boolean statusDone;
    private UUID currentItemId;
    private CurrentItemType currentItemType;
    private CurrentItemSubtype currentItemSubtype;
    private String scoreRange;

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

    public CurrentItemType getCurrentItemType() {
        return currentItemType;
    }

    public CurrentItemSubtype getCurrentItemSubtype() {
        return currentItemSubtype;
    }

    public String getUserClass() {
        return requestContext.getClassId() == null ? null : requestContext.getClassId().toString();
    }

    public void setCurrentItem(UUID itemId, CurrentItemType itemType, CurrentItemSubtype itemSubtype) {
        Objects.requireNonNull(itemId);
        Objects.requireNonNull(itemType);

        currentItemId = itemId;
        currentItemType = itemType;
        currentItemSubtype = itemSubtype;
        statusDone = true;
    }

    public FinderContext(State state, RequestContext requestContext, ContentAddress currentAddress, String userId) {
        this.state = state;
        this.requestContext = requestContext;
        this.currentAddress = currentAddress;
        this.user = userId;
    }

    public String getUser() {
        return user;
    }

    public String getScoreRange() {
        return scoreRange;
    }

    public void setScoreRange(String scoreRange) {
        this.scoreRange = scoreRange;
    }
}
