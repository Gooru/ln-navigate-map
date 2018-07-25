package org.gooru.navigatemap.processor.postprocessor;

import java.util.List;

/**
 * @author ashish on 25/7/18.
 */
interface SuggestionTrackerModelsBuilder {

    List<SuggestionTrackerModel> build();

    static SuggestionTrackerModelsBuilder buildForSystemSuggestion(PostProcessorNextCommand command) {
        return new SuggestionsTrackerModelsBuilderForSystemSuggestions(command);
    }
}
