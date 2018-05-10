package org.gooru.navigatemap.processor.next.pathfinder;

import java.util.List;

/**
 * Provides a mechanism to search for suggestions related to specified competencies. The competencies would be GUT
 * codes and not FW codes. These suggestions are fetched considering user performance (which currently is score).
 *
 * Once the suggestions are fetched in order of weight (calculated by backend jobs and
 * populated in our target table), the suggestions are filtered out based on user.
 *
 * @author ashish on 10/5/18.
 */
interface SuggestionFinder {

    List<String> findSignatureCollectionsForCompetencies(PathFinderContext context, List<String> competencies);

    List<String> findSignatureAssessmentsForCompetencies(PathFinderContext context, List<String> competencies);
}
