package org.gooru.navigatemap.processor.coursepath.flows;

import org.gooru.navigatemap.responses.ExecutionResult;

/**
 * @author ashish on 6/3/17.
 */
public interface Flow {

    <T> ExecutionResult<T> apply(ExecutionResult<T> input);
}
