package org.gooru.navigatemap.processor.coursepath.repositories.mappers;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.gooru.navigatemap.processor.context.ContextAttributes;
import org.gooru.navigatemap.processor.data.CollectionSubtype;
import org.gooru.navigatemap.processor.data.CollectionType;
import org.gooru.navigatemap.processor.data.ContentAddress;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

/**
 * @author ashish on 7/3/17.
 */
public class ContentAddressMapper implements ResultSetMapper<ContentAddress> {
    public ContentAddress map(int index, ResultSet r, StatementContext ctx) throws SQLException {
        ContentAddress result = new ContentAddress();
        result.setCollection(r.getString("id"));
        String subType = r.getString("subformat");
        result.setCollectionSubtype(subType == null ? null : CollectionSubtype.builder(subType));
        result.setCollectionType(CollectionType.builder(r.getString("format")));
        result.setCourse(r.getString(ContextAttributes.COURSE_ID));
        result.setLesson(r.getString(ContextAttributes.LESSON_ID));
        result.setUnit(r.getString(ContextAttributes.UNIT_ID));
        return result;
    }
}
