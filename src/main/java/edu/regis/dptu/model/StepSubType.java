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
     * Initialized the entire first row.
     */
    COMPLETE_FIRST_ROW("Complete First Row"),
    
    /**
     * Initialized the entire first col.
     */
    COMPLETE_FIRST_COL("Complete First Col"),
    
    /**
     * A type of dynamic programming cell step in which the cell value defaults
     * to zero because of it's position in row or column index -1
     */
    DEFAULT_ZERO("Cell Step: Default Zero"),
    
    /**
     * A type of dynamic programming cell step in which the cell value is the
     * upper-left diagonal value due to a match in the String characters
     */
    INCREASE_DIAGONAL("Cell Step: Increase Diagonal Value + 1"),
    
    /**
     * A type of dynamic programming cell step in which the cell value is equal
     * to the cell located to the left (column position - 1) because of no String
     * match, and the left cell is higher value than the upper cell
     */
    USE_LEFT("Cell Step: Use value of cell to the left."),
    
    /**
     * A type of dynamic programming cell step in which the cell value is equal
     * to the cell located above (row position - 1) because of no String
     * match, and the upper is higher value than the cell to the left
     */
    USE_UPPER("Cell Step: Use value of cell above."),
    
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