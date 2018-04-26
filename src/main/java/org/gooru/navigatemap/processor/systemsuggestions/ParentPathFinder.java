package org.gooru.navigatemap.processor.systemsuggestions;

import java.util.Objects;

import org.gooru.navigatemap.app.constants.HttpConstants;
import org.gooru.navigatemap.app.exceptions.HttpResponseWrapperException;

/**
 * @author ashish on 27/12/17.
 */
class ParentPathFinder {

    private final AddSystemSuggestionDao addSystemSuggestionDao;
    private final AddSystemSuggestionCommand command;

    ParentPathFinder(AddSystemSuggestionCommand command, AddSystemSuggestionDao addSystemSuggestionDao) {
        this.addSystemSuggestionDao = addSystemSuggestionDao;
        this.command = command;
    }

    Long findParentPath() {
        Long parentPathId;
        if (command.getCtxClassId() == null) {
            parentPathId = findParentPathForCourse();
        } else {
            parentPathId = findParentPathForClass();
        }
        validateParentPathId(parentPathId);
        return parentPathId;
    }

    private void validateParentPathId(Long parentPathId) {
        if (Objects.equals(command.getPathId(), parentPathId)) {
            return;
        }
        throw new HttpResponseWrapperException(HttpConstants.HttpStatus.BAD_REQUEST, "Invalid path id");
    }

    private Long findParentPathForClass() {
        if (command.getCtxCollectionId() == null) {
            return addSystemSuggestionDao.findParentPathForClassRootedAtLesson(command.getBean());
        } else {
            return addSystemSuggestionDao.findParentPathForClassRootedAtCollection(command.getBean());
        }
    }

    private Long findParentPathForCourse() {
        if (command.getCtxCollectionId() == null) {
            return addSystemSuggestionDao.findParentPathForCourseRootedAtLesson(command.getBean());
        } else {
            return addSystemSuggestionDao.findParentPathForCourseRootedAtCollection(command.getBean());
        }
    }

}
