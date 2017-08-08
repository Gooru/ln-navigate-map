package org.gooru.navigatemap.processor.coursepath.repositories.global;

import org.gooru.navigatemap.processor.data.ContentAddress;
import org.gooru.navigatemap.processor.data.SuggestionContext;

/**
 * @author ashish on 3/4/17.
 */
public interface ContentSuggestionsService {
    SuggestionContext findPreLessonSuggestions(ContentAddress contentAddress, String userId);

    SuggestionContext findPostLessonSuggestions(ContentAddress contentAddress, String userId);

}
