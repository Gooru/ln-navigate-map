package org.gooru.navigatemap.processor.coursepath.state;

import org.gooru.navigatemap.processor.data.NavigateProcessorContext;
import org.gooru.navigatemap.processor.data.State;

/**
 * @author ashish on 3/3/17.
 */
final class ContentEndStateStimulusMapper
    implements StimulusMapper<NavigateProcessorContext, NavigateProcessorContext> {

    @Override
    public Stimulus<NavigateProcessorContext> applyStimulus(State state, Stimulus<NavigateProcessorContext> stimulus) {
        return null;
    }
}
