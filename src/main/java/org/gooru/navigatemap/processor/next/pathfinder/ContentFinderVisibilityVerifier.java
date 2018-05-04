package org.gooru.navigatemap.processor.next.pathfinder;

import java.util.List;
import java.util.UUID;

import org.gooru.navigatemap.infra.data.ContentAddress;
import org.gooru.navigatemap.infra.data.CurrentItemType;
import org.skife.jdbi.v2.DBI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.vertx.core.json.JsonObject;

/**
 * @author ashish on 12/9/17.
 */
final class ContentFinderVisibilityVerifier {

    private final UUID classId;
    private final CLASS_VISIBILITY visibility;
    private static final Logger LOGGER = LoggerFactory.getLogger(ContentFinderVisibilityVerifier.class);

    private ContentFinderVisibilityVerifier(UUID classId, CLASS_VISIBILITY visibility) {
        this.classId = classId;
        this.visibility = visibility;
    }

    boolean isContentVisible(ContentAddress address) {
        if (this.visibility == CLASS_VISIBILITY.VISIBLE_NA) {
            return verifyVisibilityPlaceholder(address);
        } else {
            return verifyVisibilityWithClass(address);
        }
    }

    ContentAddress findFirstVisibleContentAddress(List<ContentAddress> contentAddresses) {
        if (contentAddresses != null && !contentAddresses.isEmpty()) {
            for (ContentAddress address : contentAddresses) {
                if (isContentVisible(address)) {
                    return address;
                }
            }
        }
        return null;
    }

    private boolean verifyVisibilityWithClass(ContentAddress address) {
        switch (this.visibility) {
        case VISIBLE_ALL:
            return verifyVisibilityWithClassVisibilityAsAll(address);
        case VISIBLE_NONE:
            return verifyVisibilityWithClassVisibilityAsNone(address);
        case VISIBLE_COLLECTIONS:
            return verifyVisibilityWithClassVisibilityAsCollections(address);
        }
        return false;
    }

    private boolean verifyVisibilityWithClassVisibilityAsCollections(ContentAddress address) {
        if (address.getCurrentItemType() == CurrentItemType.Assessment
            || address.getCurrentItemType() == CurrentItemType.AssessmentExternal) {
            return getVisibilityForClassInContent(address) == VISIBLITY_STATE.ON;
        } else if (address.getCurrentItemType() == CurrentItemType.Collection) {
            return getVisibilityForClassInContent(address) != VISIBLITY_STATE.OFF;
        } else {
            LOGGER.warn("Invalid collection type for collection: '{}'", address.getCollection());
            throw new IllegalStateException("Invalid collection type for collection : " + address.getCollection());
        }
    }

    private boolean verifyVisibilityWithClassVisibilityAsNone(ContentAddress address) {
        return getVisibilityForClassInContent(address) == VISIBLITY_STATE.ON;
    }

    private boolean verifyVisibilityWithClassVisibilityAsAll(ContentAddress address) {
        return getVisibilityForClassInContent(address) != VISIBLITY_STATE.OFF;
    }

    private VISIBLITY_STATE getVisibilityForClassInContent(ContentAddress address) {
        String visibilityForContentString = address.getVisibility();
        if (visibilityForContentString != null) {
            JsonObject visibilityJson = new JsonObject(visibilityForContentString);
            String result = visibilityJson.getString(this.classId.toString());
            if (result != null) {
                if (result.equalsIgnoreCase("on")) {
                    return VISIBLITY_STATE.ON;
                } else if (result.equalsIgnoreCase("off")) {
                    return VISIBLITY_STATE.OFF;
                } else {
                    return VISIBLITY_STATE.NONE;
                }
            }
        }
        return VISIBLITY_STATE.NONE;
    }

    private boolean verifyVisibilityPlaceholder(ContentAddress address) {
        // If there is no class or cases like NU we show everything
        return true;
    }

    static ContentFinderVisibilityVerifier buildPlaceholderVerifier(UUID classId) {
        return new ContentFinderVisibilityVerifier(classId, CLASS_VISIBILITY.VISIBLE_NA);
    }

    static ContentFinderVisibilityVerifier build(UUID classId, DBI dbi) {
        ContentFinderVisibilityVerifier result;
        if (classId == null) {
            result = new ContentFinderVisibilityVerifier(classId, CLASS_VISIBILITY.VISIBLE_NA);
        } else {
            ClassDao dao = dbi.onDemand(ClassDao.class);
            CLASS_VISIBILITY visibility = getVisibilityFromString(dao.getClassVisibility(classId.toString()));
            result = new ContentFinderVisibilityVerifier(classId, visibility);
        }
        return result;
    }

    private enum VISIBLITY_STATE {
        ON,
        OFF,
        NONE
    }

    private enum CLASS_VISIBILITY {
        VISIBLE_ALL,
        VISIBLE_COLLECTIONS,
        VISIBLE_NONE,
        VISIBLE_NA
    }

    private static CLASS_VISIBILITY getVisibilityFromString(String visibility) {
        switch (visibility) {
        case "visible_none":
            return CLASS_VISIBILITY.VISIBLE_NONE;
        case "visible_collections":
            return CLASS_VISIBILITY.VISIBLE_COLLECTIONS;
        case "visible_all":
            return CLASS_VISIBILITY.VISIBLE_ALL;
        default:
            throw new IllegalStateException("Invalid class visibility settings");
        }
    }
}
