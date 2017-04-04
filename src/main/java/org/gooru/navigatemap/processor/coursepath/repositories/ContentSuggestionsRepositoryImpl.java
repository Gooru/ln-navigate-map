package org.gooru.navigatemap.processor.coursepath.repositories;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.gooru.navigatemap.processor.data.ContentAddress;
import org.gooru.navigatemap.processor.data.SuggestionCard4Collection;
import org.gooru.navigatemap.processor.data.SuggestionContext;

/**
 * @author ashish on 3/4/17.
 */
final class ContentSuggestionsRepositoryImpl implements ContentSuggestionsRepository {

    private final ContentFilterRepository contentFilterRepository;
    private final ContentFinderRepository contentFinderRepository;

    ContentSuggestionsRepositoryImpl(ContentFilterRepository contentFilterRepository,
        ContentFinderRepository contentFinderRepository) {
        this.contentFilterRepository = contentFilterRepository;
        this.contentFinderRepository = contentFinderRepository;
    }

    @Override
    public SuggestionContext findPreLessonSuggestions(ContentAddress contentAddress, String userId) {
        if (contentAddress != null && contentAddress.getLesson() != null) {
            Set<String> competencies = contentFinderRepository.findCompetenciesForLesson(contentAddress);
            if (competencies != null && !competencies.isEmpty()) {
                Set<String> preTests = contentFinderRepository.findPreTestsAssessments(competencies);
                if (preTests != null && !preTests.isEmpty()) {
                    List<String> preTestsList = new ArrayList<>(preTests);
                    List<String> preTestsFilteredList =
                        contentFilterRepository.filterPreTestForNotAddedByUser(preTestsList, userId);
                    if (preTestsFilteredList != null && !preTestsFilteredList.isEmpty()) {
                        return SuggestionContext.buildSuggestionContextWithAssessments(preTestsFilteredList);
                    }
                }
            }
        }
        return SuggestionContext.buildSuggestionContextWithoutSuggestions();
    }

    @Override
    public SuggestionContext findPostLessonSuggestions(ContentAddress contentAddress, String userId) {
        if (contentAddress != null && contentAddress.getLesson() != null) {
            Set<String> competencies = contentFinderRepository.findCompetenciesForLesson(contentAddress);
            if (competencies != null && !competencies.isEmpty()) {
                Set<String> postTests = contentFinderRepository.findPostTestsAssessments(competencies);
                if (postTests != null && !postTests.isEmpty()) {
                    List<String> postTestsList = new ArrayList<>(postTests);
                    List<String> postTestsFilteredList =
                        contentFilterRepository.filterPostTestForNotAddedByUser(postTestsList, userId);
                    if (postTestsFilteredList != null && !postTestsFilteredList.isEmpty()) {
                        return SuggestionContext.buildSuggestionContextWithAssessments(postTestsFilteredList);
                    }
                }
            }
        }
        return SuggestionContext.buildSuggestionContextWithoutSuggestions();
    }

    @Override
    public List<SuggestionCard4Collection> suggestionCardForCollections(Set<String> collections) {
        if (collections != null && !collections.isEmpty()) {
            return contentFinderRepository.createSuggestionsCardForCollections(collections);
        }
        return Collections.emptyList();
    }

}
