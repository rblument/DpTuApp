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
 * The scaffolding levels used by the tutor, which define the various
 * strategies and GUI actions used to present a case study to a student.
 *
 * @author Rickb
 */
public enum ScaffoldLevel {
    /**
     * NONE
     *  The student is in complete control and the tutor is passively
     *  listening and responding to student requests. If the student asks for
     *  a hint at this level, they are 'penalized' in the student model.
     */
     NONE("None"),

    /**
     * LOW
     *  Student controls a scenario and may ask for un-penalized hints.
     */
    LOW("Low"),

    /**
     * MEDIUM
     *  Student may ask clarifying contextual questions.
     */
    MEDIUM("Medium"),

    /**
     * HIGH    - .
     */
    HIGH("High"),

    /**
     * EXTREME - Student plays a passive role and must immediately execute
     *           the currently expected step. This level is used to demo
     *           the tutor in the first SeeOne scenario.
     */
    EXTREME("Extreme");
     

    /**
     * A GUI displayable string identifying this scaffold level.
     */
    private final String title;
    
    ScaffoldLevel(String title) {
        this.title = title;
    }
     
    /**
     * Return the scaffold level
     * 
     * @return aString for display in the GUI
     */
    public String title() {
        return title;
    }
}
