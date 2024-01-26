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
 * The gestures (actions) that can be performed by a student in the GUI. 
 * 
 * @author rickb
 */
public enum GuiGesture {
    /**
     * A gesture in which the student requests a hint in the GUI.
     */
    REQUEST_HINT("Request Hint"),
    
    /**
     * Represent an error state read when creating a course.
     */
    NO_OP("No Op");
    
    /**
     * A pretty print name for this GUI action.
     */
    private final String name;
    
    /**
     * Initialize this gesture with the given pretty print name
     * 
     * @param name a String that can be displayed to the user.
     */
    GuiGesture(String name) {
        this. name = name;
    }
    
    /**
     * Return the name of this gesture.
     * 
     * @return a pretty print String
     */
    public String getName() {
        return name;
    }
    
    /**
     * Return the name that is used by the server
     * 
     * @return a String
     */
    @Override
    public String toString() {
        return name;
    }
}

