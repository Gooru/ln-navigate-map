package org.gooru.navigatemap.infra.utilities.preferredlanguagefinder;

import org.gooru.navigatemap.infra.data.NavigateProcessorContext;
import org.gooru.navigatemap.infra.utilities.jdbi.DBICreator;
import org.skife.jdbi.v2.DBI;

public interface PreferredLanguageFinder {

  Integer findPreferredLanguage();

  static PreferredLanguageFinder buildDefaultPreferredLanguageFinder(NavigateProcessorContext npc) {
    return new PreferredLanguageFinderImpl(DBICreator.getDbiForDefaultDS(), npc);
  }

  static PreferredLanguageFinder buildDefaultPreferredLanguageFinder(DBI dbi,
      NavigateProcessorContext npc) {
    return new PreferredLanguageFinderImpl(dbi, npc);
  }

}
