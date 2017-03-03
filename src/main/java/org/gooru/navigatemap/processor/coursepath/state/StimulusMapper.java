package org.gooru.navigatemap.processor.coursepath.state;

import org.gooru.navigatemap.processor.data.State;

/**
 * @author ashish on 3/3/17.
 */
public interface StimulusMapper<T extends Stateful, U extends Stateful> {
    Stimulus<U> applyStimulus(State state, Stimulus<T> stimulus);
}
