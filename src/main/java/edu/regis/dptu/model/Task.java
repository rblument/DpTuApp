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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A multi-minute activity that can be skipped or interchanged with other tasks, whose steps the
 * student is expected to perform.
 *
 * <p>Tasks are based on tutoring behaviors in (VanLehn, 2006).
 *
 * <p>As a Task appears within actions/expectations created by an agent, all fields are final so
 * that other malicious agents cannot change them.
 *
 * @author rickb
 */
public class Task extends TitledModel {
    private static final Logger log = LoggerFactory.getLogger(Task.class);

    /** Indicates the type of task the student trying to complete. */
    private TaskKind kind = TaskKind.PROBLEM;

    /** The sequence in which this task is performed in its problem. */
    private int sequenceIndex;

    /** The current step (in index into steps). */
    private int currentStepIndex = 0;

    /** The steps that must be completed in this task. */
    private ArrayList<Step> steps;

    /** Convenience reference to the Problem to which this task belongs */
    private Problem problem;

    /** The knowledge component outcomes demonstrated/exercised by this step. */
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

        log.debug("Task created with id: {}, steps initialized: {}, exercisedComponentIds initialized: {}", id, steps.size(), exercisedComponentIds.size());
    }

    public TaskKind getKind() {
        log.debug("getKind() called, returning {}", kind);
        return kind;
    }

    public void setKind(TaskKind kind) {
        this.kind = kind;
        log.debug("setKind() called, set to {}", kind);
    }

    public void addStep(Step step) {
        steps.add(step);
        log.debug("addStep() called, added step id: {}, steps size now: {}", step.getId(), steps.size());
    }

    public ArrayList<Step> getSteps() {
        log.debug("getSteps() called, returning steps size: {}", steps.size());
        return steps;
    }

    public void setSteps(ArrayList<Step> steps) {
        this.steps = steps;
        log.debug("setSteps() called, new steps size: {}", steps.size());
    }

    public Step getStep(int index) {
        Step step = steps.get(index);
        log.debug("getStep() called for index {}, returning step id: {}", index, step.getId());
        return step;
    }

    public Step lastStep() {
        Step step = steps.get(steps.size() - 1);
        log.debug("lastStep() called, returning step id: {}", step.getId());
        return step;
    }

    public Step currentStep() {
        for (Step step : steps) {
            if (step.getSequenceIndex() == currentStepIndex) {
                log.debug("currentStep() called, returning step id: {}", step.getId());
                return step;
            }
        }
        log.debug("currentStep() called, no step found for currentStepIndex: {}", currentStepIndex);
        return null;
    }

    /**
     * @param stepId the database id of the step to find.
     * @return
     */
    public Step findStepById(int stepId) {
        for (Step step : steps) {
            if (step.getId() == stepId) {
                log.debug("findStepById() found step with id: {}", stepId);
                return step;
            }
        }
        log.debug("findStepById() did not find step with id: {}", stepId);
        return null;
    }

    public Problem getProblem() {
        log.debug("getProblem() called, returning {}", problem);
        return problem;
    }

    public void setProblem(Problem problem) {
        this.problem = problem;
        log.debug("setProblem() called, set problem to {}", problem);
    }

    public int getSequenceIndex() {
        log.debug("getSequenceIndex() called, returning {}", sequenceIndex);
        return sequenceIndex;
    }

    public void setSequenceIndex(int sequenceIndex) {
        this.sequenceIndex = sequenceIndex;
        log.debug("setSequenceIndex() called, set to {}", sequenceIndex);
    }

    public Step getCurrentStep() {
        Step step = steps.get(currentStepIndex);
        log.debug("getCurrentStep() called, returning step id: {}", step.getId());
        return step;
    }

    public int getCurrentStepIndex() {
        log.debug("getCurrentStepIndex() called, returning {}", currentStepIndex);
        return currentStepIndex;
    }

    public void setCurrentStepIndex(int currentStepIndex) {
        this.currentStepIndex = currentStepIndex;
        log.debug("setCurrentStepIndex() called, set to {}", currentStepIndex);
    }

    public void addExercisedComponentId(int componentId) {
        exercisedComponentIds.add(componentId);
        log.debug("addExercisedComponentId() called, added componentId: {}, size now: {}", componentId, exercisedComponentIds.size());
    }

    public ArrayList<Integer> getExercisedComponentIds() {
        log.debug("getExercisedComponentIds() called, returning size: {}", exercisedComponentIds.size());
        return exercisedComponentIds;
    }

    public void setExercisedComponentIds(ArrayList<Integer> componentIds) {
        this.exercisedComponentIds = componentIds;
        log.debug("setExercisedComponentIds() called, new size: {}", componentIds.size());
    }
}
