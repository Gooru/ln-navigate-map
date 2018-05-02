package org.gooru.navigatemap.processor.systemsuggestions;

import org.skife.jdbi.v2.DBI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author ashish on 24/11/17.
 */
class AddSystemSuggestionService {

    private final AddSystemSuggestionDao addSystemSuggestionDao;
    private static final Logger LOGGER = LoggerFactory.getLogger(AddSystemSuggestionService.class);
    private AddSystemSuggestionCommand command;

    AddSystemSuggestionService(DBI dbi) {

        this.addSystemSuggestionDao = dbi.onDemand(AddSystemSuggestionDao.class);
    }

    Long addSystemSuggestion(AddSystemSuggestionCommand command) {
        this.command = command;
        Long result;

        new ContextInformationVerifier(command, addSystemSuggestionDao).validateContextInformation();

        Long userHasSuggestionAsPath =
            new UserAlreadyHasSuggestionVerifier(command, addSystemSuggestionDao).findUserPathForCurrentSuggestion();

        if (userHasSuggestionAsPath == null) {
            result = addSystemSuggestionDao.addSystemSuggestion(command.getBean());
        } else {
            LOGGER.info("User already has specified suggestion");
            result = userHasSuggestionAsPath;
        }
        return result;
    }

}
