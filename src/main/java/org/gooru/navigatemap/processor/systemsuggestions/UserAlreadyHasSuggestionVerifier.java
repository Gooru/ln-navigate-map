package org.gooru.navigatemap.processor.systemsuggestions;

import java.util.Objects;

import org.gooru.navigatemap.app.constants.HttpConstants;
import org.gooru.navigatemap.app.exceptions.HttpResponseWrapperException;
import org.gooru.navigatemap.infra.data.AlternatePath;
import org.gooru.navigatemap.infra.data.SuggestionType;

/**
 * @author ashish on 27/12/17.
 */
class UserAlreadyHasSuggestionVerifier {

    private final AddSystemSuggestionDao addSystemSuggestionDao;
    private final AddSystemSuggestionCommand command;

    UserAlreadyHasSuggestionVerifier(AddSystemSuggestionCommand command,
        AddSystemSuggestionDao addSystemSuggestionDao) {
        this.command = command;
        this.addSystemSuggestionDao = addSystemSuggestionDao;
    }

    Long findUserPathForCurrentSuggestion() {
        if (command.getSuggestionType() == SuggestionType.Route0) {
            return userAlreadyHasSuggestionForRoute0();
        } else if (command.getCtxClassId() == null) {
            return userAlreadyHasSuggestionForCourse();
        } else {
            return userAlreadyHasSuggestionForClass();
        }
    }

    private Long userAlreadyHasSuggestionForRoute0() {
        // If there is route0 with same suggested_content_id then we return as successful else we throw bad request
        AlternatePath userPathForRoute0;
        if (command.getCtxClassId() != null) {
            userPathForRoute0 = addSystemSuggestionDao.findUserPathForRoute0WithClass(command.getBean());
        } else {
            userPathForRoute0 = addSystemSuggestionDao.findUserPathForRoute0WithoutClass(command.getBean());
        }

        if (userPathForRoute0 != null) {
            if (Objects.equals(userPathForRoute0.getSuggestedContentId().toString(),
                command.getSuggestedContentId().toString())) {
                // we have the same suggestion for route0 already applied, fake success
                return userPathForRoute0.getId();
            } else {
                throw new HttpResponseWrapperException(HttpConstants.HttpStatus.BAD_REQUEST,
                    "Route0 already exists for specified context");
            }
        }
        return null;
    }

    private Long userAlreadyHasSuggestionForClass() {
        // Note that we do not suggest and provide alternate path at lesson level
        return addSystemSuggestionDao
            .findUserPathHavingSpecifiedSuggestionForClassRootedAtCollection(command.getBean());
    }

    private Long userAlreadyHasSuggestionForCourse() {
        // Note that we do not suggest and provide alternate path at lesson level
        return addSystemSuggestionDao
            .findUserPathHavingSpecifiedSuggestionForCourseRootedAtCollection(command.getBean());
    }

}
