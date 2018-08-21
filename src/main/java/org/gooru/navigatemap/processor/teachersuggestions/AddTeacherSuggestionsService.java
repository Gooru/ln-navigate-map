package org.gooru.navigatemap.processor.teachersuggestions;

import org.gooru.navigatemap.infra.utilities.CollectionUtils;
import org.skife.jdbi.v2.DBI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

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

    Map<String, Integer> addTeacherSuggestion(AddTeacherSuggestionsCommand command) {
        this.command = command;
        new ContextInformationVerifier(command, addTeacherSuggestionsDao).validateContextInformation();
        List<UUID> usersNotHavingSpecifiedSuggestions = findUsersNotHavingSpecifiedSuggestion();
        if (usersNotHavingSpecifiedSuggestions != null && !usersNotHavingSpecifiedSuggestions.isEmpty()) {
            int[] result =
                addTeacherSuggestionsDao.addTeacherSuggestion(command.getBean(), usersNotHavingSpecifiedSuggestions);
            return transformResult(result, usersNotHavingSpecifiedSuggestions);
        } else {
            LOGGER.info("All specified users already have specified suggestion");
            return Collections.emptyMap();
        }
    }

    private Map<String, Integer> transformResult(int[] input, List<UUID> usersNotHavingSpecifiedSuggestions) {
        if (input == null || input.length == 0 || usersNotHavingSpecifiedSuggestions == null ||
                usersNotHavingSpecifiedSuggestions.size() == 0) {
            return Collections.emptyMap();
        }
        if (input.length != usersNotHavingSpecifiedSuggestions.size()) {
            LOGGER.warn("Size of users list does not match returned id. May be race condition. Won't return anything");
            return Collections.emptyMap();
        }
        Map<String, Integer> result = new HashMap<>(input.length);
        for (int i = 0; i < input.length; i++) {
            result.put(usersNotHavingSpecifiedSuggestions.get(i).toString(), input[i]);
        }
        return result;
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
        return addTeacherSuggestionsDao.findUsersHavingSpecifiedSuggestionForClassRootedAtCollection(command.getBean(),
            CollectionUtils.convertFromListUUIDToSqlArrayOfUUID(command.getCtxUserIds()));
    }
}
