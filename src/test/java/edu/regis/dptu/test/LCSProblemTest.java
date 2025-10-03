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

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import edu.regis.dptu.model.LCSProblem;

/**
 * @author rickb
 */
public class LCSProblemTest {

    public LCSProblemTest() {}

    @BeforeAll
    public static void setUpClass() {}

    @AfterAll
    public static void tearDownClass() {}

    @BeforeEach
    public void setUp() {}

    @AfterEach
    public void tearDown() {}

    /** Test the step() and undo() methods of Problem */
    @Test
    public void testAll() {
        String x = "skullandbones"; // n == 13
        String y = "lullabybabies";

        LCSProblem problem = new LCSProblem(x, y);
        problem.reset();

        int n = problem.getVariableValue("n");
        int m = problem.getVariableValue("m");

        // assertEquals(x.length(), problem.getN());
        // assertEquals(y.length(), problem.getM());
        assertEquals(x.length(), n);
        assertEquals(y.length(), m);
        assertEquals(LCSProblem.EXECUTION_STATE.PRE, problem.getExecutionState());

        problem.step(); // execute Line 0 LCS(x,y);
        problem.step(); // enter Line1 r_loop advancing r from -1 to 0;

        assertEquals(LCSProblem.EXECUTION_STATE.R_LOOP, problem.getExecutionState());

        problem.step(); // execute Line 2 L[1,-1]  really L[1,0] in Java

        // Finish the r loop
        for (int ii = 0; ii < n; ii++) {
            problem.step(); // Line 1 for i = 1 to n-1
            problem.step(); // Line 2   L[i,-1] = 0
        }

        problem.step(); // Line1 increments r past end of loop, fall out of r-loop
        assertEquals(LCSProblem.EXECUTION_STATE.C_LOOP, problem.getExecutionState());

        problem.step(); // c from -1 to 1
        problem.step(); // Line 4

        // finish the c loop
        for (int ii = 0; ii < m - 1; ii++) {
            problem.step();
            problem.step();
        }

        problem.step(); // Line3 increment exceeds c value fall out of c-loop

        assertEquals(LCSProblem.EXECUTION_STATE.I_LOOP, problem.getExecutionState());

        // i == -1 and j == -1, step will assign i = 1
        problem.step(); // Line 5

        // j == -1 to j = 0
        problem.step(); // Line 6

        assertEquals(LCSProblem.EXECUTION_STATE.J_LOOP, problem.getExecutionState());

        problem.step(); // Line 7 if x[i] == y[j]

        problem.step(); // Line 10

        for (int xj = 2; xj < n + 1; xj++) {
            problem.step(); // Line 6  for j
            problem.step(); // Line 7  if x[i] == y[j]
            problem.step(); // Line 8 then or Line 10 else
        }

        assertEquals(LCSProblem.EXECUTION_STATE.J_LOOP, problem.getExecutionState());

        problem.step(); // break out of J-Loop

        assertEquals(LCSProblem.EXECUTION_STATE.I_LOOP, problem.getExecutionState());

        // We're about to do LCS row 2 (Java row 3)
        // then all remaining rows

        for (int xi = 3; xi < n + 2; xi++) {
            problem.step(); // increment i
            assertEquals(LCSProblem.EXECUTION_STATE.J_LOOP, problem.getExecutionState());

            for (int xj = 1; xj < n + 1; xj++) {
                problem.step(); // Line 6  for j
                problem.step(); // Line 7  if x[i] == y[j]
                problem.step(); // Line 8 then or Line 10 else
            }
            // In-J
            problem.step(); // breakout of J
            assertEquals(LCSProblem.EXECUTION_STATE.I_LOOP, problem.getExecutionState());
        }

        problem.step(); // break out of i loop

        assertEquals(LCSProblem.EXECUTION_STATE.RETRN, problem.getExecutionState());

        problem.step();

        problem.prettyPrint();

        // Start undoing
        assertEquals(LCSProblem.EXECUTION_STATE.POST, problem.getExecutionState());

        problem.undo(); // Line 11

        assertEquals(LCSProblem.EXECUTION_STATE.I_LOOP, problem.getExecutionState());

        for (int xx = 0; xx < 591; xx++) problem.undo();

        assertEquals(LCSProblem.EXECUTION_STATE.PRE, problem.getExecutionState());

        problem.prettyPrint();
    }

    /**
     * Test method for stepRLoop() method.
     *
     * @author EverettCV
     */
    @Test
    public void testRLoop() {
        /*
                System.out.println("\nPerforming testRLoop() method:\n\n");

                String x = "skullandbones";
                String y = "lullabybabies";

                LCSProblem problem = new LCSProblem(x, y);
                problem.reset();

                Random rand = new Random();

                int stepCount = rand.nextInt(14);

                System.out.println("stepCount = " + stepCount);

                problem.stepRLoop(stepCount);

                problem.prettyPrint();
        */
    }

    /**
     * Test method for stepRLoop() method.
     *
     * @author EverettCV
     */
    @Test
    public void testCLoop() {
        /*
        System.out.println("\nPerforming testCLoop() method:\n\n");

        String x = "skullandbones";
        String y = "lullabybabies";

        LCSProblem problem = new LCSProblem(x, y);
        problem.reset();

        Random rand = new Random();

        int stepCount = rand.nextInt(14);

        System.out.println("stepCount = " + stepCount);

        problem.stepCLoop(stepCount);

        problem.prettyPrint();
        */

    }

    /**
     * Test method for stepIJLoop() method.
     *
     * @author EverettCV
     */
    @Test
    public void testIJLoop() {
        /*
                System.out.println("\nPerforming testIJLoop() method:\n\n");

                String x = "skullandbones";
                String y = "lullabybabies";

                LCSProblem problem = new LCSProblem(x, y);
                problem.reset();

                Random rand = new Random();

                int stepCountI = rand.nextInt(y.length());
                int stepCountJ = rand.nextInt(x.length());

                System.out.println("stepCountI = " + stepCountI);
                System.out.println("stepCountJ = " + stepCountJ);

                problem.stepIJLoop(stepCountI, stepCountJ);

                problem.prettyPrint();
        */
    }
}
