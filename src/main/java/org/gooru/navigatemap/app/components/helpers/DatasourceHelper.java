package org.gooru.navigatemap.app.components.helpers;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import io.vertx.core.json.JsonObject;
import java.util.Map;
import java.util.Objects;
import javax.sql.DataSource;

/**
 * @author ashish on 25/2/17.
 */
public final class DatasourceHelper {

  private DatasourceHelper() {
    throw new AssertionError();
  }

  private static final String DEFAULT_DATA_SOURCE_TYPE = "nucleus.ds.type";
  private static final String DS_HIKARI = "hikari";

  public static DataSource initializeDataSource(JsonObject dbConfig) {
    // The default DS provider is hikari, so if set explicitly or not set
    // use it, else error out
    String dsType = dbConfig.getString(DEFAULT_DATA_SOURCE_TYPE);
    if (!Objects.equals(dsType, DS_HIKARI)) {
      // No support
      throw new IllegalStateException("Unsupported data store type");
    }
    final HikariConfig config = new HikariConfig();

    for (Map.Entry<String, Object> entry : dbConfig) {
      switch (entry.getKey()) {
        case "dataSourceClassName":
          config.setDataSourceClassName((String) entry.getValue());
          break;
        case "jdbcUrl":
          config.setJdbcUrl((String) entry.getValue());
          break;
        case "username":
          config.setUsername((String) entry.getValue());
          break;
        case "password":
          config.setPassword((String) entry.getValue());
          break;
        case "autoCommit":
          config.setAutoCommit((Boolean) entry.getValue());
          break;
        case "connectionTimeout":
          config.setConnectionTimeout((Long) entry.getValue());
          break;
        case "idleTimeout":
          config.setIdleTimeout((Long) entry.getValue());
          break;
        case "maxLifetime":
          config.setMaxLifetime((Long) entry.getValue());
          break;
        case "connectionTestQuery":
          config.setConnectionTestQuery((String) entry.getValue());
          break;
        case "minimumIdle":
          config.setMinimumIdle((Integer) entry.getValue());
          break;
        case "maximumPoolSize":
          config.setMaximumPoolSize((Integer) entry.getValue());
          break;
        case "metricRegistry":
        case "threadFactory":
        case "dataSource":
        case "healthCheckRegistry":
          throw new UnsupportedOperationException(entry.getKey());
        case "poolName":
          config.setPoolName((String) entry.getValue());
          break;
        case "isolationInternalQueries":
          config.setIsolateInternalQueries((Boolean) entry.getValue());
          break;
        case "allowPoolSuspension":
          config.setAllowPoolSuspension((Boolean) entry.getValue());
          break;
        case "readOnly":
          config.setReadOnly((Boolean) entry.getValue());
          break;
        case "registerMBeans":
          config.setRegisterMbeans((Boolean) entry.getValue());
          break;
        case "catalog":
          config.setCatalog((String) entry.getValue());
          break;
        case "connectionInitSql":
          config.setConnectionInitSql((String) entry.getValue());
          break;
        case "driverClassName":
          config.setDriverClassName((String) entry.getValue());
          break;
        case "transactionIsolation":
          config.setTransactionIsolation((String) entry.getValue());
          break;
        case "validationTimeout":
          config.setValidationTimeout((Long) entry.getValue());
          break;
        case "leakDetectionThreshold":
          config.setLeakDetectionThreshold((Long) entry.getValue());
          break;
        case "datasource":
          for (Map.Entry<String, Object> key : ((JsonObject) entry.getValue())) {
            config.addDataSourceProperty(key.getKey(), key.getValue());
          }
          break;
      }
    }

    return new HikariDataSource(config);

  }

}
