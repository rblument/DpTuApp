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
 * Captures the tutor's reply to a previous step completed requests. 
 * 
 * @author rickb
 */
public class StepCompletionReply {
    /**
     * Was the student's submitted answer correct.
     */
    private boolean isCorrect;
    
    /**
     * The tutor is requesting the student to perform the same task again.
     * 
     * If true, data contains a JSon encoded Step object.
     */
    private boolean isRepeatStep;
    
    /**
     * The tutor believes the student understands the previous step and
     * is requesting the student to complete a new Task.
     * 
     * If true, data contains a JSon encoded Task object.
     */
    private boolean isNewTask;
    
    /**
     * The tutor is requesting the student to perform the following step,
     * which might be a new example.
     * 
     * If true, data contains a JSon encoded Step object.
     */
    private boolean isNewStep;
    
    /**
     * Tutor is asking the student to perform the next step in the current
     * (previous) task.
     * 
     * If true, data contains a Json encoded Step object.
     */
    private boolean isNextStep;

    /**
     * A JSon encoded Task or Step object (see above flags for the type).
     */
    private String data;
    
    public StepCompletionReply() {
        
    }

    public boolean isCorrect() {
        return isCorrect;
    }
    
    public boolean getIsCorrect() {
        return isCorrect;
    }

    public void setIsCorrect(boolean isCorrect) {
        this.isCorrect = isCorrect;
    }

    public boolean isRepeatStep() {
        return isRepeatStep;
    }
    
    public boolean getIsRepeatStep() {
        return isRepeatStep;
    }

    public void setIsRepeatStep(boolean isRepeatStep) {
        this.isRepeatStep = isRepeatStep;
    }

    public boolean isNewTask() {
        return isNewTask;
    }
    
    public boolean getIsNewTask() {
        return isNewTask;
    }

    public void setIsNewTask(boolean isNewTask) {
        this.isNewTask = isNewTask;
    }

    public boolean isNewStep() {
        return isNewStep;
    }
    
    public boolean getIsNewStep() {
        return isNewStep;
    }

    public void setIsNewStep(boolean isNewStep) {
        this.isNewStep = isNewStep;
    }

    public boolean isIsNextStep() {
        return isNextStep;
    }
    
    public boolean getIsNextStep() {
        return isNextStep;
    }

    public void setIsNextStep(boolean isNextStep) {
        this.isNextStep = isNextStep;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }  
}
