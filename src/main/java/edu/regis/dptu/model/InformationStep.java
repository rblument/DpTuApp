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
package edu.regis.dptu.model;

/**
 * A step subtype in which the student must acknowledge a message from the
 * tutor.
 * 
 * @author rickb
 */
public class InformationStep {
    /**
     * The message the student must acknowledge.
     */
    private String msg;
    
    /**
     * Public constructor for the InformationStep. Sets message to blank string
     */
    public InformationStep() {
        msg = "";
    }

    /**
     * Gets message to be presented to the student
     * @return String
     */
    public String getMsg() {
        return msg;
    }

    /**
     * Sets message to be presented to the student
     * @param msg (String)
     */
    public void setMsg(String msg) {
        this.msg = msg;
    }
}
