package org.gooru.navigatemap.processor.coursepath.state;

import org.gooru.navigatemap.processor.coursepath.flows.Workflow;
import org.gooru.navigatemap.processor.data.NavigateProcessorContext;
import org.gooru.navigatemap.processor.data.State;

/**
 * @author ashish on 3/3/17.
 */
final class LessonStartStateStimulusMapper
    implements StimulusMapper<NavigateProcessorContext, NavigateProcessorContext> {

    private NavigateProcessorContext stimulusContent;

    @Override
    public Stimulus<NavigateProcessorContext> applyStimulus(State state, Stimulus<NavigateProcessorContext> stimulus) {
        stimulusContent = stimulus.getStimulusContent();
        Workflow.submit(stimulusContent);
        return stimulus;
    }
}
