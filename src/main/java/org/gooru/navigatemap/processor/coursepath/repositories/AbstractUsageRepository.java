package org.gooru.navigatemap.processor.coursepath.repositories;

import org.gooru.navigatemap.app.components.DataSourceRegistry;
import org.skife.jdbi.v2.DBI;

/**
 * @author ashish on 7/3/17.
 */
class AbstractUsageRepository {

    protected final DBI dbi;

    protected AbstractUsageRepository() {
        this.dbi = new DBI(DataSourceRegistry.getInstance().getDefaultDataSource());
    }

}
