package org.gooru.navigatemap.processor.systemsuggestions;

import org.gooru.navigatemap.app.constants.HttpConstants;
import org.gooru.navigatemap.app.exceptions.HttpResponseWrapperException;

/**
 * @author ashish on 27/12/17.
 */
class ContextInformationVerifier {
    private final AddSystemSuggestionDao addSystemSuggestionDao;
    private final AddSystemSuggestionCommand command;

    ContextInformationVerifier(AddSystemSuggestionCommand command, AddSystemSuggestionDao addSystemSuggestionDao) {
        this.command = command;
        this.addSystemSuggestionDao = addSystemSuggestionDao;
    }

    void validateContextInformation() {
        if (command.getCtxClassId() == null) {
            validateContextInformationForCourse();
        } else {
            validateContextInformationForClass();
        }
    }

    private void validateContextInformationForClass() {
        if (!addSystemSuggestionDao.validateContextInformationForClassRootedAtCollection(command.getBean())) {
            throw new HttpResponseWrapperException(HttpConstants.HttpStatus.BAD_REQUEST,
                "Incorrect context information");
        }
    }

    private void validateContextInformationForCourse() {
        if (!addSystemSuggestionDao.validateContextInformationForCourseRootedAtCollection(command.getBean())) {
            throw new HttpResponseWrapperException(HttpConstants.HttpStatus.BAD_REQUEST,
                "Incorrect context information");
        }
    }

}
