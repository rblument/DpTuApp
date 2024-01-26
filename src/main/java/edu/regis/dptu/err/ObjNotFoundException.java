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
 * Thrown when an object isn't found (context specific).
 *
 * @author rickb
 */
public class ObjNotFoundException extends DpTuException {
    /**
     * Initialize this exception with the given message.
     * 
     * @param msg message identifying the object that wasn't found.
     */
    public ObjNotFoundException(String msg) {
        super(msg);
    }
    
    /**
     * Initialize this exception with the given message and an internal root
     * cause of the exception.
     * 
     * @param msg message identifying the object that wasn't found.
     * @param cause the internal exception causing this exception
     */
    public ObjNotFoundException(String msg, Throwable cause) {
	super(msg, cause);
    }
}
