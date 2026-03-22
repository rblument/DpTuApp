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
package edu.regis.dptu.err;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Thrown when an illegal argument was passed to a method.
 *
 * @author rickb
 */
public class IllegalArgException extends DpTuException {
    private static final Logger log = LoggerFactory.getLogger(IllegalArgException.class);

    /**
     * Initialize this exception with the given message and log it.
     *
     * @param msg descriptive message about the illegal argument
     */
    public IllegalArgException(String msg) {
        super(msg);
        log.error("IllegalArgException thrown: {}", msg);
    }
}
