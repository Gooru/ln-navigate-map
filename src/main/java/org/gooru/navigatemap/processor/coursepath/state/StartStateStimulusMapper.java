package org.gooru.navigatemap.processor.coursepath.state;

import org.gooru.navigatemap.processor.coursepath.flows.Workflow;
import org.gooru.navigatemap.processor.data.NavigateProcessorContext;
import org.gooru.navigatemap.processor.data.State;
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
        Workflow.submit(stimulusContent);
        return stimulus;
    }
}
