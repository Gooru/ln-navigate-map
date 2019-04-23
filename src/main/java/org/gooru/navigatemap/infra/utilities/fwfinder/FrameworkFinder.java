package org.gooru.navigatemap.infra.utilities.fwfinder;

import org.gooru.navigatemap.infra.data.RequestContext;
import org.gooru.navigatemap.infra.utilities.jdbi.DBICreator;
import org.skife.jdbi.v2.DBI;

/**
 * @author ashish.
 */

public interface FrameworkFinder {

  String findFramework();

  static FrameworkFinder build(RequestContext requestContext, DBI dbi) {
    return new FrameworkFinderImpl(dbi, requestContext);
  }

  static FrameworkFinder build(RequestContext requestContext) {
    return new FrameworkFinderImpl(DBICreator.getDbiForDefaultDS(), requestContext);
  }
}
