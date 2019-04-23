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
  private final Integer preferredLanguage;
  private final boolean milestoneViewApplicable;

  PathFinderContext(ContentAddress contentAddress, UUID classId, String userId, Double score,
      Integer preferredLanguage, boolean milestoneViewApplicable) {

    this.contentAddress = contentAddress;
    this.classId = classId;
    this.userId = userId;
    this.score = score;
    this.preferredLanguage = preferredLanguage;
    this.milestoneViewApplicable = milestoneViewApplicable;
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

  Double getScore() {
    return score;
  }

  public Integer getPreferredLanguage() {
    return preferredLanguage;
  }

  public boolean isMilestoneViewApplicable() {
    return milestoneViewApplicable;
  }

  static PathFinderContext buildContext(NavigateProcessorContext npc) {
    return new PathFinderContext(npc.getCurrentContentAddress(), npc.requestContext().getClassId(),
        npc.navigateMessageContext().getUserId(), npc.requestContext().getScorePercent(),
        npc.getPreferredLanguage(), npc.getMilestoneViewApplicable());
  }
}
