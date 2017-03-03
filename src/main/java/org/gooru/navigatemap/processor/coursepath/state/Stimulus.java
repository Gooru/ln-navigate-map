package org.gooru.navigatemap.processor.coursepath.state;

import org.gooru.navigatemap.processor.data.State;

/**
 * @author ashish on 3/3/17.
 */
public class Stimulus<T extends Stateful> {
    private final T content;

    public Stimulus(T typed) {
        this.content = typed;
    }

    public State getCurrentState() {
        return this.content.getCurrentState();
    }

    public T getStimulusContent() {
        return this.content;
    }
}
