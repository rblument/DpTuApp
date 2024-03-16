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
 * The legal types of practice examples that a student can request.
 * 
 * @author rickb
 */
public enum ExampleType {
    /**
     * Represents a request to 
     * 
     * Data: An EncodeAsciiExample specifying the length of the string to be
     *       encoded.
     */
    INIT_FIRST_ROW("Initialize First Row"),
    
    INIT_FIRST_COL("Initialize First Column"),
    
    ENTER_CELL("Enter Cell Value");
    
    /**
     * The name used by the server to identify this request.
     */
    private final String requestName;
    
    /**
     * Initialize this enum object with the given title.
     * 
     * @param requestName 
     */
    ExampleType(String requestName) {
        this.requestName = requestName;
    }
    
    /**
     * Return the request name that is used by the server.
     * 
     * @return a String 
     */
    public String getRequestName() {
        return requestName;
    }
    
    /**
     * Return the request name that is used by the server
     * 
     * @return a String
     */
    @Override
    public String toString() {
        return requestName;
    }
}
