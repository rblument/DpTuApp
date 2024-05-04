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
    private TaskKind taskType = TaskKind.PROBLEM;
    
        /**
     * The type of this task, which can be used to determine the appropriate
     * view to display, if different from each of its steps.
     */
    private ExampleType type;
    
    /**
     * The scaffolding support for this task.
     */
    private ScaffoldLevel scaffolding = ScaffoldLevel.EXTREME;
    
    /**
     * Indicates the student's overall progress on this task.
     * (For IN_PROGRESS tasks, the student model has the current step.)
     */
    //private TaskState state = TaskState.PENDING;
    
    /**
     * The sequence in which this task is performed in its problem.
     */
    private int sequenceId;
    
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
     * ToDo: the tasks already completed in this task???
     */
    private TaskState state;
    
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
        
        state = new TaskState();
    }
    
    /**
     * Task constructor for LCS problem. Accepts the category of task being 
     * constructed and the two Strings making up the LCS task. 
     * 
     * @param id database id of this task
     * @param kind the category of task being created (TypeKind)
     * @param s1 String #1 of the LCS problem 
     * @param s2 
     */
    public Task(int id, TaskKind kind, String s1, String s2){
        super(id);
        
        switch(kind) {
            case CREATE_TABLE:
                this.taskType = TaskKind.CREATE_TABLE;
                this.steps = createTableSteps(s1, s2);
                break;
            case SOLUTION_PATH:
                this.taskType = TaskKind.SOLUTION_PATH;
                this.steps = createPathSteps(s1, s2);
                break;
            case SOLVE:
                this.taskType = TaskKind.SOLVE;
                this.steps = createSolveSteps(s1, s2);
                break;
            default:
                this.steps = new ArrayList<>();
                break;    
        }
        
        exercisedComponentIds = new ArrayList<>();
        
        state = new TaskState();
    }
    
    
    
    public TaskKind getTaskType() {
        return taskType;
    }

    public void setTaskType(TaskKind type) {
        this.taskType = type;
    }
    
    public ExampleType getType() {
        return type;
    }

    public void setType(ExampleType type) {
        this.type = type;
    }

    public ScaffoldLevel getScaffolding() {
        return scaffolding;
    }

    public void setScaffolding(ScaffoldLevel scaffolding) {
        this.scaffolding = scaffolding;
    }
    
    
    
    public TaskState getState() {
        return state;
    }

    public void setState(TaskState state) {
        this.state = state;
    } 

    public void addStep(Step step) {
        steps.add(step);
    }
    
    public ArrayList<Step> getSteps() {
        System.out.println("Task.getStepts: " + steps.size());
        return steps;
    }
    
    public void setSteps(ArrayList<Step> steps) {
        System.out.println("Task.setSepats: " + steps);
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
            if (step.getSequenceId() == currentStepIndex)
                return step;

        return null;
    }
    
    /**
     * Locally update the task state to note that the given step has been 
     * performed by the student (this doesn't notify the tutor).
     * 
     * @param step the Step that was completed by the student.
     */
    public void completedStep(StepCompletion completion) {
        completion.getStep().setIsCompleted(true);
        
        state.addStepCompletion(completion);
    }
    
    /**
     * Return whether this task is completed.
     * 
     * @return true if all of the steps in this task have been completed, 
     *         otherwise false
     */
    public boolean isTaskCompleted() {
        for (Step step : steps)
            if (!step.isCompleted())
                return false;
        
        return true;
    }

    public Problem getProblem() {
        return problem;
    }

    public void setProblem(Problem problem) {
        this.problem = problem;
    }  

    public int getSequenceId() {
        return sequenceId;
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
    
    /**
     * Generates the array list of steps to create the dynamic programming table
     * for Strings s1 and s2. Each step has a solution (the correct value the cell
     * should have) and a SubType based on how the student would arrive at that 
     * solution. 
     * 
     * Because an ArrayList is 1-dimensional, the cells are stored in a 
     * left-to-right + top-to-bottom fashion. Use the CellTranslate methods to
     * move between 1D and 2D values. 
     * 
     * @param s1 String 1 in the dynamic programming LCS problem 
     * @param s2 String 2 in the dynamic programming LCS problem 
     * @return The ArrayList containing each step to create the solution table.
     */
    private ArrayList<Step> createTableSteps(String s1, String s2){
        ArrayList<Step> stepsList = new ArrayList<>();
        int n = s1.length() + 1;
        int m = s2.length() + 1;
        int stepCount = 0;
        
        /*
        The first "n" cells (the top row) are all zero by default
        
        TO DO: establish how 'id' number is generated?
        */
        for(int i = 0; i < n; i++){
            Step step = new Step(1, stepCount, StepSubType.DEFAULT_ZERO, 0);
            stepsList.add(step);
            stepCount++;
        }
        
        /**
         * For all remaining rows... standard dynamic programming table algorithm
         * 
         * i = current row index
         * j = current column index
         * n = row length
         * m = column length
         */
        
        //Proceed through every row except for the first (already created above)
        for(int i = 1; i < n; i++){
            
            //For each row, proceed through every column
            for(int j = 0; j < m; j++){
                Step step = null;
                
                //Value in column 0 is always zero by default
                if(j == 0){
                    step = new Step(1, stepCount, StepSubType.DEFAULT_ZERO, 0);
                }
                
                //If the character matches in the n[i] and m[j] indices...
                else if(s1.charAt(i - 1) == s2.charAt(j - 1)){
                    //Get the solution at the diagonal index and add one for this solution
                    int diagPos = stepCount - (n + 1);
                    int solution = 1 + stepsList.get(diagPos).getIntSolution();
                    step = new Step(1, stepCount, StepSubType.INCREASE_DIAGONAL, solution);
                }
                //No character match. Use the left or upper cell, whichever is larger
                else{
                    int indexLeft = stepCount - 1;
                    int indexUp = stepCount - n;
                    int valueLeft = stepsList.get(indexLeft).getIntSolution();
                    int valueUp = stepsList.get(indexUp).getIntSolution();
                    
                    if(valueLeft >= valueUp){
                        step = new Step(1, stepCount, StepSubType.USE_LEFT, valueLeft);
                        
                    }else{
                        step = new Step(1, stepCount, StepSubType.USE_UPPER, valueUp);
                    }
                }
                stepsList.add(step);
                stepCount++;
            }
        }
        return stepsList;
    }
    
    private ArrayList<Step> createPathSteps(String s1, String s2){
        ArrayList<Step> steps = new ArrayList<>();
        
        return steps;
    }
    
    private ArrayList<Step> createSolveSteps(String s1, String s2){
        ArrayList<Step> steps = new ArrayList<>();
        
        return steps;
    }
}