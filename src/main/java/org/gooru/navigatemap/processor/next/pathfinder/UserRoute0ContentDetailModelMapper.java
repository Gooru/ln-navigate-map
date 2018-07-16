package org.gooru.navigatemap.processor.next.pathfinder;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import org.gooru.navigatemap.infra.data.CurrentItemType;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

/**
 * @author ashish on 12/7/18.
 */
public class UserRoute0ContentDetailModelMapper implements ResultSetMapper<UserRoute0ContentDetailModel> {

    @Override
    public UserRoute0ContentDetailModel map(int index, ResultSet r, StatementContext ctx) throws SQLException {
        UserRoute0ContentDetailModel result = new UserRoute0ContentDetailModel();
        result.setId(r.getLong(MapperFields.ID));
        result.setUserRoute0ContentId(r.getLong(MapperFields.USER_ROUTE0_CONTENT_ID));
        result.setUnitId(convertToUUID(r.getString(MapperFields.UNIT_ID)));
        result.setUnitTitle(r.getString(MapperFields.UNIT_TITLE));
        result.setUnitSequence(r.getInt(MapperFields.UNIT_SEQUENCE));
        result.setLessonId(convertToUUID(r.getString(MapperFields.LESSON_ID)));
        result.setLessonTitle(r.getString(MapperFields.LESSON_TITLE));
        result.setLessonSequence(r.getInt(MapperFields.LESSON_SEQUENCE));
        result.setCollectionId(convertToUUID(r.getString(MapperFields.COLLECTION_ID)));
        result.setCollectionType(convertToCurrentItemType(r.getString(MapperFields.COLLECTION_TYPE)));
        result.setCollectionSequence(r.getInt(MapperFields.COLLECTION_SEQUENCE));
        result.setRoute0Sequence(r.getInt(MapperFields.ROUTE0_SEQUENCE));
        return result;
    }

    private UUID convertToUUID(String string) {
        if (string != null) {
            return UUID.fromString(string);
        }
        return null;
    }

    private CurrentItemType convertToCurrentItemType(String string) {
        if (string != null) {
            return CurrentItemType.builder(string);
        }
        return null;
    }

    private static class MapperFields {
        private static final String ID = "id";
        private static final String USER_ROUTE0_CONTENT_ID = "user_route0_content_id";
        private static final String UNIT_ID = "unit_id";
        private static final String UNIT_TITLE = "unit_title";
        private static final String UNIT_SEQUENCE = "unit_sequence";
        private static final String LESSON_ID = "lesson_id";
        private static final String LESSON_TITLE = "lesson_title";
        private static final String LESSON_SEQUENCE = "lesson_sequence";
        private static final String COLLECTION_ID = "collection_id";
        private static final String COLLECTION_TYPE = "collection_type";
        private static final String COLLECTION_SEQUENCE = "collection_sequence";
        private static final String ROUTE0_SEQUENCE = "route0_sequence";

        private MapperFields() {
            throw new AssertionError();
        }
    }

}
