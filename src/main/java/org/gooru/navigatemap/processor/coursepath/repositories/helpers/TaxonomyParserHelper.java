package org.gooru.navigatemap.processor.coursepath.repositories.helpers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.gooru.navigatemap.processor.data.ContentAddress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.vertx.core.json.DecodeException;
import io.vertx.core.json.JsonObject;

/**
 * @author ashish on 8/5/17.
 */
public final class TaxonomyParserHelper {

    private static final Logger LOGGER = LoggerFactory.getLogger(TaxonomyParserHelper.class);

    private TaxonomyParserHelper() {
        throw new AssertionError();
    }

    public static Set<String> parseLessonTaxonomy(ContentAddress contentAddress, String lessonTaxonomy) {
        if (lessonTaxonomy != null && !lessonTaxonomy.isEmpty()) {
            // Lesson taxonomy is supposed to be a JsonObject with keys as competencies' internal code
            // Note that they are not GDT aware but FW specific
            try {
                JsonObject taxonomy = new JsonObject(lessonTaxonomy);
                return taxonomy.fieldNames();
            } catch (DecodeException ex) {
                LOGGER.warn("Invalid taxonomy string for address: Course='{}', Unit='{}', Lesson='{}'",
                    contentAddress.getCourse(), contentAddress.getUnit(), contentAddress.getLesson());
            }
        }
        return Collections.emptySet();
    }

    public static List<String> parseCollectionTaxonomy(ContentAddress contentAddress, String collectionTaxonomy) {
        if (collectionTaxonomy != null && !collectionTaxonomy.isEmpty()) {
            // Lesson taxonomy is supposed to be a JsonObject with keys as competencies' internal code
            // Note that they are not GDT aware but FW specific
            try {
                JsonObject taxonomy = new JsonObject(collectionTaxonomy);
                return new ArrayList<>(taxonomy.fieldNames());
            } catch (DecodeException ex) {
                LOGGER.warn("Invalid taxonomy string for address: Course='{}', Unit='{}', Lesson='{}', Collection='{}'",
                    contentAddress.getCourse(), contentAddress.getUnit(), contentAddress.getLesson(),
                    contentAddress.getCollection());
            }
        }
        return Collections.emptyList();
    }

}
