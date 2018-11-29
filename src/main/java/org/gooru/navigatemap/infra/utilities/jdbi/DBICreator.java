package org.gooru.navigatemap.infra.utilities.jdbi;

import javax.sql.DataSource;
import org.gooru.navigatemap.app.components.DataSourceRegistry;
import org.skife.jdbi.v2.DBI;

/**
 * @author ashish on 15/3/17.
 */
public final class DBICreator {

  private DBICreator() {
    throw new AssertionError();
  }

  private static DBI createDBI(DataSource dataSource) {
    DBI dbi = new DBI(dataSource);
    dbi.registerArgumentFactory(new PostgresIntegerArrayArgumentFactory());
    dbi.registerArgumentFactory(new PostgresStringArrayArgumentFactory());
    dbi.registerArgumentFactory(new PostgresUUIDArrayArgumentFactory());
    return dbi;
  }

  public static DBI getDbiForDefaultDS() {
    return createDBI(DataSourceRegistry.getInstance().getDefaultDataSource());
  }

}
