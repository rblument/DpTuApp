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
 * Thrown when an unexpected, non-recoverable error occurred associated with
 * processing an XML file in the XmlMgr.
 *
 * @author Rickb
 */
public class XmlException extends DpTuException {
    /**
     * Initialize this exception with the given message.
     * 
     * @param msg descriptive information about this exception
     */
    public XmlException(String msg) {
        super(msg);
    }
    
    /**
     * Initialize this exception with the given message and cause
     * 
     * @param msg descriptive information about this exception
     * @param cause the original cause of this exception
     */
    public XmlException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
