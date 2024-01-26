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

/**
 * Thrown when there is an inconsistency in the database, which should never
 * happen (a programmer error).
 * 
 * @author rickb
 */
public class InconsistentDBException extends NonRecoverableException {
    /**
     * Initialize this new instance with the given message and log the exception.
     *
     * @param msg a string describing the cause of this exception.
     */
    public InconsistentDBException(String msg) {
        super(msg);
    }
    
    /**
     * Initialize this new instance with the given message and the underlying
     * Java exception that caused this ShaTu exception and log the exception.
     *
     * @param msg a string describing the cause of this exception.
     * @param cause the Java exception that caused this ShaTu exception.
     */
    public InconsistentDBException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
