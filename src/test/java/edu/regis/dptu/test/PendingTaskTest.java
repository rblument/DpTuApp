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
package edu.regis.dptu.test;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import edu.regis.dptu.model.PendingStep;
import edu.regis.dptu.model.PendingTask;
import edu.regis.dptu.model.Step;
import edu.regis.dptu.model.StepSubType;
import edu.regis.dptu.model.Task;

public class PendingTaskTest {

    @Test
    public void testCurrentStepAndCompletion() {
        Step step = new Step(1, 1, StepSubType.COMPLETE_CELL);
        Task task = new Task(10);
        task.addStep(step);

        PendingStep pendingStep = new PendingStep(2, step);
        PendingTask pendingTask = new PendingTask(task);

        pendingTask.setCurrentStep(pendingStep);
        pendingStep.setIsCompleted(true);
        assertTrue(pendingTask.isTaskCompleted());

        pendingStep.setIsCompleted(false);
        assertFalse(pendingTask.isTaskCompleted());

        pendingTask.setCurrentStep(step);
        assertNotNull(pendingTask.currentStep());
        assertEquals(task, pendingTask.getTask());
    }

    @Test
    public void testIsTaskCompletedWhenCurrentStepIsNull() {
        PendingTask pendingTask = new PendingTask(new Task(99));

        assertFalse(pendingTask.isTaskCompleted());
    }
}
