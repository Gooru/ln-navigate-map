package org.gooru.navigatemap.processor.teachersuggestions;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.gooru.navigatemap.processor.utilities.CollectionUtils;
import org.skife.jdbi.v2.DBI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author ashish on 24/11/17.
 */
class AddTeacherSuggestionsService {

    private final AddTeacherSuggestionsDao addTeacherSuggestionsDao;
    private static final Logger LOGGER = LoggerFactory.getLogger(AddTeacherSuggestionsService.class);
    private AddTeacherSuggestionsCommand command;

    AddTeacherSuggestionsService(DBI dbi) {

        this.addTeacherSuggestionsDao = dbi.onDemand(AddTeacherSuggestionsDao.class);
    }

    void addTeacherSuggestion(AddTeacherSuggestionsCommand command) {
        this.command = command;
        List<UUID> usersNotHavingSpecifiedSuggestions = findUsersNotHavingSpecifiedSuggestion();
        if (usersNotHavingSpecifiedSuggestions != null && !usersNotHavingSpecifiedSuggestions.isEmpty()) {
            addTeacherSuggestionsDao.addTeacherSuggestion(command.getBean(), usersNotHavingSpecifiedSuggestions);
        } else {
            LOGGER.info("All specified users already have specified suggestion");
        }
    }

    private List<UUID> findUsersNotHavingSpecifiedSuggestion() {
        List<UUID> usersHavingSpecifiedSuggestions = findUsersHavingSpecifiedSuggestionForClass();
        return findUsersNotInAlreadyHavingSuggestionList(usersHavingSpecifiedSuggestions);
    }

    private List<UUID> findUsersNotInAlreadyHavingSuggestionList(List<UUID> usersHavingSpecifiedSuggestions) {
        List<UUID> result = new ArrayList<>(command.getCtxUserIds());
        result.removeAll(usersHavingSpecifiedSuggestions);
        return result;
    }

    private List<UUID> findUsersHavingSpecifiedSuggestionForClass() {
        if (command.getCtxCollectionId() == null) {
            return addTeacherSuggestionsDao.findUsersHavingSpecifiedSuggestionForClassRootedAtLesson(command.getBean(),
                CollectionUtils.convertFromListUUIDToSqlArrayOfUUID(command.getCtxUserIds()));
        } else {
            return addTeacherSuggestionsDao
                .findUsersHavingSpecifiedSuggestionForClassRootedAtCollection(command.getBean(),
                    CollectionUtils.convertFromListUUIDToSqlArrayOfUUID(command.getCtxUserIds()));
        }
    }
}
