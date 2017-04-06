package org.gooru.navigatemap.processor.coursepath.repositories.mappers;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import org.gooru.navigatemap.processor.data.AlternatePath;
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
        result.setCtxCourse(safeStringToUUID(r.getString("ctx_course_id")));
        result.setCtxUnit(safeStringToUUID(r.getString("ctx_unit_id")));
        result.setCtxLesson(safeStringToUUID(r.getString("ctx_lesson_id")));
        result.setCtxCollection(safeStringToUUID(r.getString("ctx_collection_id")));
        result.setParentPathId(r.getLong("parent_path_id"));
        result.setParentPathType(r.getString("parent_path_type"));
        result.setTargetCourse(safeStringToUUID(r.getString("target_course_id")));
        result.setTargetUnit(safeStringToUUID(r.getString("target_unit_id")));
        result.setTargetLesson(safeStringToUUID(r.getString("target_lesson_id")));
        result.setTargetCollection(safeStringToUUID(r.getString("target_collection_id")));
        result.setTargetContentType(r.getString("target_content_type"));
        result.setTargetContentSubtype(r.getString("target_content_subtype"));

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
