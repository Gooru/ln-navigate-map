package org.gooru.navigatemap.processor.next.pathfinder;

import org.gooru.navigatemap.infra.data.ContentAddress;
import org.skife.jdbi.v2.DBI;

/**
 * This class just looks at Course path to find the content which is to be served next. This class
 * takes a criteria based on which it decides whether the content is eligible for play. The criteria
 * verification is implemented by {@link ContentVerifier}
 *
 * @author ashish on 5/4/17.
 */
class AlternatePathUnawareMainPathContentFinder implements ContentFinder {

  private final DBI dbi;
  private final ContentFinderCriteria criteria;
  private PathFinderContext context;

  AlternatePathUnawareMainPathContentFinder(DBI dbi, ContentFinderCriteria criteria) {
    this.dbi = dbi;
    this.criteria = criteria;
  }

  @Override
  public ContentAddress findContent(PathFinderContext context) {
    this.context = context;
    ContentAddress result = findNextValidContent(context.getContentAddress());
    if (result != null) {
      return result;
    }
    return new ContentAddress();

  }

  private ContentAddress findNextValidContent(ContentAddress contentAddress) {
    if (context.isMilestoneViewApplicable()) {
      return findNextValidContentInMilestoneView(contentAddress);
    } else {
      return findNextValidContentInCULView(contentAddress);
    }
  }

  private ContentAddress findNextValidContentInCULView(ContentAddress contentAddress) {
    return new AlternatePathUnawareCULViewContentFinder(dbi, criteria, context)
        .findNext(contentAddress);
  }

  private ContentAddress findNextValidContentInMilestoneView(ContentAddress contentAddress) {
    return new AlternatePathUnawareMilestoneViewContentFinder(dbi, criteria, context)
        .findNext(contentAddress);
  }


}
