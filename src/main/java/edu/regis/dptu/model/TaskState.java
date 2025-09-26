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

import java.util.HashMap;
import java.util.LinkedList;

/**
 * To provide appropriate hints to the student, we need to keep track of what task steps they've
 * already performed.
 *
 * @author rickb
 */
public class TaskState {
    /** The sequence id, zero-indexed, of the currently expected task. */
    private int currentTask = 0;

    /** The sequence id, zero-indexed, of the current step in the current task. */
    private int currentStep = 0;

    /** The sequence id, zero-indexed, of the current hint in the current step. */
    private int currentHint = 0;

    /**
     * The steps completed by the student in this task, which may be out order from the desired
     * learning outcome.
     */
    private LinkedList<StepCompletion> completedSteps;

    /** The time when this task state was started. */
    private long startTime;

    /** The time when this task state was completed, or 0 if not yet completed. */
    private long completionTime;

    /** Map of step timestamps - key is step ID, value is completion time. */
    private HashMap<Integer, Long> stepTimestamps;

    /** Map of hint timestamps - key is hint ID, value is request time. */
    private HashMap<Integer, Long> hintTimestamps;

    /** Initialize this task state with an empty list of completed steps. */
    public TaskState() {
        completedSteps = new LinkedList<>();
        startTime = System.currentTimeMillis();
        completionTime = 0;
        stepTimestamps = new HashMap<>();
        hintTimestamps = new HashMap<>();
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

    /**
     * Record a step completion timestamp.
     *
     * @param stepId ID of the completed step
     */
    public void recordStepCompletion(int stepId) {
        stepTimestamps.put(stepId, System.currentTimeMillis());
    }

    /**
     * Record a hint request timestamp.
     *
     * @param hintId ID of the requested hint
     */
    public void recordHintRequest(int hintId) {
        hintTimestamps.put(hintId, System.currentTimeMillis());
    }

    /** Record task completion and set completion timestamp. */
    public void recordTaskCompletion() {
        completionTime = System.currentTimeMillis();
    }

    /**
     * Get the total time spent on this task in milliseconds.
     *
     * @return Total time if task is completed, current duration if still in progress
     */
    public long getTotalTime() {
        if (completionTime > 0) {
            return completionTime - startTime;
        }
        return System.currentTimeMillis() - startTime;
    }

    /**
     * Get time spent on a specific step in milliseconds.
     *
     * @param stepId ID of the step
     * @return Time spent on step, or -1 if step not completed
     */
    public long getStepTime(int stepId) {
        Long timestamp = stepTimestamps.get(stepId);
        if (timestamp == null) {
            return -1;
        }
        // For first step, measure from task start
        if (stepId == 0) {
            return timestamp - startTime;
        }
        // For other steps, measure from previous step
        Long prevTimestamp = stepTimestamps.get(stepId - 1);
        if (prevTimestamp == null) {
            return -1;
        }
        return timestamp - prevTimestamp;
    }

    /**
     * Get timestamp of when a hint was requested.
     *
     * @param hintId ID of the hint
     * @return Timestamp when hint was requested, or -1 if hint not requested
     */
    public long getHintRequestTime(int hintId) {
        return hintTimestamps.getOrDefault(hintId, -1L);
    }

    /**
     * Get time taken before first hint request.
     *
     * @return Time in milliseconds before first hint, or -1 if no hints requested
     */
    public long getTimeToFirstHint() {
        if (hintTimestamps.isEmpty()) {
            return -1;
        }
        long firstHintTime = Long.MAX_VALUE;
        for (long timestamp : hintTimestamps.values()) {
            firstHintTime = Math.min(firstHintTime, timestamp);
        }
        return firstHintTime - startTime;
    }

    public long getStartTime() {
        return startTime;
    }

    public long getCompletionTime() {
        return completionTime;
    }

    public HashMap<Integer, Long> getStepTimestamps() {
        return new HashMap<>(stepTimestamps); // Return copy for immutability
    }

    public HashMap<Integer, Long> getHintTimestamps() {
        return new HashMap<>(hintTimestamps); // Return copy for immutability
    }
}
