package org.gooru.navigatemap.processor.next.pathfinder;


import org.gooru.navigatemap.app.components.utilities.TenantSettingLookupUtility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TenantSettingService {
  private static final double DEFAULT_COMPLETION_SCORE = 80.00;
  private final static Logger LOGGER = LoggerFactory.getLogger(TenantSettingService.class);

  public Double fetchTenantCompletionScore(String tenantId) {
    try {
      Double completionScore =
          TenantSettingLookupUtility.getInstance().getScoreBytenantId(tenantId);
      return completionScore;
    } catch (Exception e) {
      LOGGER.error("Invalid completion score for settings '{}'", tenantId);
    }
    // returning default score incase of error or null
    return DEFAULT_COMPLETION_SCORE;
  }
}

