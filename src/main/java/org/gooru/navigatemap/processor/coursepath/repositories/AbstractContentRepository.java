package org.gooru.navigatemap.processor.coursepath.repositories;

import org.gooru.navigatemap.app.components.DataSourceRegistry;
import org.gooru.navigatemap.processor.utilities.jdbi.DBICreator;
import org.skife.jdbi.v2.DBI;

/**
 * @author ashish on 7/3/17.
 */
abstract class AbstractContentRepository {
    protected final DBI dbi;

    protected AbstractContentRepository() {
        this.dbi = DBICreator.createDBI(DataSourceRegistry.getInstance().getDefaultDataSource());
    }
}
