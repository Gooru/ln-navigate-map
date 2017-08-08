package org.gooru.navigatemap.processor.utilities.jdbi;

import javax.sql.DataSource;

import org.skife.jdbi.v2.DBI;

/**
 * @author ashish on 15/3/17.
 */
public final class DBICreator {

    private DBICreator() {
        throw new AssertionError();
    }

    public static DBI createDBI(DataSource dataSource) {
        DBI dbi = new DBI(dataSource);
        dbi.registerArgumentFactory(new PostgresIntegerArrayArgumentFactory());
        dbi.registerArgumentFactory(new PostgresStringArrayArgumentFactory());
        dbi.registerArgumentFactory(new PostgresUUIDArrayArgumentFactory());
        return dbi;
    }
}
