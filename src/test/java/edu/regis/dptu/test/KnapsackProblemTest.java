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

import edu.regis.dptu.model.KnapsackProblem;
import edu.regis.dptu.model.ProblemKind;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Classic 3-item example: A(w=2,v=6), B(w=2,v=10), C(w=3,v=12), W=5.
 * Optimal = 22 (items B + C).
 */
public class KnapsackProblemTest {

    private static final String[] NAMES   = {"A", "B", "C"};
    private static final int[]    WEIGHTS = {2, 2, 3};
    private static final int[]    VALUES  = {6, 10, 12};
    private static final int      CAP     = 5;

    private KnapsackProblem problem;

    @BeforeEach
    public void setUp() {
        problem = new KnapsackProblem(NAMES, WEIGHTS, VALUES, CAP);
    }

    @Test public void testProblemKind() {
        assertEquals(ProblemKind.KNAPSACK_0_1, problem.getType());
    }

    @Test public void testInitialStateNotFinished() {
        assertFalse(problem.hasFinished());
    }

    @Test public void testCapacityAndN() {
        assertEquals(CAP, problem.getCapacity());
        assertEquals(3, problem.getN());
    }

    @Test public void testArraysCloned() {
        int[] w = problem.getWeights();
        w[0] = 99;
        assertNotEquals(99, problem.getWeights()[0]);
    }

    @Test public void testConstructorRejectsNullArrays() {
        assertThrows(IllegalArgumentException.class,
                () -> new KnapsackProblem(null, WEIGHTS, VALUES, CAP));
    }

    @Test public void testConstructorRejectsMismatchedArrays() {
        assertThrows(IllegalArgumentException.class,
                () -> new KnapsackProblem(NAMES, new int[]{1}, VALUES, CAP));
    }

    @Test public void testConstructorRejectsZeroCapacity() {
        assertThrows(IllegalArgumentException.class,
                () -> new KnapsackProblem(NAMES, WEIGHTS, VALUES, 0));
    }

    @Test public void testOptimalValueAfterFullExecution() {
        while (!problem.hasFinished()) problem.step();
        int[][] dp = problem.getDpTable();
        assertEquals(22, dp[problem.getN()][problem.getCapacity()]);
    }

    @Test public void testHasFinishedAfterFullExecution() {
        while (!problem.hasFinished()) problem.step();
        assertTrue(problem.hasFinished());
    }

    @Test public void testResetClearsDpTable() {
        problem.step(); problem.step();
        problem.reset();
        for (int[] row : problem.getDpTable())
            for (int cell : row)
                assertEquals(0, cell);
    }

    @Test public void testResetRestoresNotFinished() {
        while (!problem.hasFinished()) problem.step();
        problem.reset();
        assertFalse(problem.hasFinished());
    }

    @Test public void testBacktrackNotReadyAtStart() {
        assertFalse(problem.backtrackReady());
    }

    @Test public void testBacktrackReadyAfterForwardExecution() {
        while (!problem.hasFinished()) problem.step();
        assertTrue(problem.backtrackReady());
    }
}
