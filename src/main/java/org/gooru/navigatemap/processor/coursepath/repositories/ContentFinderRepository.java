package org.gooru.navigatemap.processor.coursepath.repositories;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.gooru.navigatemap.processor.data.ContentAddress;
import org.gooru.navigatemap.processor.data.SuggestionCard4Collection;

/**
 * @author ashish on 3/3/17.
 */
public interface ContentFinderRepository {

    ContentAddress findFirstContentInCourse(UUID course);

    ContentAddress findNextContentFromCUL(ContentAddress address);

    List<String> findBenchmarkAssessments(List<String> competencies);

    Set<String> findPreTestsAssessments(Set<String> competencies);

    Set<String> findPostTestsAssessments(Set<String> competencies);

    List<String> findCompetenciesForPostTest(UUID postTestId);

    Set<String> findCompetenciesForLesson(ContentAddress contentAddress);

    List<SuggestionCard4Collection> createSuggestionsCardForCollections(Set<String> collections);
}
