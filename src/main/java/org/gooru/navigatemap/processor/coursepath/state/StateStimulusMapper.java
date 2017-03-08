package org.gooru.navigatemap.processor.coursepath.state;

import java.util.EnumMap;
import java.util.function.Supplier;

import org.gooru.navigatemap.processor.data.NavigateProcessorContext;
import org.gooru.navigatemap.processor.data.State;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author ashish on 3/3/17.
 */
public final class StateStimulusMapper {

    private StateStimulusMapper() {
        throw new AssertionError();
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(StateStimulusMapper.class);

    public static Stimulus<NavigateProcessorContext> stimulate(Stimulus<NavigateProcessorContext> snpc) {

        try {
            return lookupStimulusMapper(snpc.getCurrentState()).applyStimulus(snpc.getCurrentState(), snpc);
        } catch (Throwable throwable) {
            LOGGER.warn("Not able to run state machine", throwable);
            throw throwable;
        }
    }

    private static StimulusMapper<NavigateProcessorContext, NavigateProcessorContext> lookupStimulusMapper(
        State state) {
        return LOOKUP.get(state).get();
    }

    private static final EnumMap<State, Supplier<StimulusMapper<NavigateProcessorContext, NavigateProcessorContext>>>
        LOOKUP = new EnumMap<>(State.class);

    static {
        LOOKUP.put(State.Start, StartStateStimulusMapper::new);
        LOOKUP.put(State.LessonStartSuggested, LessonStartStateStimulusMapper::new);
        LOOKUP.put(State.LessonEndSuggested, LessonEndStateStimulusMapper::new);
        LOOKUP.put(State.ContentEndSuggested, ContentEndStateStimulusMapper::new);
        LOOKUP.put(State.ContentServed, ContentServedStateStimulusMapper::new);
        LOOKUP.put(State.Continue, StartStateStimulusMapper::new);
        LOOKUP.put(State.Done, () -> (state, stimulus) -> stimulus);

    }
}
