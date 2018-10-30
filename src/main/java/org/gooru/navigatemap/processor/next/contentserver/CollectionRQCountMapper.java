package org.gooru.navigatemap.processor.next.contentserver;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.gooru.navigatemap.infra.data.CollectionRQCount;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

/**
 * @author ashish on 31/5/17.
 */
public class CollectionRQCountMapper implements ResultSetMapper<CollectionRQCount> {

  @Override
  public CollectionRQCount map(int index, ResultSet r, StatementContext ctx) throws SQLException {
    CollectionRQCount result = new CollectionRQCount();

    result.setCollectionId(r.getString("collection_id"));
    result.setQuestionCount(r.getInt("question_count"));
    result.setResourceCount(r.getInt("resource_count"));

    return result;
  }

}
