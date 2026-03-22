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

/**
 * @author corey
 */
@SuppressWarnings("Logging")
public class MatrixChainProblemTest {
    @Test
    public void testMatrixChainExecution() {
        // Input sizes for matrices: 10x5, 5x2, 2x20, 20x12, 12x4, 4x60
        int[][] sizes = {{10, 5}, {5, 2}, {2, 20}, {20, 12}, {12, 4}, {4, 60}};

        MatrixChainProblem problem = new MatrixChainProblem(sizes);

        // Step through all lines until execution is complete
        while (!problem.hasFinished()) {
            problem.step();
        }

        // Confirm we reached finished state
        assertTrue(problem.hasFinished());

        // Optimal cost should be 2356 for this matrix sequence
        int expectedCost = 2356;
        int result = problem.getValueAt(0, problem.getVariableValue("n") - 1);
        assertEquals(expectedCost, result);

        // Now test undo all the way back to PRE
        while (problem.getNextLineNumber() != 0) {
            problem.undo();
        }

        // Final state after undoing everything
        assertTrue(problem.getNextLineNumber() == 0);
    }
}
