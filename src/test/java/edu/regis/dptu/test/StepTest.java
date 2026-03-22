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

class StepTest {

    @Test
    void testConstructorAndDefaults() {
        Step step = new Step(100, 3, StepSubType.COMPLETE_CELL);

        assertEquals(100, step.getId());
        assertEquals(3, step.getSequenceIndex());
        assertEquals(StepSubType.COMPLETE_CELL, step.getSubType());

        assertNotNull(step.getHints());
        assertTrue(step.getHints().isEmpty());

        assertNotNull(step.getExercisedComponentIds());
        assertTrue(step.getExercisedComponentIds().isEmpty());

        assertFalse(step.isCompleted());
        assertFalse(step.isNotifyTutor());

        assertNull(step.getTimeout());
        assertNull(step.getData());
        assertEquals(0, step.getIntSolution(), "intSolution default should be 0 when not set");
        assertEquals(0, step.getCurrentHintIndex(), "Default currentHintIndex should be 0");
    }

    @Test
    void testConstructorWithIntSolution() {
        Step step = new Step(7, 1, StepSubType.COMPLETE_CELL, 42);

        assertEquals(7, step.getId());
        assertEquals(StepSubType.COMPLETE_CELL, step.getSubType());
        assertEquals(42, step.getIntSolution());
        assertFalse(step.isCompleted());
    }

    @Test
    void testHintsAddGetCurrentAndIndex() {
        Step step = new Step(1, 1, StepSubType.COMPLETE_CELL);

        Hint h1 = new Hint(10);
        h1.setText("First");
        Hint h2 = new Hint(20);
        h2.setText("Second");

        step.addHint(h1);
        step.addHint(h2);

        assertEquals(2, step.getHints().size());

        // Default index 0
        assertEquals(0, step.getCurrentHintIndex());
        assertSame(h1, step.getCurrentHint());

        // Change index
        step.setCurrentHintIndex(1);
        assertEquals(1, step.getCurrentHintIndex());
        assertSame(h2, step.getCurrentHint());
    }

    @Test
    void testFindHintById() {
        Step step = new Step(1, 1, StepSubType.COMPLETE_CELL);

        Hint h1 = new Hint(10);
        Hint h2 = new Hint(20);

        step.addHint(h1);
        step.addHint(h2);

        assertSame(h1, step.findHintById(10));
        assertSame(h2, step.findHintById(20));
        assertNull(step.findHintById(999));
    }

    @Test
    void testSetHintsReplacesList() {
        Step step = new Step(1, 1, StepSubType.COMPLETE_CELL);

        ArrayList<Hint> newHints = new ArrayList<>();
        newHints.add(new Hint(1));
        newHints.add(new Hint(2));

        step.setHints(newHints);

        assertSame(newHints, step.getHints());
        assertEquals(2, step.getHints().size());
    }

    @Test
    void testNotifyTutorAndCompletionFlags() {
        Step step = new Step(1, 1, StepSubType.COMPLETE_CELL);

        assertFalse(step.isNotifyTutor());
        step.setNotifyTutor(true);
        assertTrue(step.isNotifyTutor());

        assertFalse(step.isCompleted());
        step.setIsCompleted(true);
        assertTrue(step.isCompleted());
    }

    @Test
    void testExercisedComponentIdsAddGetSet() {
        Step step = new Step(1, 1, StepSubType.COMPLETE_CELL);

        step.addExercisedComponentId(101);
        step.addExercisedComponentId(202);

        assertEquals(2, step.getExercisedComponentIds().size());
        assertEquals(101, step.getExercisedComponentIds().get(0));
        assertEquals(202, step.getExercisedComponentIds().get(1));

        ArrayList<Integer> replacement = new ArrayList<>();
        replacement.add(999);
        step.setExercisedComponentIds(replacement);

        assertSame(replacement, step.getExercisedComponentIds());
        assertEquals(1, step.getExercisedComponentIds().size());
        assertEquals(999, step.getExercisedComponentIds().get(0));
    }

    @Test
    void testDataGetSet() {
        Step step = new Step(1, 1, StepSubType.COMPLETE_CELL);

        assertNull(step.getData());

        step.setData("{\"row\":1,\"col\":2,\"value\":5}");
        assertEquals("{\"row\":1,\"col\":2,\"value\":5}", step.getData());
    }

    @Test
    void testCheckSolutionTrueFalseBranches() {
        Step step = new Step(1, 1, StepSubType.COMPLETE_CELL, 42);

        assertTrue(step.checkSolution(42));
        assertFalse(step.checkSolution(41));
        assertFalse(step.checkSolution(0));
    }
}
