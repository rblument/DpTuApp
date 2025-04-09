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

import java.util.ArrayList;

/**
 * A multi-minute activity that can be skipped or interchanged with other tasks,
 * whose steps the student is expected to perform.
 * 
 * Tasks are based on tutoring behaviors in (VanLehn, 2006).
 * 
 * As a Task appears within actions/expectations created by an agent,
 * all fields are final so that other malicious agents cannot change them.
 * 
 * @author rickb
 */
public class Task extends TitledModel {
    /**
     * Indicates the type of task the student trying to complete.
     */
    private TaskKind kind = TaskKind.LCS_PROBLEM;
    
    /**
     * The sequence in which this task is performed in its problem.
     */
    private int sequenceIndex;
    
    /**
     * The current step (in index into steps).
     */
    private int currentStepIndex = 0;
    
    /**
     * The steps that must be completed in this task.
     */
    private ArrayList<Step> steps;
    
    /**
     * Convenience reference to the Problem to which this task belongs
     */
    private Problem problem;
    
    /**
     * The knowledge component outcomes demonstrated/exercised by this step.
     */
    protected ArrayList<Integer> exercisedComponentIds;
 
    public Task() {
        this(Model.DEFAULT_ID);
    }
    
    /**
     * Instantiate this task with the given id.
     * 
     * @param id database id of this task.
     */
    public Task(int id) {
        super(id);
        
        this.steps = new ArrayList<>();
        
        exercisedComponentIds = new ArrayList<>();
    }
    
    public TaskKind getKind() {
        return kind;
    }

    public void setKind(TaskKind kind) {
        this.kind = kind;
    }

    public void addStep(Step step) {
        steps.add(step);
    }
    
    public ArrayList<Step> getSteps() {
        System.out.println("Task.getSteps: " + steps.size());
        return steps;
    }
    
    public void setSteps(ArrayList<Step> steps) {
        System.out.println("Task.setSteps: " + steps);
        this.steps = steps;
    }
    
    public Step getStep(int index) {
        return steps.get(index);
    }
    
    public Step lastStep() {
        return steps.get(steps.size() - 1);
    }
    
    public Step currentStep() {    
        System.out.println("*** Task.currentStep: " + steps.size());
        for (Step step : steps)
            if (step.getSequenceIndex() == currentStepIndex)
                return step;

        return null;
    }
    
    /**
     * 
     * @param stepId the database id of the step to find.
     * @return 
     */
    public Step findStepById(int stepId) {
        for (Step step : steps)
            if (step.getId() == stepId)
                return step;
        
        return null;
    }
   
    

    public Problem getProblem() {
        return problem;
    }

    public void setProblem(Problem problem) {
        this.problem = problem;
    }  

    public int getSequenceIndex() {
        return sequenceIndex;
    }
    
    public void setSequenceIndex(int sequenceIndex) {
        this.sequenceIndex = sequenceIndex;
    }
    
    public Step getCurrentStep() {
        return steps.get(currentStepIndex);
    }

    public int getCurrentStepIndex() {
        return currentStepIndex;
    }

    public void setCurrentStepIndex(int currentStepIndex) {
        this.currentStepIndex = currentStepIndex;
    }
    
    public void addExercisedComponentId(int componentId) {
        exercisedComponentIds.add(componentId);
    }
    
    public ArrayList<Integer> getExercisedComponentIds() {
        return exercisedComponentIds;
    }

    public void setExercisedComponentIds(ArrayList<Integer> componentIds) {
        this.exercisedComponentIds = componentIds;
    }
}
