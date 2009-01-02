/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.commons.scxml;

// Used for suggesting replacement in deprecation warnings
import org.apache.commons.scxml.semantics.ErrorConstants;

/**
 * An interface for reporting SCXML errors to the host environment,
 * containing the definition of commonly occuring errors while executing
 * SCXML documents.
 *
 */
public interface ErrorReporter {

    /**
     * Handler for reporting an error.
     *
     * @param errCode
     *            one of the ErrorReporter's constants
     * @param errDetail
     *            human readable description
     * @param errCtx
     *            typically an SCXML element which caused an error,
     *            may be accompanied by additional information
     */
    void onError(String errCode, String errDetail, Object errCtx);

    /**
     * Missing initial state for a composite state or for the scxml root.
     *
     * @see org.apache.commons.scxml.model.SCXML#getInitialState()
     * @see org.apache.commons.scxml.model.State#getInitial()
     *
     * @deprecated Use {@link ErrorConstants#NO_INITIAL} instead.
     */
    @Deprecated
    String NO_INITIAL = "NO_INITIAL";

    /**
     * An initial state for a composite state whose Transition does not.
     * Map to a descendant of the composite state.
     *
     * @deprecated Use {@link ErrorConstants#ILLEGAL_INITIAL} instead.
     */
    @Deprecated
    String ILLEGAL_INITIAL = "ILLEGAL_INITIAL";

    /**
     * Unknown action - unsupported executable content. List of supported.
     * actions: assign, cancel, elseif, else, if, log, send, var
     *
     * @deprecated Use {@link ErrorConstants#UNKNOWN_ACTION} instead.
     */
    @Deprecated
    String UNKNOWN_ACTION = "UNKNOWN_ACTION";

    /**
     * Illegal state machine configuration.
     * Either a parallel exists which does not have all its AND sub-states
     * active or there are multiple enabled OR states on the same level.
     *
     * @deprecated Use {@link ErrorConstants#ILLEGAL_CONFIG} instead.
     */
    @Deprecated
    String ILLEGAL_CONFIG = "ILLEGAL_CONFIG";

    /**
     * Non-deterministic situation has occured - there are more than
     * one enabled transitions in conflict.
     *
     * @deprecated No longer necessary, non determinism is solved based
     *             on state heirarchy and document order priorities.
     */
    @Deprecated
    String NON_DETERMINISTIC = "NON_DETERMINISTIC";

    /**
     * A variable referred to by assign name attribute is undefined.
     *
     * @deprecated Use {@link ErrorConstants#UNDEFINED_VARIABLE} instead.
     */
    @Deprecated
    String UNDEFINED_VARIABLE = "UNDEFINED_VARIABLE";

    /**
     * An expression language error.
     *
     * @deprecated Use {@link ErrorConstants#EXPRESSION_ERROR} instead.
     */
    @Deprecated
    String EXPRESSION_ERROR = "EXPRESSION_ERROR";

}
