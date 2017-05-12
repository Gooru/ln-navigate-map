package org.gooru.navigatemap.processor.coursepath.repositories.mappers;

import static java.util.Objects.nonNull;

import java.sql.Array;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

/**
 * @author ashish on 8/5/17.
 */
public class JavaSqlArrayToStringMapper implements ResultSetMapper<String[]> {
    @Override
    public String[] map(int index, ResultSet r, StatementContext ctx) throws SQLException {
        Array array = r.getArray(1);
        return nonNull(array) ? (String[]) array.getArray() : null;
    }
}
