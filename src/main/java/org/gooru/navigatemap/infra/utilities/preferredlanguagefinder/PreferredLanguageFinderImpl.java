package org.gooru.navigatemap.infra.utilities.preferredlanguagefinder;

import org.gooru.navigatemap.app.components.AppConfiguration;
import org.gooru.navigatemap.infra.data.NavigateProcessorContext;
import org.skife.jdbi.v2.DBI;

/**
 * @author ashish.
 */

class PreferredLanguageFinderImpl implements PreferredLanguageFinder {

  private final NavigateProcessorContext npc;
  private final DBI dbi;
  private PreferredLanguageFinderDao dao = null;

  PreferredLanguageFinderImpl(DBI dbi, NavigateProcessorContext npc) {
    this.dbi = dbi;
    this.npc = npc;
  }

  @Override
  public Integer findPreferredLanguage() {
    Integer primaryLanguageId = fetchPrimaryLanguage();

    if (primaryLanguageId == null) {
      return fetchDefaultPrimaryLanguageId();
    } else {
      return primaryLanguageId;
    }

  }

  private Integer fetchDefaultPrimaryLanguageId() {
    return AppConfiguration.getInstance().getDefaultPrimaryLanguageId();
  }

  private Integer fetchPrimaryLanguage() {
    if (npc.requestContext().isContextIL()) {
      // For IL we lookup what we have in user session
      return npc.navigateMessageContext().getPrimaryLanguage();
    } else {
      // For class we need to fetch from class
      return fetchDao()
          .fetchPrimaryLanguageIdFromClass(npc.requestContext().getClassId());
    }
  }

  private PreferredLanguageFinderDao fetchDao() {
    if (dao == null) {
      dao = dbi.onDemand(PreferredLanguageFinderDao.class);
    }
    return dao;
  }

}
