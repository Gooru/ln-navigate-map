package org.gooru.navigatemap.processor.postprocessor;

import org.gooru.navigatemap.infra.utilities.jdbi.DBICreator;
import org.skife.jdbi.v2.DBI;

import io.vertx.core.json.JsonObject;

/**
 * @author ashish on 24/7/18.
 */
interface PostProcessorHandler {

    void handle(JsonObject requestData);

    static PostProcessorHandler buildForNextCommand(DBI dbi) {
        return new PostProcessNextHandler(dbi);
    }

    static PostProcessorHandler buildForSystemSuggestionsAddCommand(DBI dbi) {
        return new PostProcessSystemSuggestionsAddHandler(dbi);
    }

    static PostProcessorHandler buildForTeacherSuggestionsAddCommand(DBI dbi) {
        return new PostProcessTeacherSuggestionAddHandler(dbi);
    }

    static PostProcessorHandler buildForNextCommand() {
        return new PostProcessNextHandler(DBICreator.getDbiForDefaultDS());
    }

    static PostProcessorHandler buildForSystemSuggestionsAddCommand() {
        return new PostProcessSystemSuggestionsAddHandler(DBICreator.getDbiForDefaultDS());
    }

    static PostProcessorHandler buildForTeacherSuggestionsAddCommand() {
        return new PostProcessTeacherSuggestionAddHandler(DBICreator.getDbiForDefaultDS());
    }

}
