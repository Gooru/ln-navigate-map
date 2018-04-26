package org.gooru.navigatemap.processor.postprocessor.repositories;

import org.gooru.navigatemap.processor.utilities.jdbi.DBICreator;
import org.skife.jdbi.v2.DBI;

/**
 * @author ashish on 22/9/17.
 */
public final class PostProcessorRespositoryBuilder {

    private PostProcessorRespositoryBuilder() {
        throw new AssertionError();
    }

    public static PostProcessorRepository build() {
        DBI dbi = DBICreator.getDbiForDefaultDS();
        return new PostProcessorRepositoryImpl(dbi);

    }
}
