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
    private final Double score;

    PathFinderContext(ContentAddress contentAddress, UUID classId, String userId, Double score) {
        this.contentAddress = contentAddress;
        this.classId = classId;
        this.userId = userId;
        this.score = score;
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

    public Double getScore() {
        return score;
    }

    static PathFinderContext buildContext(NavigateProcessorContext npc) {
        return new PathFinderContext(npc.getCurrentContentAddress(), npc.requestContext().getClassId(),
            npc.navigateMessageContext().getUserId(), npc.requestContext().getScorePercent());
    }
}
