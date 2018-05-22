package org.gooru.navigatemap.infra.utilities.suggestionsapplicability;

import java.util.UUID;

import org.skife.jdbi.v2.DBI;

import io.vertx.core.json.JsonObject;

/**
 * @author ashish on 2/5/18.
 */
class SuggestionsApplicabilityService {

    private SuggestionsApplicabilityDao suggestionsApplicabilityDao;
    private final DBI dbi;
    private static final String RESCOPE_SETTING_KEY = "rescope";

    SuggestionsApplicabilityService(DBI dbi) {
        this.dbi = dbi;
    }

    boolean areSuggestionsApplicable(UUID classId, UUID courseId) {
        suggestionsApplicabilityDao = dbi.onDemand(SuggestionsApplicabilityDao.class);
        if (classId != null) {
            return rescopeApplicableBasedOnClassSettings(classId);
        }
        return areSuggestionsApplicableBasedOnCourseVersion(courseId);
    }

    private boolean areSuggestionsApplicableBasedOnCourseVersion(UUID course) {
        String version = suggestionsApplicabilityDao.findCourseVersion(course);
        return (version != null);
    }

    private boolean rescopeApplicableBasedOnClassSettings(UUID classId) {
        String setting = suggestionsApplicabilityDao.fetchClassSetting(classId);
        if (setting == null) {
            throw new IllegalStateException("Class setting should not be null");
        }
        JsonObject jsonSetting = new JsonObject(setting);
        return Boolean.TRUE.equals(jsonSetting.getBoolean(RESCOPE_SETTING_KEY));
    }

}
