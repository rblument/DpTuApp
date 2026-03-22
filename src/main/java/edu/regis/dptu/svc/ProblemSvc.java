/*
 * DPTu: Dynamic Programming Tutor
 *
 *  (C) Johanna & Richard Blumenthal, All rights reserved
 *
 *  Unauthorized use, duplication or distribution without the authors'
 *  permission is strictly prohibited.
 *
 *  Unless required by applicable law or agreed to in writing, this
 *  software is distributed on an "AS IS" basis without warranties
 *  or conditions of any kind, either expressed or implied.
 */
package edu.regis.dptu.svc;

import edu.regis.dptu.err.NonRecoverableException;
import edu.regis.dptu.err.ObjNotFoundException;
import edu.regis.dptu.model.Problem;
import edu.regis.dptu.model.ProblemKind;

/**
 * Specifies the API for Problem life-cycle maintenance (database persistence).
 *
 * @author rickb
 */
@SuppressWarnings("Logging")
public interface ProblemSvc {
    /**
     * Locate and return the Problem with the given kind.
     *
     * @param kind the problem kind of the problem
     * @return The Problem of type ProblemKind
     * @exception ObjNotFoundException No Problem with the given kind exists.
     * @throws NonRecoverableException also see getCause().getErrorCode()..
     */
    Problem retrieveByKind(ProblemKind kind) throws ObjNotFoundException, NonRecoverableException;

    /**
     * Locate and return the Problem with the given id.
     *
     * <p>The method is similar to {@link #retrieveDigest()} except it returns the entire course
     * content.
     *
     * @param problemId integer key of the Problem to load.
     * @return The Problem with the given id.
     * @exception ObjNotFoundException No Problem with the given id exists.
     * @throws NonRecoverableException also see getCause().getErrorCode()..
     */
    Problem retrieve(int problemId) throws ObjNotFoundException, NonRecoverableException;
}
