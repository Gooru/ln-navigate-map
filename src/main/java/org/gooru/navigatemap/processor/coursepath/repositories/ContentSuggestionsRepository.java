package org.gooru.navigatemap.processor.coursepath.repositories;

import java.util.List;
import java.util.Set;

import org.gooru.navigatemap.processor.data.ContentAddress;
import org.gooru.navigatemap.processor.data.SuggestionCard4Collection;
import org.gooru.navigatemap.processor.data.SuggestionContext;

/**
 * @author ashish on 3/4/17.
 */
public interface ContentSuggestionsRepository {
    SuggestionContext findPreLessonSuggestions(ContentAddress contentAddress, String userId);

    SuggestionContext findPostLessonSuggestions(ContentAddress contentAddress, String userId);

    List<SuggestionCard4Collection> suggestionCardForCollections(Set<String> collections);
}
