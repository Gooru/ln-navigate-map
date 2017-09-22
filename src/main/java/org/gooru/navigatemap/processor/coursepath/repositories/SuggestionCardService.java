package org.gooru.navigatemap.processor.coursepath.repositories;

import java.util.List;

import org.gooru.navigatemap.processor.data.SuggestionCard;

/**
 * @author ashish on 7/5/17.
 */
public interface SuggestionCardService {
    List<SuggestionCard> suggestionCardForCollections(List<String> collections);

    List<SuggestionCard> suggestionCardForResources(List<String> resources);

}
