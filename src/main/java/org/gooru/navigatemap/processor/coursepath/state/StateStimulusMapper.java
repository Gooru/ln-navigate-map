package org.gooru.navigatemap.processor.coursepath.state;

import java.util.EnumMap;

import org.gooru.navigatemap.processor.data.NavigateProcessorContext;
import org.gooru.navigatemap.processor.data.State;

/**
 * @author ashish on 3/3/17.
 */
public final class StateStimulusMapper {

    private StateStimulusMapper() {
        throw new AssertionError();
    }

    public static Stimulus<NavigateProcessorContext> stimulate(Stimulus<NavigateProcessorContext> snpc) {

        return lookupStimulusMapper(snpc.getCurrentState()).applyStimulus(snpc.getCurrentState(), snpc);
    }

    private static StimulusMapper<NavigateProcessorContext, NavigateProcessorContext> lookupStimulusMapper(
        State state) {
        return LOOKUP.get(state);
    }

    private static final EnumMap<State, StimulusMapper<NavigateProcessorContext, NavigateProcessorContext>> LOOKUP =
        new EnumMap<>(State.class);

    static {
        LOOKUP.put(State.Started, null);
        LOOKUP.put(State.ContentStartSuggested, null);
        LOOKUP.put(State.LessonStartSuggested, null);
        LOOKUP.put(State.LessonEndSuggested, null);
        LOOKUP.put(State.ContentEndSuggested, null);
        LOOKUP.put(State.ContentServed, null);
        LOOKUP.put(State.Done, null);
    }
}
