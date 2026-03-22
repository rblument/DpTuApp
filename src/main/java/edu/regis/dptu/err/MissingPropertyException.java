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
 * Thrown when a property cannot be found in a "*.properties" file.
 *
 * <p>ToDo: Should this extend NonRecoverableException (vs. are there appropriate defaults when a
 * property is missing)?
 *
 * @author rickb
 */
public class MissingPropertyException extends DpTuException {
    private static final Logger log = LoggerFactory.getLogger(MissingPropertyException.class);

    /**
     * Construct this new instance with the given message.
     *
     * @param property the property key (e.g. "edu.regis.dptu.DebugLevel")
     */
    public MissingPropertyException(String property) {
        super("Missing property " + property);
        log.error("MissingPropertyException thrown: missing property '{}'", property);
    }

    /**
     * Initialize this new instance with the given message and java exception that initially caused
     * this exception.
     *
     * @param property the property key (e.g. "edu.regis.dptu.DebugLevel")
     * @param cause the Java exception that caused this exception.
     */
    public MissingPropertyException(String property, Throwable cause) {
        super("Missing property " + property, cause);
        log.error(
                "MissingPropertyException thrown: missing property '{}'. Cause: {}",
                property,
                cause.toString(),
                cause);
    }
}
