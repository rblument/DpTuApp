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

import java.util.HashMap;
import java.util.LinkedList;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import edu.regis.dptu.model.TaskState;

class TaskStateTest {

    @Test
    void testDefaultsAndSettersAndIncrementers() {
        TaskState ts = new TaskState();

        assertEquals(0, ts.getCurrentTask());
        assertEquals(0, ts.getCurrentStep());
        assertEquals(0, ts.getCurrentHint());
        assertNotNull(ts.getCompletedSteps());
        assertTrue(ts.getCompletedSteps().isEmpty());

        ts.setCurrentTask(3);
        ts.incfTask();
        assertEquals(4, ts.getCurrentTask());

        ts.setCurrentStep(10);
        ts.incfStep();
        assertEquals(11, ts.getCurrentStep());

        ts.setCurrentHint(7);
        ts.incfHint();
        assertEquals(8, ts.getCurrentHint());

        assertTrue(ts.getStartTime() > 0);
        assertEquals(0, ts.getCompletionTime());
    }

    @Test
    void testCompletedStepsListReplacement() {
        TaskState ts = new TaskState();

        LinkedList<?> original = ts.getCompletedSteps();
        assertNotNull(original);

        LinkedList newList = new LinkedList();
        newList.add("dummy");
        ts.setCompletedSteps(newList);

        assertSame(newList, ts.getCompletedSteps());
        assertEquals(1, ts.getCompletedSteps().size());
    }

    @Test
    void testGetTotalTimeBeforeAndAfterCompletion() throws Exception {
        TaskState ts = new TaskState();

        long t1 = ts.getTotalTime();
        Thread.sleep(2);
        long t2 = ts.getTotalTime();
        assertTrue(t2 >= t1, "Elapsed time should not decrease while in progress");

        ts.recordTaskCompletion();
        long completed = ts.getTotalTime();
        assertTrue(completed >= 0);

        // After completion, total time should remain stable (or extremely close).
        Thread.sleep(2);
        long completed2 = ts.getTotalTime();
        assertEquals(completed, completed2, "Total time should be fixed after completion");
        assertTrue(ts.getCompletionTime() > 0);
    }

    @Test
    void testStepTimingPaths() throws Exception {
        TaskState ts = new TaskState();

        // Not completed yet => -1
        assertEquals(-1, ts.getStepTime(0));
        assertEquals(-1, ts.getStepTime(2));

        // Record step 0 => should be >= 0 relative to start
        Thread.sleep(2);
        ts.recordStepCompletion(0);
        long step0 = ts.getStepTime(0);
        assertTrue(step0 >= 0);

        // Step 2 without step 1 => should be -1 (previous missing)
        ts.recordStepCompletion(2);
        assertEquals(-1, ts.getStepTime(2), "Should be -1 if previous step timestamp missing");

        // Now add step 1 and step 2 again to cover normal path
        Thread.sleep(2);
        ts.recordStepCompletion(1);
        Thread.sleep(2);
        ts.recordStepCompletion(2);

        long step1 = ts.getStepTime(1);
        long step2 = ts.getStepTime(2);

        assertTrue(step1 >= 0);
        assertTrue(step2 >= 0);
    }

    @Test
    void testHintTimingPaths() throws Exception {
        TaskState ts = new TaskState();

        assertEquals(-1, ts.getHintRequestTime(123));
        assertEquals(-1, ts.getTimeToFirstHint(), "No hints => -1");

        Thread.sleep(2);
        ts.recordHintRequest(10);
        Thread.sleep(2);
        ts.recordHintRequest(20);

        assertTrue(ts.getHintRequestTime(10) > 0);
        assertTrue(ts.getHintRequestTime(20) > 0);
        assertEquals(-1, ts.getHintRequestTime(999));

        long timeToFirst = ts.getTimeToFirstHint();
        assertTrue(timeToFirst >= 0, "First hint time delta should be non-negative");
    }

    @Test
    void testTimestampMapsAreCopies() {
        TaskState ts = new TaskState();

        ts.recordStepCompletion(0);
        ts.recordHintRequest(1);

        HashMap<Integer, Long> stepCopy = ts.getStepTimestamps();
        HashMap<Integer, Long> hintCopy = ts.getHintTimestamps();

        assertEquals(1, stepCopy.size());
        assertEquals(1, hintCopy.size());

        // Mutate returned copies; underlying state should not change
        stepCopy.put(999, 123L);
        hintCopy.put(999, 123L);

        HashMap<Integer, Long> stepCopy2 = ts.getStepTimestamps();
        HashMap<Integer, Long> hintCopy2 = ts.getHintTimestamps();

        assertFalse(stepCopy2.containsKey(999));
        assertFalse(hintCopy2.containsKey(999));
    }
}
