package org.gooru.navigatemap.processor.next.pathfinder;

import java.util.UUID;

import org.gooru.navigatemap.infra.data.ContentAddress;
import org.gooru.navigatemap.infra.data.NavigateProcessorContext;

/**
 * @author ashish on 4/5/18.
 */
class PathFinderContext {
    private final ContentAddress contentAddress;
    private final UUID classId;
    private final String userId;

    PathFinderContext(ContentAddress contentAddress, UUID classId, String userId) {
        this.contentAddress = contentAddress;
        this.classId = classId;
        this.userId = userId;
    }

    ContentAddress getContentAddress() {
        return contentAddress;
    }

    UUID getClassId() {
        return classId;
    }

    String getUserId() {
        return userId;
    }

    static PathFinderContext buildContext(NavigateProcessorContext npc) {
        return new PathFinderContext(npc.getCurrentContentAddress(), npc.requestContext().getClassId(),
            npc.navigateMessageContext().getUserId());
    }
}
