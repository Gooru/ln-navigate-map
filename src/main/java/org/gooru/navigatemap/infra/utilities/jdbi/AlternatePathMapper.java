package org.gooru.navigatemap.infra.utilities.jdbi;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import org.gooru.navigatemap.infra.data.AlternatePath;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author ashish on 6/4/17.
 */
public class AlternatePathMapper implements ResultSetMapper<AlternatePath> {

    private static final Logger LOGGER = LoggerFactory.getLogger(AlternatePathMapper.class);

    @Override
    public AlternatePath map(int index, ResultSet r, StatementContext ctx) throws SQLException {
        AlternatePath result = new AlternatePath();

        result.setId(r.getLong("id"));
        result.setUserId(safeStringToUUID(r.getString("ctx_user_id")));
        result.setClassId(safeStringToUUID(r.getString("ctx_class_id")));
        result.setCourseId(safeStringToUUID(r.getString("ctx_course_id")));
        result.setUnitId(safeStringToUUID(r.getString("ctx_unit_id")));
        result.setLessonId(safeStringToUUID(r.getString("ctx_lesson_id")));
        result.setCollectionId(safeStringToUUID(r.getString("ctx_collection_id")));
        result.setSuggestedContentId(safeStringToUUID(r.getString("suggested_content_id")));
        result.setSuggestedContentType(r.getString("suggested_content_type"));
        result.setSuggestedContentSubtype(r.getString("suggested_content_subtype"));
        result.setServeCount(r.getLong("serve_count"));

        return result;
    }

    private static UUID safeStringToUUID(String input) {
        if (input == null || input.isEmpty()) {
            return null;
        }
        try {
            return UUID.fromString(input);
        } catch (IllegalArgumentException e) {
            LOGGER.warn("Invalid string to be converted to UUID : '{}' ", input);
            return null;
        }
    }
}
