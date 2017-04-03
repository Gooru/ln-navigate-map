package org.gooru.navigatemap.processor.coursepath.repositories;

import org.gooru.navigatemap.processor.data.ContentAddress;
import org.gooru.navigatemap.processor.data.SuggestionContext;

/**
 * @author ashish on 3/4/17.
 */
public interface ContentSuggestionsRepository {
    SuggestionContext findPreLessonSuggestions(ContentAddress contentAddress, String userId);

    SuggestionContext findPostLessonSuggestions(ContentAddress contentAddress, String userId);
}
