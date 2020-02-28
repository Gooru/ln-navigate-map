package org.gooru.navigatemap.app.components.utilities;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.sql.DataSource;
import org.skife.jdbi.v2.DBI;
import org.skife.jdbi.v2.Handle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author vijayaraman on 20/2/20.
 */

public final class TenantSettingLookupUtility {
 
  private Map<String, Double> tenantCompletionScoreMap;
  private static final Logger LOGGER = LoggerFactory.getLogger(TenantSettingLookupUtility.class);
  private static final double DEFAULT_COMPLETION_SCORE = 80.00;
  private static final String ID = "id";
  private static final String VALUE = "value";

  public static TenantSettingLookupUtility getInstance() {
    return TenantSettingLookupUtility.Holder.INSTANCE;
  }
  
  private volatile boolean initialized = false;
  
  private TenantSettingLookupUtility() {
  }

  public void initialize(DataSource defaultDataSource) {
    if (!initialized) {
      synchronized (TenantSettingLookupUtility.Holder.INSTANCE) {
        if (!initialized) {
          initializeThresholdForCompetencyCompletionBasedOnAssessment(defaultDataSource);
          initialized = true;
        }
      }
    }
  }

  private void initializeThresholdForCompetencyCompletionBasedOnAssessment(DataSource dataSource) {
    Handle handle = DBI.open(dataSource);
    List<Map<String, Object>> tenantCompletionList =
        Queries.getThresholdForCompetencyCompletionBasedOnAssessment(handle);
    if (tenantCompletionList != null) {
      tenantCompletionScoreMap = new HashMap<>();
      for (Map<String, Object> tenantMap : tenantCompletionList) {
        LOGGER.debug("Tenant Id : '{}' Completion score : '{}'", tenantMap.get(ID),
            tenantMap.get(VALUE));
        if (tenantMap.get(ID) != null) {
          String tenantId = tenantMap.get(ID).toString();
          if (tenantMap.get(VALUE) != null) {
            try {
              tenantCompletionScoreMap.put(tenantId,
                  Double.valueOf(tenantMap.get(VALUE).toString()));
            } catch (NumberFormatException e) {
              LOGGER.warn("Invalid completion score for the tenant '{}'", tenantId);
              tenantCompletionScoreMap.put(tenantId, DEFAULT_COMPLETION_SCORE);
            }
          } else {
            tenantCompletionScoreMap.put(tenantId, DEFAULT_COMPLETION_SCORE);
          }

        }
      }
    }
  }
  
  public Double getScoreBytenantId(String id) {
    if (tenantCompletionScoreMap != null && tenantCompletionScoreMap.containsKey(id)) {
        return tenantCompletionScoreMap.get(id);
    }
    return DEFAULT_COMPLETION_SCORE;
  }
  
  private static final class Holder {

    private static final TenantSettingLookupUtility INSTANCE = new TenantSettingLookupUtility();
    
  }
  private static final class Queries {

    private static final String DEFAULT_LOOKUP_QUERY = "SELECT id,value FROM tenant_setting where key = :key";    
    private static final String KEY = "key";
    private static final String COMPETENCY_COMPLETION_THRESHOLD =
        "comp_completion_threshold";


    static List<Map<String,Object>> getThresholdForCompetencyCompletionBasedOnAssessment(Handle handle) {
      return handle.createQuery(DEFAULT_LOOKUP_QUERY)
          .bind(KEY, COMPETENCY_COMPLETION_THRESHOLD).list();
    }
  }

}
  
