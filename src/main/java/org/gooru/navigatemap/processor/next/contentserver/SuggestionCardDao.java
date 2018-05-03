package org.gooru.navigatemap.processor.next.contentserver;

import java.util.List;
import java.util.UUID;

import org.gooru.navigatemap.infra.data.CollectionRQCount;
import org.gooru.navigatemap.infra.data.SuggestionCard;
import org.gooru.navigatemap.infra.utilities.jdbi.PGArray;
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
    List<SuggestionCard> createSuggestionsCardForCollectionsWithoutRQCount(
        @Bind("collections") PGArray<UUID> collections);

    @Mapper(CollectionRQCountMapper.class)
    @SqlQuery("select collection_id, sum(case when content_format = 'resource' then 1 else 0 end) as resource_count, "
                  + "sum(case when content_format = 'question' then 1 else 0 end) as question_count from content "
                  + "where collection_id = any(:collections) and is_deleted = false group by collection_id;")
    List<CollectionRQCount> fetchRQCountForCollections(@Bind("collections") PGArray<UUID> collections);
}
