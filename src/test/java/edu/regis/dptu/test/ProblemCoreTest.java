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

import java.lang.reflect.Field;
import java.util.ArrayList;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import edu.regis.dptu.model.Problem;
import edu.regis.dptu.model.ProblemKind;
import edu.regis.dptu.model.ProblemListener;

@SuppressWarnings("Logging")
public class ProblemCoreTest {

    @Test
    public void addProblemListenerRejectsNullAndPreventsDuplicates() {
        FakeProblem problem = new FakeProblem();
        CountingListener listener = new CountingListener();

        assertThrows(NullPointerException.class, () -> problem.addProblemListener(null));

        problem.addProblemListener(listener);
        problem.addProblemListener(listener);

        assertEquals(1, problem.getProblemListeners().size());
    }

    @Test
    public void stepAndUndoDriveExecutionAndNotifications() {
        FakeProblem problem = new FakeProblem();
        CountingListener listener = new CountingListener();
        problem.addProblemListener(listener);

        problem.step();
        assertEquals(1, problem.executeCount);
        assertEquals(1, listener.updateCount);

        problem.undo();
        assertEquals(1, problem.undoCount);
        assertEquals(2, listener.updateCount);
        assertEquals(0, problem.getNextLineNumber());
    }

    @Test
    public void undoOnEmptyHistoryAndMissingMethodAreSafeNoOps() {
        FakeProblem problem = new FakeProblem();

        problem.undo();
        problem.executeMethod("doesNotExist");

        assertEquals(0, problem.undoCount);
    }

    @Test
    public void gettersAndTableAccessReturnExpectedValues() {
        FakeProblem problem = new FakeProblem();

        assertNotNull(problem.getVariableNames());
        assertEquals(7, problem.getVariableValue("scalar"));
        assertEquals(2, problem.getValueAt(0, 1));
        assertEquals(100, problem.getBacktrackingStartNum());
        assertEquals(-1, problem.decode(0, 0));
    }

    @Test
    public void getVariableObjectHandlesNullVariablesMap() throws Exception {
        FakeProblem problem = new FakeProblem();

        Field variablesField = Problem.class.getDeclaredField("variables");
        variablesField.setAccessible(true);
        variablesField.set(problem, null);

        assertNull(problem.getVariableObject("scalar"));
    }

    @Test
    public void problemSettersAndBatchUndoUpdateState() {
        FakeProblem problem = new FakeProblem();

        problem.setSubTypeId(55);
        problem.setTaskId(99);
        problem.setNextLineNumber(3);

        ArrayList<String> code = new ArrayList<>();
        code.add("line1");
        problem.setCodeStatements(code);

        ArrayList<String> backtracking = new ArrayList<>();
        backtracking.add("back1");
        problem.setBacktrackingCodeStatements(backtracking);

        problem.step();
        problem.step();
        problem.undo(2);

        assertEquals(55, problem.getSubTypeId());
        assertEquals(99, problem.getTaskId());
        assertEquals(1, problem.getCodeStatements().size());
        assertEquals(1, problem.getBacktrackingCodeStatements().size());
        assertEquals(0, problem.undoCount);
    }

    @Test
    public void stepNUsesTimerAndStopsWhenFinished() {
        FakeProblem problem = new FakeProblem();
        CountingListener listener = new CountingListener();
        problem.addProblemListener(listener);

        problem.finishOnNextStep = true;
        problem.step(3);

        long deadline = System.currentTimeMillis() + 1500;
        while (problem.executeCount == 0 && System.currentTimeMillis() < deadline) {
            Thread.yield();
        }

        assertTrue(problem.executeCount >= 1);
        assertTrue(listener.updateCount >= 1);
    }

    private static class CountingListener implements ProblemListener {
        int updateCount = 0;

        @Override
        public void problemUpdated(Problem problem) {
            updateCount++;
        }
    }

    private static class FakeProblem extends Problem {
        int executeCount = 0;
        int undoCount = 0;
        boolean finishOnNextStep = false;
        boolean finished = false;

        FakeProblem() {
            super(1);
            setTableVariable("table");
            variables.put("scalar", 7);
            variables.put("table", new int[][] {{1, 2}, {3, 4}});
        }

        @Override
        public ProblemKind getType() {
            return ProblemKind.LCS_PROBLEM;
        }

        @Override
        public boolean hasFinished() {
            return finished;
        }

        @Override
        public void reset() {
            setNextLineNumber(0);
        }

        @Override
        protected void loadCodeStatements() {}

        @Override
        protected void loadBacktrackingCodeStatements() {}

        @Override
        public boolean canStepBack() {
            return getNextLineNumber() > 0;
        }

        @Override
        public boolean backtrackReady() {
            return false;
        }

        @Override
        public void backtrackingOn() {
            setNextLineNumber(getBacktrackingStartNum());
        }

        @Override
        public boolean undoingBacktrackButton() {
            return false;
        }

        @SuppressWarnings("unused")
        private void executeLine0() {
            executeCount++;
            if (finishOnNextStep) {
                finished = true;
            }
        }

        @SuppressWarnings("unused")
        private void undoLine0() {
            undoCount++;
        }

        int decode(int row, int col) {
            return TableToLineNumberdecoder(row, col);
        }
    }
}
