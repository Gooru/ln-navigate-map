package org.gooru.navigatemap.processor.postprocessor.repositories;

import org.gooru.navigatemap.processor.coursepath.repositories.dao.PostProcessorDao;
import org.skife.jdbi.v2.DBI;

/**
 * @author ashish on 22/9/17.
 */

class PostProcessorRepositoryImpl implements PostProcessorRepository {

    private final DBI dbi;

    PostProcessorRepositoryImpl(DBI dbi) {
        this.dbi = dbi;
    }

    @Override
    public void incrementResourceSuggestedCount(String resourceId) {
        PostProcessorDao postProcessorDao = dbi.onDemand(PostProcessorDao.class);
        postProcessorDao.incrementResourceSuggestCount(resourceId);
    }
}
