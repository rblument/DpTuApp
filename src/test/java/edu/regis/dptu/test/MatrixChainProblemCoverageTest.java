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

import edu.regis.dptu.model.MatrixChainProblem;

@SuppressWarnings("Logging")
public class MatrixChainProblemCoverageTest {
    @Test
    public void coversBacktrackingAndUndoPaths() {
        int[][] sizes = {{10, 5}, {5, 2}, {2, 20}, {20, 12}};
        MatrixChainProblem problem = new MatrixChainProblem(99, sizes);

        int guard = 0;
        while (!problem.hasFinished() && guard++ < 2000) {
            problem.step();
        }
        assertTrue(problem.hasFinished());

        problem.backtrackingOn();

        guard = 0;
        while (!problem.hasFinished() && guard++ < 2000) {
            problem.step();
        }
        assertTrue(problem.hasFinished());

        int undoCount = 0;
        while (problem.canStepBack() && undoCount++ < 2000) {
            problem.undo();
        }
        assertTrue(problem.getNextLineNumber() >= 0);
    }

    @Test
    public void throwsForUnimplementedMethods() {
        int[][] sizes = {{3, 4}, {4, 5}};
        MatrixChainProblem problem = new MatrixChainProblem(sizes);

        assertThrows(UnsupportedOperationException.class, problem::getExecutionState);
        assertThrows(UnsupportedOperationException.class, problem::prettyPrint);
    }

    @Test
    public void resetAndManualLineCallsRemainStable() {
        int[][] sizes = {{2, 3}, {3, 4}, {4, 5}};
        MatrixChainProblem problem = new MatrixChainProblem(sizes);

        problem.executeLine0();
        problem.executeLine1();
        problem.executeLine2();
        problem.executeLine3();
        problem.executeLine4();
        problem.executeLine5();
        problem.executeLine6();

        problem.reset();
        assertEquals(0, problem.getNextLineNumber());

        problem.executeLine100();
        problem.executeLine101();
        problem.executeLine102();
        problem.executeLine103();
        problem.executeLine104();
        problem.executeLine118();

        problem.undoLine100();
        problem.undoLine101();
        problem.undoLine102();
        problem.undoLine103();
        problem.undoLine104();
        problem.undoLine118();
    }
}
