package org.gooru.navigatemap.processor.data;

import java.util.HashSet;
import java.util.Set;

/**
 * @author ashish on 28/2/17.
 */
public final class SuggestionContext {
    private final Set<String> assessments = new HashSet<>();
    private final Set<String> collections = new HashSet<>();

    public void addAssessment(String id) {
        assessments.add(id);
    }

    public void removeAssessment(String id) {
        assessments.remove(id);
    }

    public void addCollection(String id) {
        collections.add(id);
    }

    public void removeCollection(String id) {
        collections.remove(id);
    }

    public boolean hasSuggestions() {
        return (assessments.size() > 0 || collections.size() > 0);
    }
}
