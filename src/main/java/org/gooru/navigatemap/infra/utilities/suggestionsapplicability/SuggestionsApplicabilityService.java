package org.gooru.navigatemap.infra.utilities.suggestionsapplicability;

import java.util.UUID;

import org.gooru.navigatemap.app.components.AppConfiguration;
import org.skife.jdbi.v2.DBI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.vertx.core.json.JsonObject;

/**
 * @author ashish on 2/5/18.
 */
class SuggestionsApplicabilityService {

    private SuggestionsApplicabilityDao suggestionsApplicabilityDao;
    private final DBI dbi;
    private static final Logger LOGGER = LoggerFactory.getLogger(SuggestionsApplicabilityService.class);

    SuggestionsApplicabilityService(DBI dbi) {
        this.dbi = dbi;
    }

    boolean areSuggestionsApplicable(UUID classId, UUID courseId) {
        suggestionsApplicabilityDao = dbi.onDemand(SuggestionsApplicabilityDao.class);
        if (classId != null) {
            return areSuggestionsApplicableBasedOnClass(classId);
        }
        return areSuggestionsApplicableBasedOnCourseVersion(courseId);
    }

    private boolean areSuggestionsApplicableBasedOnCourseVersion(UUID course) {
        return AppConfiguration.getInstance().getSuggestionsApplicabilityCourseVersion()
            .equals(suggestionsApplicabilityDao.fetchCourseVersion(course));
    }

    private boolean areSuggestionsApplicableBasedOnClass(UUID classId) {
        String courseId = suggestionsApplicabilityDao.fetchCourseForClass(classId);
        if (courseId == null) {
            LOGGER.info("Course is not assigned to class '{}' hence suggestions not applicable", classId.toString());
            return false;
        }
        return AppConfiguration.getInstance().getSuggestionsApplicabilityCourseVersion()
            .equals(suggestionsApplicabilityDao.fetchCourseVersion(courseId));
    }

}
