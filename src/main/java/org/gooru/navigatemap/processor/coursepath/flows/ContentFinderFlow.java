package org.gooru.navigatemap.processor.coursepath.flows;

import java.util.Objects;

import org.gooru.navigatemap.app.components.AppConfiguration;
import org.gooru.navigatemap.processor.coursepath.repositories.ContentRepositoryBuilder;
import org.gooru.navigatemap.processor.data.ContentAddress;
import org.gooru.navigatemap.processor.data.NavigateProcessorContext;
import org.gooru.navigatemap.processor.data.ResponseContext;
import org.gooru.navigatemap.processor.data.State;
import org.gooru.navigatemap.responses.ExecutionResult;

/**
 * @author ashish on 6/3/17.
 */
final class ContentFinderFlow implements Flow<NavigateProcessorContext> {

    private NavigateProcessorContext npc;
    private ExecutionResult<NavigateProcessorContext> executionResult;

    @Override
    public ExecutionResult<NavigateProcessorContext> apply(ExecutionResult<NavigateProcessorContext> input) {

        this.executionResult = input;

        if (this.executionResult.isCompleted()) {
            return executionResult;
        }
        npc = input.result();

        if (npc.requestContext().getState() == State.Continue) {
            // We should have the content here already as this may be start of course
            npc.responseContext().setContentAddress(npc.getNextContentAddress());
            input.setStatus(ExecutionResult.ExecutionStatus.SUCCESSFUL);
            npc.responseContext().setState(State.ContentServed);
        }

        if (npc.requestContext().getState() == State.ContentServed && npc.requestContext().getPathId() == null
            && !AppConfiguration.getInstance().suggestionsTurnedOn()) {
            fetchNextItemFromCUL();
        }
        return executionResult;
    }

    private void fetchNextItemFromCUL() {
        ResponseContext responseContext = npc.responseContext();
        ContentAddress contentAddress =
            ContentRepositoryBuilder.buildContentFinderService().findNextContentFromCUL(getCurrentContentAddress());
        if (contentAddress != null) {
            if (contentAddress.getCollection() != null) {
                npc.setNextContextAddress(contentAddress);
                if (shouldModifyMainResponseContext()) {
                    responseContext.setContentAddress(contentAddress);
                    markAsDone();
                }
            } else {
                responseContext.setState(State.Done);
            }
        } else {
            throw new IllegalStateException("Not able to locate valid content in course");
        }

    }

    private void markAsDone() {
        executionResult.setStatus(ExecutionResult.ExecutionStatus.SUCCESSFUL);
        npc.responseContext().setState(State.ContentServed);
    }

    private boolean shouldModifyMainResponseContext() {
        return npc.navigateMessageContext().isUserAnonymous() || !AppConfiguration.getInstance().suggestionsTurnedOn();
    }

    private ContentAddress getCurrentContentAddress() {
        ContentAddress result = new ContentAddress();
        result.setCollectionSubtype(npc.requestContext().getCurrentItemSubtype());
        result.setCollectionType(npc.requestContext().getCurrentItemType());
        result.setCollection(Objects.toString(npc.requestContext().getCurrentItemId(), null));
        result.setCourse(npc.requestContext().getCourseId().toString());
        result.setUnit(Objects.toString(npc.requestContext().getUnitId(), null));
        result.setLesson(Objects.toString(npc.requestContext().getLessonId(), null));
        return result;
    }

}
