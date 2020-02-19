package org.gooru.navigatemap.processor.next.pathfinder;


import org.skife.jdbi.v2.DBI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TenantSettingService {
  private final TenantSettingDao dao;
  private static final double DEFAULT_COMPLETION_SCORE = 80.00;
  private final static Logger LOGGER = LoggerFactory.getLogger(TenantSettingService.class);
      
  public TenantSettingService(DBI getDbiForDefaultDS) {
    this.dao = getDbiForDefaultDS.onDemand(TenantSettingDao.class);
    
  }
  public Double fetchTenantCompletionScore(
      String tenantId) {
    String completionScore = this.dao.fetchTenantCompletionScore(tenantId);
    try  {
      return Double.parseDouble(completionScore);
    } catch(Exception e) {
      LOGGER.error("Invalid completion score for settings '{}'",completionScore);
    }
    // returning default score incase of error or null
    return DEFAULT_COMPLETION_SCORE;
  }
}

