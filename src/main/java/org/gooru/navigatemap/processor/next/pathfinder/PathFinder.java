package org.gooru.navigatemap.processor.next.pathfinder;

/**
 * @author ashish on 7/5/18.
 */
interface PathFinder {

  PathFinderResult findPath(PathFinderContext context);
}
