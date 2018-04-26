package org.gooru.navigatemap.processor.systemsuggestions;

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
        if (command.getCtxClassId() == null) {
            return userAlreadyHasSuggestionForCourse();
        } else {
            return userAlreadyHasSuggestionForClass();
        }
    }

    private Long userAlreadyHasSuggestionForClass() {
        if (command.getCtxCollectionId() == null) {
            return userAlreadyHasSuggestionForClassRootedAtLesson();
        } else {
            return userAlreadyHasSuggestionForClassRootedAtCollection();
        }
    }

    private Long userAlreadyHasSuggestionForCourse() {
        if (command.getCtxCollectionId() == null) {
            return userAlreadyHasSuggestionForCourseRootedAtLesson();
        } else {
            return userAlreadyHasSuggestionForCourseRootedAtCollection();
        }
    }

    private Long userAlreadyHasSuggestionForClassRootedAtCollection() {
        return addSystemSuggestionDao
            .findUserPathHavingSpecifiedSuggestionForClassRootedAtCollection(command.getBean());
    }

    private Long userAlreadyHasSuggestionForClassRootedAtLesson() {
        return addSystemSuggestionDao.findUserPathHavingSpecifiedSuggestionForClassRootedAtLesson(command.getBean());
    }

    private Long userAlreadyHasSuggestionForCourseRootedAtCollection() {
        return addSystemSuggestionDao
            .findUserPathHavingSpecifiedSuggestionForCourseRootedAtCollection(command.getBean());
    }

    private Long userAlreadyHasSuggestionForCourseRootedAtLesson() {
        return addSystemSuggestionDao.findUserPathHavingSpecifiedSuggestionForCourseRootedAtLesson(command.getBean());
    }

}
