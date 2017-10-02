package org.gooru.navigatemap.processor.data;

import java.util.*;

/**
 * @author ashish on 28/2/17.
 */
public final class SuggestionContext {
    private final Set<String> assessments = new HashSet<>();
    private final Set<String> collections = new HashSet<>();
    private final List<SignatureResource> resources = new ArrayList<>();

    public void addAssessment(String id) {
        assessments.add(id);
    }

    public void removeAssessment(String id) {
        assessments.remove(id);
    }

    public void addCollections(Set<String> collectionsToSuggest) {
        if (collectionsToSuggest.isEmpty()) {
            return;
        }
        collections.addAll(collectionsToSuggest);
    }

    public void addAssessments(Set<String> assessmentsToSuggest) {
        if (assessmentsToSuggest.isEmpty()) {
            return;
        }
        assessments.addAll(assessmentsToSuggest);
    }

    public void addCollection(String id) {
        collections.add(id);
    }

    public void removeCollection(String id) {
        collections.remove(id);
    }

    public boolean hasSuggestions() {
        return (!assessments.isEmpty() || !collections.isEmpty() || !resources.isEmpty());
    }

    public boolean hasAssessmentsSuggested() {
        return !assessments.isEmpty();
    }

    public boolean hasCollectionsSuggested() {
        return !collections.isEmpty();
    }

    public Set<String> getAssessments() {
        return Collections.unmodifiableSet(assessments);
    }

    public Set<String> getCollections() {
        return Collections.unmodifiableSet(collections);
    }

    public boolean hasResourcesSuggested() {
        return !resources.isEmpty();
    }

    public List<SignatureResource> getResources() {
        return Collections.unmodifiableList(resources);
    }

    public void addResources(List<SignatureResource> resourcesToSuggest) {
        if (resourcesToSuggest.isEmpty()) {
            return;
        }
        resources.addAll(resourcesToSuggest);
    }

    public void addResource(SignatureResource resource) {
        resources.add(resource);
    }

    public static SuggestionContext buildSuggestionContextWithAssessments(List<String> suggestedAssessments) {
        SuggestionContext suggestions = new SuggestionContext();
        if (suggestedAssessments != null && !suggestedAssessments.isEmpty()) {
            Set<String> suggestedSet = new HashSet<>(suggestedAssessments);
            suggestions.addAssessments(suggestedSet);
        }
        return suggestions;
    }

    public static SuggestionContext buildSuggestionContextWithCollections(List<String> suggestedCollections) {
        SuggestionContext suggestions = new SuggestionContext();
        if (suggestedCollections != null && !suggestedCollections.isEmpty()) {
            Set<String> suggestedSet = new HashSet<>(suggestedCollections);
            suggestions.addCollections(suggestedSet);
        }
        return suggestions;
    }

    public static SuggestionContext buildSuggestionContextWithResources(List<String> suggestedResources) {
        SuggestionContext suggestions = new SuggestionContext();
        if (suggestedResources != null && !suggestedResources.isEmpty()) {
            Set<String> suggestedSet = new HashSet<>(suggestedResources);
            suggestions.addCollections(suggestedSet);
        }
        return suggestions;
    }

    public static SuggestionContext buildSuggestionContextWithoutSuggestions() {
        return new SuggestionContext();
    }

}
