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

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * An unexpected exception that the user cannot recovered from, which is logged.
 * 
 * Typically, this exception is caused by a low-level Java exception, which
 * case the getCause() method of the exception can be used to see the likely
 * cause. In the case of actual MySQL database exceptions, the getErrorCode() 
 * method often contains the database issue.
 * 
 * @author rickb
 */
public class NonRecoverableException extends DpTuException {
    /**
     * Initialize this new instance with the given message and log the exception.
     *
     * @param msg a string describing the cause of this exception.
     */
    public NonRecoverableException(String msg) {
	super(msg);

        Logger.getLogger(NonRecoverableException.class.getName())
            .log(Level.SEVERE, "ShaTuException: {0}", msg);
        
      }

    /**
     * Initialize this new instance with the given message and the underlying
     * Java exception that caused this ShaTu exception and log the exception.
     *
     * @param msg a string describing the cause of this exception.
     * @param cause the Java exception that caused this ShaTu exception.
     */
    public NonRecoverableException(String msg, Throwable cause) {
	super(msg, cause);
         
        Logger.getLogger(NonRecoverableException.class.getName())
                .log(Level.SEVERE, msg, cause);
    }
}

