package org.gooru.navigatemap.processor.next.pathfinder;

import org.gooru.navigatemap.infra.data.ContentAddress;

/**
 * @author ashish on 7/5/18.
 */
interface ContentFinder {

  ContentAddress findContent(PathFinderContext context);

}
