package org.gooru.navigatemap.processor.coursepath.repositories;

import java.util.List;
import java.util.Set;

import org.gooru.navigatemap.processor.data.SuggestionCard;

/**
 * @author ashish on 7/5/17.
 */
public interface SuggestionCardService {
    List<SuggestionCard> suggestionCardForCollections(Set<String> collections);

    List<SuggestionCard> suggestionCardForResources(Set<String> resources);

}
