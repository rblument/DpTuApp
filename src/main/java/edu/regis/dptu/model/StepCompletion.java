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
     * The step that was completed.
     */
    private Step step;

    /**
     * The date the associated step was completed (time in milliseconds).
     */
    private long date;
    
    /**
     * Whether the step timed out.
     */
    private boolean timeoutOccur;
    
    /**
     * The number of hints given in the GUI for this step.
     */
    private int hintsGiven;
    
    /**
     * A JSon encoded objects corresponding to the example step type.
     */
    private String data;
    
    public StepCompletion(Step step, String data) {
        date = System.currentTimeMillis();
        
        this.data = data;
    }
    
    public Step getStep() {
        return step;
    }

    public void setStep(Step step) {
        this.step = step;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
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