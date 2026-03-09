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

import edu.regis.dptu.model.Hint;
import edu.regis.dptu.model.PendingStep;
import edu.regis.dptu.model.Step;
import edu.regis.dptu.model.StepSubType;

public class PendingStepTest {

    @Test
    public void testHintBehaviorAndFlags() {
        Step step = new Step(1, 1, StepSubType.COMPLETE_CELL);
        PendingStep pendingStep = new PendingStep(2, step);

        pendingStep.setCurrentHintIndex(7);
        assertEquals("Sorry, no hints available", pendingStep.getCurrentHint().getText());

        Hint firstHint = new Hint();
        firstHint.setText("First hint");
        Hint secondHint = new Hint();
        secondHint.setText("Second hint");
        step.addHint(firstHint);
        step.addHint(secondHint);

        pendingStep.setCurrentHintIndex(99);
        assertEquals("First hint", pendingStep.getCurrentHint().getText());

        pendingStep.setNotifyTutor(true);
        pendingStep.setIsCompleted(true);
        assertTrue(pendingStep.isNotifyTutor());
        assertTrue(pendingStep.isCompleted());
    }
}
