package org.gooru.navigatemap.processor.postprocessor;

import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;

/**
 * @author ashish on 22/9/17.
 */
interface PostProcessorDao {

    @SqlUpdate("update user_navigation_paths set serve_count = serve_count + 1 where id = :id")
    void updatePathServeCount(@Bind("id") long id);
}
