package org.gooru.navigatemap.processor.coursepath.repositories.dao;

import java.util.List;
import java.util.UUID;

import org.gooru.navigatemap.processor.coursepath.repositories.mappers.SuggestionCardMapper;
import org.gooru.navigatemap.processor.data.SuggestionCard;
import org.gooru.navigatemap.processor.utilities.jdbi.PGArray;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.customizers.Mapper;

/**
 * @author ashish on 7/5/17.
 */
public interface SuggestionCardDao {
    @Mapper(SuggestionCardMapper.class)
    @SqlQuery("select id, title, format, subformat, thumbnail, metadata, taxonomy from collection where id = any"
                  + "(:collections)")
    List<SuggestionCard> createSuggestionsCardForCollections(@Bind("collections") PGArray<UUID> collections);

    @Mapper(SuggestionCardMapper.class)
    @SqlQuery("select id, title, 'resource' as format, content_subformat as subformat, thumbnail, metadata, taxonomy "
                  + "from original_resource where id = any(:resources)")
    List<SuggestionCard> createSuggestionsCardForResources(@Bind("resources") PGArray<UUID> resources);
}
