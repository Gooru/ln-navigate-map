package org.gooru.navigatemap.processor.postprocessor;

import org.gooru.navigatemap.infra.utilities.jdbi.DBICreator;
import org.skife.jdbi.v2.DBI;

import io.vertx.core.json.JsonObject;

/**
 * @author ashish on 23/7/18.
 */
public interface PostProcessorService {

    static PostProcessorService build() {
        return new PostProcessorServiceImpl(DBICreator.getDbiForDefaultDS());
    }

    static PostProcessorService build(DBI dbi) {
        return new PostProcessorServiceImpl(dbi);
    }

    void process(String op, JsonObject requestData);
}
