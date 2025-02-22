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
 * A task that a student needs to complete as part of the current tutoring
 * session (this may be the current task or one that is still to be completed).
 * 
 *A pending task represents the dynamic information associated with a student
 * completing a task, while the associated task is a static step within a
 * course.
 * 
 * @author rickb
 */
public class PendingTask {
    /**
     * The static course task that is associated with this pending task.
     */
    private Task task;
    
    private PendingStep currentStep;
    
    public PendingTask(Task task) {
        this.task = task;
    }

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }
    
    // ToDo: (Rick) Should the current step really be within the task?
    // I think it should be here in the pending task.
    public PendingStep getCurrentStep() {
        return currentStep;
    }
    
    public void setCurrentStep(PendingStep step) {
        this.currentStep = step;
    }
    
    public void setCurrentStep(Step step) {
        currentStep = new PendingStep(step);
    }
    
    // ToDo: See comment above
    public PendingStep currentStep() {
        return currentStep;
    }
    
       /**
     * Return whether this task is completed.
     * 
     * @return true if all of the steps in this task have been completed, 
     *         otherwise false
     */
    public boolean isTaskCompleted() {
        // ToDo: all steps handled?
        return currentStep.isCompleted();
    }
}
