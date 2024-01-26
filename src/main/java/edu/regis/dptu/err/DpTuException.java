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
 * Root of all checked DpTu application exceptions.
 * 
 * Logging should be done in subclasses since some exceptions might be
 * appropriately handled by the user or in code.
 * 
 * @author Rickb
 */
public abstract class DpTuException extends Exception {
    /**
     * Initialize this new instance with the given message.
     *
     * @param msg a string describing the cause of this exception.
     */
    public DpTuException(String msg) {
	super(msg);  
    }

    /**
     * Initialize this new instance with the given message and the underlying
     * Java exception that caused this ShaTu exception.
     *
     * @param msg a string describing the cause of this exception.
     * @param cause the Java exception that caused this ShaTu exception.
     */
    public DpTuException(String msg, Throwable cause) {
	super(msg, cause);
    }
}

