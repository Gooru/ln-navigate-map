package org.gooru.navigatemap.processor.coursepath.state;

import org.gooru.navigatemap.processor.coursepath.flows.Workflow;
import org.gooru.navigatemap.processor.coursepath.repositories.ContentRepositoryBuilder;
import org.gooru.navigatemap.processor.data.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author ashish on 3/3/17.
 */
final class StartStateStimulusMapper implements StimulusMapper<NavigateProcessorContext, NavigateProcessorContext> {

    private NavigateProcessorContext stimulusContent;
    private static final Logger LOGGER = LoggerFactory.getLogger(StartStateStimulusMapper.class);

    @Override
    public Stimulus<NavigateProcessorContext> applyStimulus(State state, Stimulus<NavigateProcessorContext> stimulus) {
        stimulusContent = stimulus.getStimulusContent();

        // User has explicitly asked to start, so they better have provided the currentItem and type.
        // Also if they are asking to be started from alternate path, they need to provide it in context
        if (stimulusContent.requestContext().getState() == State.Start) {
            stimulusContent.responseContext().setState(State.ContentServed);
        } else if (stimulusContent.requestContext().getState() == State.Continue) {
            // User asked to continue but we did not have any context from past
            courseStart();
        } else {
            LOGGER.warn("Invalid state for course start: '{}'", stimulusContent.requestContext().getState().getName());
        }
        return stimulus;
    }

    private void courseStart() {
        RequestContext requestContext = stimulusContent.requestContext();
        boolean userIsAnonymous = stimulusContent.navigateMessageContext().isUserAnonymous();
        ResponseContext responseContext = stimulusContent.responseContext();
        ContentAddress contentAddress =
            ContentRepositoryBuilder.buildContentFinderService().findFirstContentInCourse(requestContext.getCourseId());
        if (contentAddress != null) {
            if (contentAddress.getCollection() != null) {
                stimulusContent.setNextContextAddress(contentAddress);
                if (userIsAnonymous) {
                    responseContext.setContentAddress(contentAddress);
                    stimulusContent.responseContext().setState(State.ContentServed);
                } else {
                    courseStartForSignedInUser();
                }
            } else {
                responseContext.setState(State.Done);
            }
        } else {
            throw new IllegalStateException("Not able to locate first valid content in course");
        }

    }

    private void courseStartForSignedInUser() {
        Workflow.submit(stimulusContent);
    }
}
