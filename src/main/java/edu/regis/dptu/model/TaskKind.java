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
 * The task types that a student may be required to complete in the tutor.
 * 
 * @author rickb
 */
public enum TaskKind {
    /**
     * A task requiring a Student to complete a Problem.
     */
    PROBLEM("Problem");
    
    /**
     * A GUI displayable string identifying this task kind.
     */
    private final String title;

    /**
     * Initialize this task kind with its title.
     * 
     * @param title 
     */
    TaskKind(String title) {
        this.title = title;
    }
    
    /**
     * Return the title for this task.
     * 
     * @return a String
     */
    public String title() {
        return title;
    }
}
    
