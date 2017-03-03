package org.gooru.navigatemap.processor.data;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * @author ashish on 28/2/17.
 */
public final class SuggestionContext {
    private final Set<UUID> assessments = new HashSet<>();
    private final Set<UUID> collections = new HashSet<>();

    public void addAssessment(UUID uuid) {
        assessments.add(uuid);
    }

    public void removeAssessment(UUID uuid) {
        assessments.remove(uuid);
    }

    public void addCollection(UUID uuid) {
        collections.add(uuid);
    }

    public void removeCollection(UUID uuid) {
        collections.remove(uuid);
    }

    public boolean hasSuggestions() {
        return (assessments.size() > 0 || collections.size() > 0);
    }
}
