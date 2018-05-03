package org.gooru.navigatemap.processor.next.contentserver;

import java.util.List;

import org.gooru.navigatemap.infra.data.SuggestionCard;

/**
 * @author ashish on 7/5/17.
 */
public interface SuggestionCardService {
    List<SuggestionCard> suggestionCardForCollections(List<String> collections);

}
