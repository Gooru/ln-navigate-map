package org.gooru.navigatemap.processor.next.pathfinder;

import java.util.UUID;
import org.skife.jdbi.v2.DBI;

/**
 * @author ashish on 8/5/18.
 */
final class ContentVerifierBuilder {

  private ContentVerifierBuilder() {
    throw new AssertionError();
  }

  static ContentVerifier buildContentVisibilityVerifier(UUID classId, DBI dbi) {
    return ContentVisibilityVerifier.build(classId, dbi);
  }

  static ContentVerifier buildContentNonSkippabilityVerifier(DBI dbi, String user) {
    return ContentNonSkippabilityVerifier.build(dbi, user);
  }

  static ContentVerifier buildRoute0ContentNonSkippabilityVerifier(DBI dbi, String user) {
    return ContentNonSkippabilityVerifier.build(dbi, user);
  }

}
