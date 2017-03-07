package org.gooru.navigatemap.processor.coursepath.repositories;

import java.util.UUID;

import org.gooru.navigatemap.processor.data.ContentAddress;

/**
 * @author ashish on 3/3/17.
 */
public interface ContentFinderRepository {

    ContentAddress findFirstContentInCourse(UUID course);
}
