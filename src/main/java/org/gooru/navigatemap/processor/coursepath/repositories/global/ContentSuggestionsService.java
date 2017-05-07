package org.gooru.navigatemap.processor.coursepath.repositories.global;

import java.util.List;
import java.util.Set;

import org.gooru.navigatemap.processor.data.ContentAddress;
import org.gooru.navigatemap.processor.data.SuggestionCard;
import org.gooru.navigatemap.processor.data.SuggestionContext;

/**
 * @author ashish on 3/4/17.
 */
public interface ContentSuggestionsService {
    SuggestionContext findPreLessonSuggestions(ContentAddress contentAddress, String userId);

    SuggestionContext findPostLessonSuggestions(ContentAddress contentAddress, String userId);

    List<SuggestionCard> suggestionCardForCollections(Set<String> collections);

    List<SuggestionCard> suggestionCardForResources(Set<String> resources);
}
