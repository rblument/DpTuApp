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
 * Thrown when an illegal argument was passed to a method.
 * 
 * @author rickb
 */
public class IllegalArgException extends DpTuException {
    /**
     * Initialize this exception with the given message.
     * @param msg
     */
    public IllegalArgException(String msg) {
        super(msg);
    }
}
