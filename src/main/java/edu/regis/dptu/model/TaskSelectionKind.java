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
 * The approaches used to select the next task presented to the student, which
 * follows Van Lehn's outer loop task selections.
 * 
 * @author rickb
 */
public enum TaskSelectionKind {
    /**
     * Via a gesture in the user interface, the student selects the next
     * task. This is used in the ShaTu tutor to allow the student to practice
     * a task at any time.
     */
    STUDENT_CHOICE("Student Choice"),
    
    /**
     * Course units and/or the tasks within them are assigned in a fixed sequence.
     */
    FIXED_SEQUENCE("Fixed Sequence"),
    
    /**
     * Units comprise a sequence of difficulty levels and tasks are selected
     * from the current unit until the student has mastered them.
     */
    MASTERY_LEARNING("Mastery Learning"),
    
    /**
     * Task selection is based on a student's mastery of a knowledge component.
     */
    MICROADAPTATION("Microadaptation"),
    
    /**
     * A default value used in a Knowledge Component indicating that some
     * approach other than macroadaptation is used to select task addressing
     * this component (i.e., a fixed sequence within a unit).
     * 
     */
    OTHER("Other"),
    
    /**
     * Used to signal an unknown selection kind was read from the DB.
     */
    ERROR("Error");
    
    /**
     * A title that can be displayed to the user.
     */
    private final String title;
    
    /**
     * Initialize this enum value with the given title.
     * 
     * @param title a String that can be displayed to the user.
     */
    TaskSelectionKind(String title) {
        this.title = title;
    }
    
    public String getTitle() {
        return title;
    }
    
     /**
     * Return the enum value for the given title.
     * 
     * @param aTitle
     * @return 
     */
    public static TaskSelectionKind findValue(String aTitle) {
        for (TaskSelectionKind kind : values()) {
            if (kind.getTitle().equalsIgnoreCase(aTitle))
                return kind;
        }
        
        return ERROR;
    }
}
