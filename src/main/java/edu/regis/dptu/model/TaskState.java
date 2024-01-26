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

import java.util.LinkedList;

/**
 * To provide appropriate hints to the student, we need to keep track of 
 * what task steps they've already performed.
 * 
 * @author rickb
 */
public class TaskState {   
    /**
     * The sequence id, zero-indexed, of the currently expected task.
     */
    private int currentTask = 0;
    
    /**
     * The sequence id, zero-indexed, of the current step in the current task.
     */
    private int currentStep = 0;
    
    /**
     * The sequence id, zero-indexed, of the current hint in the current step.
     */
    private int currentHint = 0;
    
    /**
     * The steps completed by the student in this task, which may be out order
     * from the desired learning outcome. 
     */
    private LinkedList<StepCompletion> completedSteps;
    
    /**
     * Initialize this task state with an empty list of completed steps.
     */
    public TaskState() {
        completedSteps = new LinkedList<>();
    }

    public int getCurrentTask() {
        return currentTask;
    }

    public void setCurrentTask(int currentTask) {
        this.currentTask = currentTask;
    }
    
    public void incfTask() {
        currentTask++;
    }

    public int getCurrentStep() {
        return currentStep;
    }

    public void setCurrentStep(int currentStep) {
        this.currentStep = currentStep;
    }
    
    public void incfStep() {
        currentStep++;
    }

    public int getCurrentHint() {
        return currentHint;
    }

    public void setCurrentHint(int currentHint) {
        this.currentHint = currentHint;
    } 
    
    public void incfHint() {
        currentHint++;
    }
    
    public void addStepCompletion(StepCompletion step) {
        completedSteps.add(step);
    }

    public LinkedList<StepCompletion> getCompletedSteps() {
        return completedSteps;
    }

    public void setCompletedSteps(LinkedList<StepCompletion> completedSteps) {
        this.completedSteps = completedSteps;
    }
}
