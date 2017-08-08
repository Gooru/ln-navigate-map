package org.gooru.navigatemap.processor.coursepath.repositories.global;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.gooru.navigatemap.processor.data.AlternatePath;
import org.gooru.navigatemap.processor.data.ContentAddress;

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

    List<String> findBackfillsForPreTestAndScoreRange(UUID preTestId, String scoreRangeName);

    AlternatePath findAlternatePathForUser(ContentAddress currentAddress, String user);

    AlternatePath findAlternatePathForUserInClass(ContentAddress currentAddress, String user, String classId);

    List<AlternatePath> findChildPathsOfTypeBA(AlternatePath currentPath);

    List<AlternatePath> findChildPathsOfTypeBackfill(AlternatePath currentPath);

    List<AlternatePath> findChildPathsOfTypePostTest(ContentAddress currentAddress, String user, String classId);

    List<AlternatePath> findChildPathsOfTypePreTest(ContentAddress currentAddress, String user, String classId);

    boolean validateContentAddress(ContentAddress contentAddress);

    String findCourseVersion(UUID course);
}
