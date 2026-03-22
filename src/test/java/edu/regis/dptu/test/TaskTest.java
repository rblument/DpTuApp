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

import java.util.ArrayList;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import edu.regis.dptu.model.Hint;
import edu.regis.dptu.model.Step;
import edu.regis.dptu.model.StepSubType;
import edu.regis.dptu.model.Task;
import edu.regis.dptu.model.TaskKind;

@SuppressWarnings("Logging")
public class TaskTest {
    @Test
    public void taskCoversCoreBehaviors() {
        Task task = new Task(42);
        task.setKind(TaskKind.CREATE_TABLE);
        assertEquals(TaskKind.CREATE_TABLE, task.getKind());

        Step step1 = new Step(1, 0, StepSubType.INFO_MESSAGE);
        Step step2 = new Step(2, 1, StepSubType.COMPLETE_CELL);
        task.addStep(step1);
        task.addStep(step2);

        assertEquals(2, task.getSteps().size());
        assertEquals(step1, task.getStep(0));
        assertEquals(step2, task.lastStep());
        assertEquals(step1, task.currentStep());

        task.setCurrentStepIndex(1);
        assertEquals(1, task.getCurrentStepIndex());
        assertEquals(step2, task.getCurrentStep());

        assertEquals(step1, task.findStepById(1));
        assertNull(task.findStepById(999));

        task.setSequenceIndex(7);
        assertEquals(7, task.getSequenceIndex());

        task.addExercisedComponentId(10);
        task.addExercisedComponentId(11);
        assertEquals(2, task.getExercisedComponentIds().size());

        ArrayList<Integer> replacement = new ArrayList<>();
        replacement.add(99);
        task.setExercisedComponentIds(replacement);
        assertEquals(1, task.getExercisedComponentIds().size());
        assertEquals(99, task.getExercisedComponentIds().get(0));

        ArrayList<Step> replacementSteps = new ArrayList<>();
        replacementSteps.add(new Step(3, 0, StepSubType.COMPLETE_FIRST_ROW));
        task.setSteps(replacementSteps);
        assertEquals(1, task.getSteps().size());

        Hint hint = new Hint(5);
        hint.setText("hint");
        replacementSteps.get(0).addHint(hint);
        assertEquals(hint, replacementSteps.get(0).getCurrentHint());
    }
}
