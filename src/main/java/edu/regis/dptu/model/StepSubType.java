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
 * The legal step types
 * 
 * This class is essentially kludge since I couldn't get subclassing of
 * Gson/Json subtypes to work. So, I now manually handle it. 
 * 
 * @author rickb
 */
public enum StepSubType {
    /**
     * The user must acknowledge a message (i.e., perhaps via a pop-up dialog)
     */
    INFO_MESSAGE("Information Message"),
    
    /**
    * A step in which the student must perform some GUI action, which is 
    * typically performed outside of actual tutoring for purposes of learning 
    * the GUI, such as learning to request a hint.
     */
    GUI_ACTION("GUI Action"),
    
    /**
     * Enter a value in a dynamic programming table cell
     */
    COMPLETE_CELL("Complete Cell"),
    
    /**
     * The initial default value in a NewExampleRequest
     */
    DEFAULT("Unknown");
    
    /**
     * The name used by the server to identify this request.
     */
    private final String subType;
    
    /**
     * Initialize this enum object with the given title.
     * 
     * @param subType 
     */
    StepSubType(String subType) {
        this.subType = subType;
    }
    
    /**
     * Return the request name that is used by the server.
     * 
     * @return a String 
     */
    public String getSubType() {
        return subType;
    }
    
    /**
     * Return the subType name that is used by the server
     * 
     * @return a String
     */
    @Override
    public String toString() {
        return subType;
    }
}
