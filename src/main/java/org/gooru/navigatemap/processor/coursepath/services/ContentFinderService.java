package org.gooru.navigatemap.processor.coursepath.services;

import java.util.UUID;

import org.gooru.navigatemap.processor.data.ContentAddress;

/**
 * @author ashish on 3/3/17.
 */
public interface ContentFinderService {

    ContentAddress findFirstContentInCourse(UUID course);

    ContentAddress findFirstContentInCourse(UUID course, UUID unit);

    ContentAddress findFirstContentInCourse(UUID course, UUID unit, UUID lesson);
}
