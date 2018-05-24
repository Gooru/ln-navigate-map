package org.gooru.navigatemap.app.components.utilities;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.sql.DataSource;

import org.skife.jdbi.v2.DBI;
import org.skife.jdbi.v2.Handle;
import org.skife.jdbi.v2.util.StringColumnMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

/**
 * @author ashish on 14/3/17.
 */
public final class DbLookupUtility {
    private List<ScoreRange> scoreRanges;
    private Float thresholdForCompetencyCompletionBasedOnAssessment;

    private static final Logger LOGGER = LoggerFactory.getLogger(DbLookupUtility.class);

    public Float thresholdForCompetencyCompletionBasedOnAssessment() {
        return thresholdForCompetencyCompletionBasedOnAssessment;
    }

    public String scoreRangeNameByScore(double score) {
        for (ScoreRange scoreRange : scoreRanges) {
            if (score >= scoreRange.min && score <= scoreRange.max) {
                return scoreRange.rangeName;
            }
        }
        LOGGER.warn("Not able to find range for score: {}", score);
        return null;
    }

    public static DbLookupUtility getInstance() {
        return DbLookupUtility.Holder.INSTANCE;
    }

    private volatile boolean initialized = false;

    private DbLookupUtility() {
    }

    public void initialize(DataSource defaultDataSource) {
        if (!initialized) {
            synchronized (DbLookupUtility.Holder.INSTANCE) {
                if (!initialized) {
                    initializeScoreRanges(defaultDataSource);
                    initializeThresholdForCompetencyCompletionBasedOnAssessment(defaultDataSource);
                    initialized = true;
                }
            }
        }
    }

    private void initializeThresholdForCompetencyCompletionBasedOnAssessment(DataSource dataSource) {
        Handle handle = DBI.open(dataSource);
        String threshold = Queries.getThresholdForCompetencyCompletionBasedOnAssessment(handle);
        thresholdForCompetencyCompletionBasedOnAssessment = Float.valueOf(threshold);
        LOGGER.debug("Competency completion threshold for assessment initialized with: {}",
            thresholdForCompetencyCompletionBasedOnAssessment);
    }

    private void initializeScoreRanges(DataSource dataSource) {
        Handle handle = DBI.open(dataSource);
        String scoreRangesString = Queries.getPreTestScoreRanges(handle);
        JsonArray scoreRangesArray = new JsonArray(scoreRangesString);
        scoreRanges = ScoreRange.scoreRangeBuilder(scoreRangesArray);
    }

    private final static class ScoreRange {
        private static final String MAX = "max";
        private static final String MIN = "min";
        private static final String NAME = "name";
        private final String rangeName;
        private final double min;
        private final double max;

        private ScoreRange(String rangeName, float min, float max) {
            this.rangeName = rangeName;
            this.min = min;
            this.max = max;
        }

        public String toString() {
            return "name: " + rangeName + "; min: " + min + "; max: " + max;
        }

        static List<ScoreRange> scoreRangeBuilder(JsonArray input) {
            Objects.requireNonNull(input);
            JsonObject value;
            List<ScoreRange> result = new ArrayList<>(input.size());
            for (Object o : input) {
                value = new JsonObject(String.valueOf(o));
                final ScoreRange scoreRange =
                    new ScoreRange(value.getString(NAME), value.getFloat(MIN), value.getFloat(MAX));
                LOGGER.debug("Adding score range: {}", scoreRange.toString());
                result.add(scoreRange);
            }
            return result;
        }
    }

    private static final class Holder {
        private static final DbLookupUtility INSTANCE = new DbLookupUtility();
    }

    private static final class Queries {
        private static final String DEFAULT_LOOKUP_QUERY = "SELECT value FROM default_lookup where key = :key";
        private static final String KEY = "key";
        private static final String PRE_TEST_SCORE_RANGES_KEY = "pre-test-score-ranges";
        private static final String COMPETENCY_COMPLETION_THRESHOLD_FOR_ASSESSMENT =
            "competency-completion-threshold-for-assessment";

        static String getPreTestScoreRanges(Handle handle) {
            return handle.createQuery(DEFAULT_LOOKUP_QUERY).bind(KEY, PRE_TEST_SCORE_RANGES_KEY)
                .map(StringColumnMapper.INSTANCE).first();
        }

        static String getThresholdForCompetencyCompletionBasedOnAssessment(Handle handle) {
            return handle.createQuery(DEFAULT_LOOKUP_QUERY).bind(KEY, COMPETENCY_COMPLETION_THRESHOLD_FOR_ASSESSMENT)
                .map(StringColumnMapper.INSTANCE).first();
        }
    }

}
