package org.gooru.navigatemap.processor.coursepath.repositories;

import java.util.UUID;

import org.gooru.navigatemap.processor.coursepath.repositories.dao.ContentFinderDao;
import org.gooru.navigatemap.processor.data.ContentAddress;

/**
 * @author ashish on 7/3/17.
 */
class ContentFinderRepositoryImpl extends AbstractContentRepository implements ContentFinderRepository {

    @Override
    public ContentAddress findFirstContentInCourse(UUID course) {

        ContentFinderDao finderDao = dbi.onDemand(ContentFinderDao.class);

        return finderDao.findFirstContentInCourse(course.toString());

    }
}
