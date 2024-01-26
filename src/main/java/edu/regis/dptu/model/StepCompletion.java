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
 * Represents a step that the student completed.
 * 
 * @author rickb
 */
public class StepCompletion {
    /**
     * The database id of the step that was completed.
     */
    private int stepId = Model.DEFAULT_ID;
    
    /**
     * The date the associated step was completed (time in milliseconds).
     */
    private long date;
    
    private boolean timeoutOccur;
    
    private int hintsGiven;
    
    /**
     * Initialize this completion with the current date and time.
     */
    public StepCompletion() {
        date = System.currentTimeMillis();
    }

    /**
     * Return the database id of the step associated with this completion.
     * 
     * @return int id
     */
    public int getStepId() {
        return stepId;
    }

    /**
     * Set the id of the step associated with this step completion (to only be
     * used by the database when this step completion is being restored).
     * 
     * @param stepId int id of the associated step.
     */
    public void setStepId(int stepId) {
        this.stepId = stepId;
    }

    /**
     * Return the time the associated step was completed.
     * 
     * @return a long (time in milliseconds)
     */
    public long getDate() {
        return date;
    }
    
    /**
     * Set the time the associated step was completed (to only be used by
     * the database when restoring a step completion).
     * 
     * @param date a long (time in milliseconds)
     */
    public void setDate(long date) {
        this.date = date;
    }
}
