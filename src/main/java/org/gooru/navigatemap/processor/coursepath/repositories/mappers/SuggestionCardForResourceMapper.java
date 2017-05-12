package org.gooru.navigatemap.processor.coursepath.repositories.mappers;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.gooru.navigatemap.processor.data.SuggestionCard;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

/**
 * @author ashish on 4/4/17.
 */
public class SuggestionCardForResourceMapper implements ResultSetMapper<SuggestionCard> {
    private static final String CONTENT_FORMAT_RESOURCE = "resource";

    public SuggestionCard map(int index, ResultSet r, StatementContext ctx) throws SQLException {
        SuggestionCard result = new SuggestionCard();

        result.setId(r.getString("id"));
        result.setTitle(r.getString("title"));
        result.setFormat(CONTENT_FORMAT_RESOURCE);
        result.setSubformat(r.getString("content_subformat"));
        result.setMetadata(r.getString("metadata"));
        result.setTaxonomy(r.getString("taxonomy"));
        result.setThumbnail(r.getString("thumbnail"));

        return result;
    }

}
