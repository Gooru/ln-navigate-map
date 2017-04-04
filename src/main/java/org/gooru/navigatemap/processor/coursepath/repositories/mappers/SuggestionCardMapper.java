package org.gooru.navigatemap.processor.coursepath.repositories.mappers;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.gooru.navigatemap.processor.data.SuggestionCard4Collection;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

/**
 * @author ashish on 4/4/17.
 */
public class SuggestionCardMapper implements ResultSetMapper<SuggestionCard4Collection> {
    public SuggestionCard4Collection map(int index, ResultSet r, StatementContext ctx) throws SQLException {
        SuggestionCard4Collection result = new SuggestionCard4Collection();

        result.setId(r.getString("id"));
        result.setTitle(r.getString("title"));
        result.setFormat(r.getString("format"));
        result.setSubformat(r.getString("subformat"));
        result.setMetadata(r.getString("metadata"));
        result.setTaxonomy(r.getString("taxonomy"));
        result.setThumbnail(r.getString("thumbnail"));

        return result;
    }

}
