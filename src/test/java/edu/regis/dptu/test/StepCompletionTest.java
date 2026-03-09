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

import edu.regis.dptu.model.Step;
import edu.regis.dptu.model.StepCompletion;
import edu.regis.dptu.model.StepSubType;

public class StepCompletionTest {

    @Test
    public void testFieldsAndCounters() {
        Step step = new Step(15, 1, StepSubType.COMPLETE_CELL);
        StepCompletion completion = new StepCompletion(step, "{\"value\":1}");

        long originalDate = completion.getDate();
        completion.setDate(originalDate + 1000);
        completion.setData("{\"value\":2}");
        completion.setTimeoutOccur(true);
        completion.setHintsGiven(2);
        completion.incrementHints();

        assertEquals(step, completion.getStep());
        assertTrue(completion.isTimeoutOccur());
        assertEquals(3, completion.getHintsGiven());
        assertEquals("{\"value\":2}", completion.getData());
        assertTrue(completion.getDate() > originalDate);
    }
}

