package org.gooru.navigatemap.processor.next.pathfinder;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.gooru.navigatemap.infra.data.ContentAddress;
import org.gooru.navigatemap.infra.data.CurrentItemSubtype;
import org.gooru.navigatemap.infra.data.CurrentItemType;
import org.gooru.navigatemap.infra.data.context.ContextAttributes;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

/**
 * @author ashish on 7/3/17.
 */
public class ContentAddressMapper implements ResultSetMapper<ContentAddress> {
    public ContentAddress map(int index, ResultSet r, StatementContext ctx) throws SQLException {
        ContentAddress result = new ContentAddress();
        result.setCollection(r.getString("id"));
        result.setCurrentItem(r.getString("id"));
        String subType = r.getString("subformat");
        result.setCurrentItemSubtype(subType == null ? null : CurrentItemSubtype.builder(subType));
        result.setCurrentItemType(CurrentItemType.builder(r.getString("format")));
        result.setCourse(r.getString(ContextAttributes.COURSE_ID));
        result.setLesson(r.getString(ContextAttributes.LESSON_ID));
        result.setUnit(r.getString(ContextAttributes.UNIT_ID));
        result.setPathId(r.getLong(ContextAttributes.PATH_ID));
        result.setVisibility(r.getString("visibility"));
        return result;
    }
}
