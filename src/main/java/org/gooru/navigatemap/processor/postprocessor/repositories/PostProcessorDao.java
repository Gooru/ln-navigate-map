package org.gooru.navigatemap.processor.postprocessor.repositories;

import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;

/**
 * @author ashish on 22/9/17.
 */
public interface PostProcessorDao {

    @SqlUpdate("update signature_resources set suggested_count = suggested_count + 1 where resource_id = :resourceId")
    void incrementResourceSuggestCount(@Bind("resourceId") String resourceId);
}
