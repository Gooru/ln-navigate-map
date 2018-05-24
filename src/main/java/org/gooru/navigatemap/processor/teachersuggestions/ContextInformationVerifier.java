package org.gooru.navigatemap.processor.teachersuggestions;

import org.gooru.navigatemap.app.constants.HttpConstants;
import org.gooru.navigatemap.app.exceptions.HttpResponseWrapperException;

/**
 * @author ashish on 27/12/17.
 */
class ContextInformationVerifier {
    private final AddTeacherSuggestionsDao addTeacherSuggestionsDao;
    private final AddTeacherSuggestionsCommand command;

    ContextInformationVerifier(AddTeacherSuggestionsCommand command,
        AddTeacherSuggestionsDao addTeacherSuggestionsDao) {
        this.command = command;
        this.addTeacherSuggestionsDao = addTeacherSuggestionsDao;
    }

    void validateContextInformation() {
        // We do not support suggestions at lesson level any more
        if (!addTeacherSuggestionsDao.validateContextInformationForClassRootedAtCollection(command.getBean())) {
            throw new HttpResponseWrapperException(HttpConstants.HttpStatus.BAD_REQUEST,
                "Incorrect context information");
        }
    }
}
